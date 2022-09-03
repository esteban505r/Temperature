package com.valentina.temperature

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentina.temperature.ui.theme.TemperatureTheme
import com.valentina.temperature.viewmodel.temperature.TemperatureViewModel

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var temperature: Sensor? = null
    private val viewModel: TemperatureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        setContent {

            TemperatureTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Content()
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        viewModel.setProgress(event.values.first())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    @Composable
    fun Content() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TemperatureIndicator(viewModel.progressState.collectAsState().value)
        }
    }

    @Composable
    fun TemperatureIndicator(progress: Float) {
        val progressAnimation by animateFloatAsState(
           progress,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
        Box(contentAlignment = Alignment.Center) {
            Text(
                "$progress°C",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            )
            CircularProgressIndicator(
                progress = progressAnimation / 100, modifier = Modifier
                    .size(420.dp)
                    .padding(70.dp), strokeWidth = 15.dp,
                color = when(progress/100){
                    in 0f .. 0.4f -> Color(0xFF29b6f6)
                    in 0.4f .. 0.6f -> Color(0xFFffee58)
                    in 0.6f .. 0.8f -> Color(0xFFff5722)
                    in 0.8f .. 1f -> Color(0xFFf44336)
                    in -Float.MAX_VALUE .. 0f -> Color(0xFF81d4fa)
                    in 1f .. Float.MAX_VALUE ->Color(0xFFe53935)
                    else -> Color.Gray
                }
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        TemperatureTheme {
            TemperatureIndicator(0.1f)
        }
    }
}

