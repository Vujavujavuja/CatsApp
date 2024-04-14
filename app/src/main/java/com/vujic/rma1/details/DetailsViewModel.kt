package com.vujic.rma1.details

import androidx.browser.customtabs.CustomTabsIntent
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val catId: String,
    private val repository: Repository = Repository,
) : ViewModel() {
    private val _state = MutableStateFlow(DetailsContract.CatDetailsState())
    val state = _state.asStateFlow()

    private val events = MutableSharedFlow<DetailsContract.CatDetailsEvent>()
    fun setEvent(event: DetailsContract.CatDetailsEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        fetchCatDetails()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is DetailsContract.CatDetailsEvent.OpenWiki -> {
                        val customTabsIntent = CustomTabsIntent.Builder().build()
                        event.url?.let { url -> customTabsIntent.launchUrl(event.context, url) }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun fetchCatDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(loading = true) }
            try {
                val catA = repository.fetchBreedById(catId)
                val cat = catA.asCatUIModel()
                val imagesA = repository.fetchBreedImages(catId)
                if (imagesA != null) {
                    cat.image = imagesA.asImageUIModel()
                }

                _state.update { it.copy(cat = cat, loading = false) }
            } catch (error: Exception) {
                println("Error fetching cat details: $error")
                _state.update { it.copy(loading = false) }
            }
        }
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
