package com.vujic.rma1.list

import com.vujic.rma1.list.model.Cat

interface ListContract {
    data class CatListState(
        val loading: Boolean = false,
        val query: String = "",
        val isSearchMode: Boolean = false,
        val cats: List<Cat> = emptyList(),
        val filteredCats: List<Cat> = emptyList(),
    )

    sealed class CatListEvent {
        data class SearchQueryChanged(val query: String) : CatListEvent()
        data object ClearSearch : CatListEvent()
        data object CloseSearchMode : CatListEvent()
        data object OpenSearchMode : CatListEvent()
        data object Dummy : CatListEvent()
    }
}