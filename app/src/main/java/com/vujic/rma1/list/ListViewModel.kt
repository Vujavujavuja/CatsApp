package com.vujic.rma1.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vujic.rma1.api.model.CatApiImage
import com.vujic.rma1.api.model.CatApiModel
import com.vujic.rma1.api.model.CatApiWeight
import com.vujic.rma1.list.model.Cat
import com.vujic.rma1.list.model.Image
import com.vujic.rma1.list.model.Weight
import com.vujic.rma1.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ListViewModel(
    private val repository: Repository = Repository,
) : ViewModel() {
    private val _state = MutableStateFlow(ListContract.CatListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: ListContract.CatListState.() -> ListContract.CatListState) = _state.update(reducer)

    private val events = MutableSharedFlow<ListContract.CatListEvent>()
    fun setEvent(event: ListContract.CatListEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchAllCats()
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            events
                .filterIsInstance<ListContract.CatListEvent.SearchQueryChanged>()
                .debounce(2.seconds)
                .collect {
                    setEvent(ListContract.CatListEvent.Dummy)
                }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is ListContract.CatListEvent.SearchQueryChanged -> {
                        println(it)
                        setState { copy(query = it.query, filteredCats = state.value.cats.filter { cat ->
                            cat.name.contains(it.query, ignoreCase = true) ||
                                    cat.description.contains(it.query, ignoreCase = true)
                        })}
                    }
                    is ListContract.CatListEvent.OpenSearchMode -> setState { copy(isSearchMode = true) }
                    is ListContract.CatListEvent.ClearSearch -> setState { copy(query = "", isSearchMode = false) }
                    is ListContract.CatListEvent.CloseSearchMode -> setState { copy(isSearchMode = false) }

                    else -> {

                    }
                }
            }
        }
    }

    private fun fetchAllCats() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loading = true) }
            runCatching {
                val cats = repository.fetchAllBreeds()
                cats.map { it.asCatUIModel() }
            }.onSuccess { breeds ->
                _state.update { it.copy(cats = breeds, loading = false) }
            }.onFailure { error ->
                println("Error fetching cats: $error")
                _state.update { it.copy(loading = false) }
            }
        }
    }

    fun publishEvent(it: ListContract.CatListEvent) {
        setEvent(it)
    }

    private fun CatApiModel.asCatUIModel() = Cat(
        id = id,
        name = name,
        description = description,
        origin = origin,
        temperament = temperament,
        image = image?.asImageUIModel(),
        weight = weight?.asWeightUIModel(),
        energy_level = energy_level,
        affection_level = affection_level,
        child_friendly = child_friendly,
        dog_friendly = dog_friendly,
        stranger_friendly = stranger_friendly,
        alt_names = alt_names,
        country_codes = country_codes,
        life_span = life_span,
        wikipedia_url = wikipedia_url,
        rare = rare
    )

    private fun CatApiImage.asImageUIModel() = Image(
        id = id,
        url = url
    )

    private fun CatApiWeight.asWeightUIModel() = Weight(
        imperial = imperial,
        metric = metric
    )
}