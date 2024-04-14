package com.vujic.rma1.details

import android.content.Context
import android.net.Uri
import com.vujic.rma1.list.model.Cat

interface DetailsContract {
    data class CatDetailsState(
        val loading: Boolean = false,
        val cat: Cat? = null,
    )

    sealed class CatDetailsEvent {
        data class OpenWiki(val url: Uri?, val context: Context) : CatDetailsEvent()
        data object Dummy : CatDetailsEvent()
    }
}