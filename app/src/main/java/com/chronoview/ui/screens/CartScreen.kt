package com.chronoview.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chronoview.viewmodel.ChronoViewViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ChronoViewViewModel,
    onBack: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val items by viewModel.cartItems.collectAsState()
    val total by viewModel.totalPrice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("Shopping Cart", style = MaterialTheme.typography.headlineSmall)
        }

        if (items.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(items, key = { it.watchId }) { item ->
                    CartItemRow(item, viewModel)
                }
            }
            
            Divider()
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "$${"%.2f".format(total)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Button(
                onClick = { 
                    viewModel.checkout()
                    onNavigateToHistory()
                }, 
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Place Order")
            }
        }
    }
}

@Composable
fun CartItemRow(item: com.chronoview.data.CartItemUi, viewModel: ChronoViewViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(shape = MaterialTheme.shapes.small) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                Text(item.brand, style = MaterialTheme.typography.bodySmall)
                Text("$${"%.2f".format(item.unitPrice)}", color = MaterialTheme.colorScheme.primary)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { viewModel.decrement(item) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(20.dp))
                }
                Text(item.quantity.toString(), style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { viewModel.increment(item) }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}
