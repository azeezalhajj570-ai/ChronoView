package com.chronoview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chronoview.viewmodel.ChronoViewViewModel

@Composable
fun ProductDetailScreen(
    viewModel: ChronoViewViewModel,
    watchId: Int,
    onBack: () -> Unit
) {
    val watch = remember(watchId) { viewModel.getWatchById(watchId) } ?: return
    val aiDescription = remember(watchId) { viewModel.generateAiDescription(watch) }
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { watch.imageUrls.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            AsyncImage(
                model = watch.imageUrls[page],
                contentDescription = "${watch.name} image ${page + 1}",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            repeat(watch.imageUrls.size) { index ->
                val color = if (pagerState.currentPage == index) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(watch.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("${watch.brand} • ${watch.category}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.outline)
                }
                Text(
                    "$${"%.2f".format(watch.price)}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Text("⭐ ${watch.rating}", style = MaterialTheme.typography.bodyLarge)
            
            Spacer(modifier = Modifier.height(16.dp))
            Text("Description", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(aiDescription, style = MaterialTheme.typography.bodyLarge, lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.addToCart(watch) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Add to Cart", style = MaterialTheme.typography.titleMedium)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
