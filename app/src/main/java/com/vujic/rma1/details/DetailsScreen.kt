package com.vujic.rma1.details

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.vujic.rma1.list.model.Cat

fun NavGraphBuilder.catDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    onClose: () -> Unit
) = composable(route, arguments = arguments) { navBackStackEntry ->
    val catId = navBackStackEntry.arguments?.getString("catId")
        ?: throw IllegalStateException("catId required")

    var detailsViewModel = viewModel<DetailsViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailsViewModel(catId = catId) as T
            }
        }
    )

    val state = detailsViewModel.state.collectAsState()

    CatDetailsScreen(
        state = state.value,
        eventPublisher = {
            detailsViewModel.setEvent(it)
        },
        onClose = onClose,
    )

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    state: DetailsContract.CatDetailsState,
    eventPublisher: (DetailsContract.CatDetailsEvent) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = "Cat Details", onClose = onClose)
        },
        content = { paddingValues ->
            if (state.loading) {
                LoadingIndicator()
            } else {
                state.cat?.let { cat ->
                    CatDetailsContent(cat = cat, eventPublisher = eventPublisher)
                }
            }
        }
    )
}

@Composable
fun TopAppBar(title: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsContent(
    cat: Cat,
    eventPublisher: (DetailsContract.CatDetailsEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        CatImage(cat.image?.url, cat.name)
        Text(text = cat.name, style = MaterialTheme.typography.headlineMedium)
        if (cat.rare == 1) {
            Badge(content = { Text("Rare") })
        }
        Text("Origin: ${cat.origin}", style = MaterialTheme.typography.bodyLarge)
        Text(cat.description, style = MaterialTheme.typography.bodyMedium)
        Text("Temperaments: ${cat.temperament}", style = MaterialTheme.typography.bodyMedium)
        Text("Life Span: ${cat.life_span} years", style = MaterialTheme.typography.bodyMedium)
        Text("Weight: ${cat.weight?.metric} kg", style = MaterialTheme.typography.bodyMedium)
        Text("Countries: ${cat.country_codes}", style = MaterialTheme.typography.bodyMedium)
        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
            BehavioralTrait("Affection Level", cat.affection_level)
            BehavioralTrait("Child Friendly", cat.child_friendly)
            BehavioralTrait("Dog Friendly", cat.dog_friendly)
            BehavioralTrait("Energy Level", cat.energy_level)
            BehavioralTrait("Stranger Friendly", cat.stranger_friendly)
        }
        WikipediaButton(cat.wikipedia_url) { uri, context ->
            openUri(uri, context)
        }
    }
}

@Composable
fun BehavioralTrait(name: String, value: Int) {
    Row {
        Text("$name: ", style = MaterialTheme.typography.bodyMedium)
        LinearProgressIndicator(
            progress = value / 5f,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .height(4.dp)
        )
    }
}


@Composable
fun CatImage(url: String?, description: String) {
    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = description,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CatInfo(cat: Cat) {
    Text(text = cat.name, style = MaterialTheme.typography.headlineMedium)
    if (cat.rare == 1) {
        Text("Rare", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyLarge)
    }
    Text("Origin: ${cat.origin}", style = MaterialTheme.typography.bodyLarge)

}

@Composable
fun CatCharacteristics(cat: Cat) {
    Characteristic("Life Span", "${cat.life_span} years")
    Characteristic("Weight", "${cat.weight?.metric} kg")
}

@Composable
fun Characteristic(name: String, detail: String) {
    Text("$name: $detail", style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun WikipediaButton(url: String?, eventPublisher: (Uri, Context) -> Unit) {
    val context = LocalContext.current
    Button(
        onClick = {
            url?.let {
                eventPublisher(it.toUri(), context)
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Learn More on Wikipedia")
    }
}

fun openUri(uri: Uri, context: Context) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, uri)
}

