package com.chronoview.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chronoview.data.Watch
import com.chronoview.data.WatchCategory
import com.chronoview.viewmodel.ChronoViewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: ChronoViewViewModel,
    onBack: () -> Unit
) {
    val watches by viewModel.watches.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedWatch by remember { mutableStateOf<Watch?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedWatch = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(watches) { watch ->
                AdminWatchCard(
                    watch = watch,
                    onEdit = {
                        selectedWatch = watch
                        showDialog = true
                    },
                    onDelete = { viewModel.deleteProduct(watch) }
                )
            }
        }

        if (showDialog) {
            ProductEditDialog(
                watch = selectedWatch,
                onDismiss = { showDialog = false },
                onSave = {
                    viewModel.saveProduct(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AdminWatchCard(watch: Watch, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = watch.imageUrls.firstOrNull(),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(watch.name, style = MaterialTheme.typography.titleMedium)
                Text(watch.brand, style = MaterialTheme.typography.bodySmall)
                Text("$${watch.price}", color = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditDialog(
    watch: Watch?,
    onDismiss: () -> Unit,
    onSave: (Watch) -> Unit
) {
    var name by remember { mutableStateOf(watch?.name ?: "") }
    var brand by remember { mutableStateOf(watch?.brand ?: "") }
    var price by remember { mutableStateOf(watch?.price?.toString() ?: "") }
    var imageUrl by remember { mutableStateOf(watch?.imageUrls?.firstOrNull() ?: "") }
    var description by remember { mutableStateOf(watch?.baseDescription ?: "") }
    var category by remember { mutableStateOf(watch?.category ?: WatchCategory.Casual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (watch == null) "Add Product" else "Edit Product") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") })
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Image URL") })
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                
                Text("Category", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    WatchCategory.entries.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    Watch(
                        id = watch?.id ?: 0,
                        name = name,
                        brand = brand,
                        category = category,
                        price = price.toDoubleOrNull() ?: 0.0,
                        rating = watch?.rating ?: 4.5,
                        imageUrls = listOf(imageUrl),
                        baseDescription = description
                    )
                )
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
