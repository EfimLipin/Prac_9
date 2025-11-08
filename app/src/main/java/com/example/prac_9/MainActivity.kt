package com.example.prac_9

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class Cart(val products: List<Product>)
data class Product(val id: Int, val name: String, val price: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingCartScreen()
        }
    }
}

@Composable
fun ShoppingCartScreen() {
    val context = LocalContext.current

    var products by remember {
        mutableStateOf(
            listOf(
                Product(0, "Товар #1", 100),
                Product(1, "Товар #2", 150),
                Product(2, "Товар #3", 56)
            )
        )
    }

    val totalSum by derivedStateOf { products.sumOf { it.price } }
    val productSize by derivedStateOf { products.size }

    var shownFreeDeliveryForSum by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(totalSum) {
        if (totalSum > 500 && shownFreeDeliveryForSum != totalSum) {
            Toast.makeText(context, "Доставка бесплатная!", Toast.LENGTH_SHORT).show()
            shownFreeDeliveryForSum = totalSum
        }
        if (totalSum <= 500) shownFreeDeliveryForSum = null
    }

    Column(modifier = Modifier.padding(16.dp)) {
        for (product in products) {
            Text(text = "${product.name} - ${product.price} рублей")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Товаров на сумму: $totalSum рублей")

        Spacer(modifier = Modifier.height(16.dp))

        AddProductSection {
            val newProduct = Product(
                id = products.size,
                name = "Товар #${products.size + 1}",
                price = Random.nextInt(0, 100)
            )
            products = products + newProduct
        }

        Spacer(modifier = Modifier.height(8.dp))

        RemoveProductSection(enabled = productSize > 0) {
            if (products.isNotEmpty()) {
                products = products.dropLast(1)
            }
        }
    }
}

@Composable
fun AddProductSection(onAdd: () -> Unit) {
    Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Добавить товар")
    }
}

@Composable
fun RemoveProductSection(enabled: Boolean, onRemove: () -> Unit) {
    if (enabled) {
        Button(onClick = onRemove, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Удалить товар")
        }
    }
}
