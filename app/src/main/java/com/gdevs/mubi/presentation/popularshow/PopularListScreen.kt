package com.gdevs.mubi.presentation.popularshow

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.gdevs.mubi.domain.model.TvShowModel
import com.gdevs.mubi.presentation.components.RatingBar
import com.google.accompanist.coil.CoilImage

@Composable
fun PopularListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Popular Shows", fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            PopularList(navController = navController)
        }
    }
}

@Composable
fun PopularList(
    navController: NavController,
    viewModel: PopularViewModel = hiltNavGraphViewModel()
) {
    val tvShowList by remember { viewModel.tvShowList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    LazyColumn( //is recyclerview
        contentPadding = PaddingValues(8.dp)
    ) {
        val itemCount = if (tvShowList.size % 2 == 0) {
            tvShowList.size / 2
        } else {
            tvShowList.size / 2 + 1
        }

        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading) {//PAGINATE
                viewModel.loadTvShowPaginated()
            }
            TvShowRow(rowIndex = it, entries = tvShowList, navController = navController)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadTvShowPaginated()
            }
        }
    }
}

@Composable
fun TvShowEntry(
    entry: TvShowModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .width(175.dp)
            .height(216.dp)
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
    ) {

        Column {
            CoilImage(
                request = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w220_and_h330_face" + entry.poster)
                    .build(),
                contentDescription = entry.name,
                fadeIn = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(136.dp)
                    .weight(0.6f),
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.6f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(0.3f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = entry.name, style = MaterialTheme.typography.body2)

                Row()
                {
                    RatingBar(rating = entry.rate.toFloat(), spaceBetween = 2.dp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = (entry.rate.toFloat() / 2f).toString(),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }

    }
}

@Composable
fun TvShowRow(
    rowIndex: Int,
    entries: List<TvShowModel>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            TvShowEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                TvShowEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}
