package com.example.visitatcnica

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.visitatcnica.ui.theme.VisitaTécnicaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VisitaTécnicaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SolarInstallationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun SolarInstallationScreen() {
    var area by remember { mutableStateOf("") }
    var panelPower by remember { mutableStateOf("") }
    var displayResults by remember { mutableStateOf(false) }
    var panelArea by remember { mutableStateOf(0) }
    var totalCost by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Solar Installation Cost Calculator",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = area,
            onValueChange = { area = it },
            label = { Text("Enter the area of the installation (in square meters)") },
            modifier = Modifier.padding(16.dp)
        )

        TextField(
            value = panelPower,
            onValueChange = { panelPower = it },
            label = { Text("Enter the power of the panels (in Watts)") },
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                panelArea = calculatePanelArea(area.toFloat())
                val requiredEquipment = determineRequiredEquipment(panelPower.toFloat())
                totalCost = calculateTotalCost(panelArea, requiredEquipment)
                displayResults = true
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Calculate")
        }

        if (displayResults) {
            DisplayResults(panelArea, totalCost)
        }
    }
}

@Composable
fun DisplayResults(panelArea: Int, totalCost: Float) {
    Text(
        text = "The estimated number of panels required for the given area: $panelArea",
        modifier = Modifier.padding(16.dp)
    )
    Text(
        text = "The estimated total cost of the solar installation: €$totalCost",
        modifier = Modifier.padding(16.dp)
    )
}

fun calculatePanelArea(area: Float): Int {
    val panelArea = 1.5 // Assume each panel occupies 1.5 square meters
    return (area / panelArea).toInt()
}

fun determineRequiredEquipment(panelPower: Float): List<Float> {
    val requiredInverter = if (panelPower > 1500) {
        listOf(2000f, 349f) // Example values for a 2000W inverter and its price
    } else {
        listOf(1500f, 279f) // Example values for a 1500W inverter and its price
    }

    val requiredBattery = listOf(3000f, 899f) // Example values for a 3000W battery and its price
    val requiredCable = listOf(10f, 20f) // Example values for a 10-meter cable and its price

    return listOf(requiredInverter, requiredBattery, requiredCable).flatten()
}

fun calculateTotalCost(panelArea: Int, requiredEquipment: List<Float>): Float {
    val panelPrice = 134f // Example price for a 460W panel
    val inverterPrice = requiredEquipment[1]
    val batteryPrice = requiredEquipment[3]
    val cablePrice = requiredEquipment[5]

    val panelCost = panelArea * panelPrice
    val cableCost = requiredEquipment[4] * cablePrice

    return panelCost + inverterPrice + batteryPrice + cableCost
}
