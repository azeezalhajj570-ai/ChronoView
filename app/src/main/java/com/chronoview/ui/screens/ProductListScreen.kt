package com.chronoview.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chronoview.data.WatchCategory
import com.chronoview.ui.components.WatchCard
import com.chronoview.viewmodel.ChronoViewViewModel

@Composable
fun ProductListScreen(
    viewModel: ChronoViewViewModel,
    onWatchClick: (Int) -> Unit
) {
    val uiState by viewModel.homeUiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = viewModel::setSearchQuery,
            label = { Text("Search by watch or brand") },
            modifier = Modifier.fillMaxWidth()
        )

        CategoryRow(selected = uiState.selectedCategory, onSelect = viewModel::setCategory)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.watches, key = { it.id }) { watch ->
                WatchCard(watch = watch, onClick = { onWatchClick(watch.id) })
            }
        }
    }
}

@Composable
private fun CategoryRow(selected: WatchCategory?, onSelect: (WatchCategory?) -> Unit) {
    val categories = listOf<WatchCategory?>(null, WatchCategory.Luxury, WatchCategory.Sport, WatchCategory.Casual)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selected
            AssistChip(
                onClick = { onSelect(category) },
                label = { Text(category?.name ?: "All") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                    labelColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
