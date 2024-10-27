package com.marvingudiel.app_prestamos.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.marvingudiel.app_prestamos.Components.Alert
import com.marvingudiel.app_prestamos.Components.MainButton
import com.marvingudiel.app_prestamos.Components.MainTextField
import com.marvingudiel.app_prestamos.Components.ShowInfoCards
import com.marvingudiel.app_prestamos.Components.SpaceH
import java.math.BigDecimal
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Calculadora de cuotas") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        ContentHomeView(paddingValues)
    }
}

@Composable
fun ContentHomeView(paddingValues: PaddingValues) {
    var montoPrestamo by remember { mutableStateOf("") }
    var cantCuotas by remember { mutableStateOf("") }
    var tasa by remember { mutableStateOf("") }
    var montoInteres by remember { mutableStateOf(0.0) }
    var montoCuota by remember { mutableStateOf(0.0) }
    var showAlert by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(10.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShowInfoCards(
            titleInteres = "Interés",
            montoInteres = montoInteres,
            titleMonto = "Monto",
            monto = montoCuota
        )
        MainTextField(value = montoPrestamo, onValueChange = { montoPrestamo = it }, label = "Préstamo")
        SpaceH()
        MainTextField(value = cantCuotas, onValueChange = { cantCuotas = it }, label = "Cuotas")
        SpaceH(10.dp)
        MainTextField(value = tasa, onValueChange = { tasa = it }, label = "Tasa")

        MainButton(text = "Calcular") {
            if (montoPrestamo.isNotEmpty() && cantCuotas.isNotEmpty() && tasa.isNotEmpty()) {
                montoInteres = calcularTotal(montoPrestamo.toDouble(), tasa.toDouble())
                montoCuota = calcularCuota(montoPrestamo.toDouble(), cantCuotas.toInt(), tasa.toDouble())
            } else {
                showAlert = true
            }
        }
        SpaceH()
        MainButton(text = "Borrar", color = Color.Red) {
            montoPrestamo = ""
            cantCuotas = ""
            tasa = ""
            montoInteres = 0.0
            montoCuota = 0.0
        }

        if (showAlert) {
            Alert(
                title = "Error",
                message = "Por favor, completa todos los campos.",
                confirmText = "Aceptar",
                onConfirmClick = { showAlert = false },
                onDismiss = { showAlert = false }
            )
        }
    }
}

fun calcularTotal(monto: Double, tasa: Double): Double {
    val interes = monto * (tasa / 100)
    return BigDecimal(interes).setScale(2, RoundingMode.UP).toDouble()
}

fun calcularCuota(monto: Double, cuotas: Int, tasa: Double): Double {
    val tasaInteresMensual = tasa / 12 / 100
    val cuota = monto * tasaInteresMensual * Math.pow(1 + tasaInteresMensual, cuotas.toDouble()) /
            (Math.pow(1 + tasaInteresMensual, cuotas.toDouble()) - 1)
    return BigDecimal(cuota).setScale(2, RoundingMode.UP).toDouble()
}







