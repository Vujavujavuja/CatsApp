package com.vujic.rma1.list
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.vujic.rma1.list.model.Cat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image as Image
import androidx.compose.ui.text.style.TextAlign


fun NavGraphBuilder.catList(
    route: String,
    onCatSelected: (String) -> Unit,
) = composable(route) {
    var listViewModel = viewModel<ListViewModel>()

    val state = listViewModel.state.collectAsState()
    CatListScreen(
        state = state.value,
        eventPublisher = {
            listViewModel.publishEvent(it)
        },
        onCatSelected = onCatSelected
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    state: ListContract.CatListState,
    eventPublisher: (ListContract.CatListEvent) -> Unit,
    onCatSelected: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            Column {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = { query ->
                        eventPublisher(ListContract.CatListEvent.SearchQueryChanged(query))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Search cats...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    trailingIcon = {
                        IconButton(onClick = {
                            eventPublisher(ListContract.CatListEvent.ClearSearch)
                            scrollToTop(LazyListState())
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Search",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                MediumTopAppBar(
                    title = {
                        Text(
                            "List of cats:",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                    )
                )


            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                ) {
                    items(
                        items = if (state.query.isNotEmpty()) state.filteredCats else state.cats,
                        key = { it.id },
                    ) { cat ->
                        CatCard(cat, onCatSelected)
                    }
                }
            }
        }
    )
}


@Composable
fun CatCard(cat: Cat, onCatSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onCatSelected(cat.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            cat.image?.let { image ->
                Image(
                    painter = rememberAsyncImagePainter(image.url),
                    contentDescription = cat.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cat.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (cat.alt_names != null) cat.alt_names else cat.origin,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (cat.description.length > 250) "${cat.description.take(250)}..." else cat.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCatList() {
    CatListScreen(
        state = ListContract.CatListState(loading = false, cats = listOf()),
        eventPublisher = {},
        onCatSelected = {},
    )
}

fun scrollToTop(listState: LazyListState) {
    CoroutineScope(Dispatchers.Main).launch {
        listState.animateScrollToItem(index = 0)
    }
}
