package com.example.projectuno.module_4.task_14

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

data class SensorData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f,
    val magnitude: Float = 0f
)

class SensorRepository(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    val magnetometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    val lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    fun registerListener(sensor: Sensor?, listener: SensorEventListener, samplingRate: Int) {
        sensor?.let {
            sensorManager.registerListener(listener, it, samplingRate)
        }
    }

    fun unregisterListener(listener: SensorEventListener) {
        sensorManager.unregisterListener(listener)
    }

    fun getSensorInfo(sensor: Sensor?): String {
        return sensor?.let {
            "Name: ${it.name}\nVendor: ${it.vendor}\nMax Range: ${it.maximumRange}\nResolution: ${it.resolution}"
        } ?: "Sensor not available"
    }
}

@Composable
fun SensorScreen() {
    val context = LocalContext.current
    val repository = remember { SensorRepository(context) }

    var accelerometerData by remember { mutableStateOf(SensorData()) }
    var gyroscopeData by remember { mutableStateOf(SensorData()) }
    var magnetometerData by remember { mutableStateOf(SensorData()) }
    var lightLevel by remember { mutableStateOf(0f) }

    var isAccelerometerActive by remember { mutableStateOf(false) }
    var isGyroscopeActive by remember { mutableStateOf(false) }
    var isMagnetometerActive by remember { mutableStateOf(false) }
    var isLightSensorActive by remember { mutableStateOf(false) }

    var selectedSamplingRate by remember { mutableStateOf(SensorManager.SENSOR_DELAY_NORMAL) }

    val accelerometerListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    accelerometerData = SensorData(x, y, z, magnitude)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    val gyroscopeListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    gyroscopeData = SensorData(x, y, z, magnitude)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    val magnetometerListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]
                    val magnitude = sqrt(x * x + y * y + z * z)
                    magnetometerData = SensorData(x, y, z, magnitude)
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    val lightSensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    lightLevel = it.values[0]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            repository.unregisterListener(accelerometerListener)
            repository.unregisterListener(gyroscopeListener)
            repository.unregisterListener(magnetometerListener)
            repository.unregisterListener(lightSensorListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Sensors Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Sampling Rate",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedSamplingRate == SensorManager.SENSOR_DELAY_FASTEST,
                        onClick = { selectedSamplingRate = SensorManager.SENSOR_DELAY_FASTEST },
                        label = { Text("Fastest") }
                    )
                    FilterChip(
                        selected = selectedSamplingRate == SensorManager.SENSOR_DELAY_GAME,
                        onClick = { selectedSamplingRate = SensorManager.SENSOR_DELAY_GAME },
                        label = { Text("Game") }
                    )
                    FilterChip(
                        selected = selectedSamplingRate == SensorManager.SENSOR_DELAY_UI,
                        onClick = { selectedSamplingRate = SensorManager.SENSOR_DELAY_UI },
                        label = { Text("UI") }
                    )
                    FilterChip(
                        selected = selectedSamplingRate == SensorManager.SENSOR_DELAY_NORMAL,
                        onClick = { selectedSamplingRate = SensorManager.SENSOR_DELAY_NORMAL },
                        label = { Text("Normal") }
                    )
                }
            }
        }

        SensorCard(
            title = "Accelerometer",
            sensorData = accelerometerData,
            isActive = isAccelerometerActive,
            isAvailable = repository.accelerometer != null,
            onToggle = {
                isAccelerometerActive = !isAccelerometerActive
                if (isAccelerometerActive) {
                    repository.registerListener(
                        repository.accelerometer,
                        accelerometerListener,
                        selectedSamplingRate
                    )
                } else {
                    repository.unregisterListener(accelerometerListener)
                    accelerometerData = SensorData()
                }
            }
        )

        SensorCard(
            title = "Gyroscope",
            sensorData = gyroscopeData,
            isActive = isGyroscopeActive,
            isAvailable = repository.gyroscope != null,
            onToggle = {
                isGyroscopeActive = !isGyroscopeActive
                if (isGyroscopeActive) {
                    repository.registerListener(
                        repository.gyroscope,
                        gyroscopeListener,
                        selectedSamplingRate
                    )
                } else {
                    repository.unregisterListener(gyroscopeListener)
                    gyroscopeData = SensorData()
                }
            }
        )

        SensorCard(
            title = "Magnetometer",
            sensorData = magnetometerData,
            isActive = isMagnetometerActive,
            isAvailable = repository.magnetometer != null,
            onToggle = {
                isMagnetometerActive = !isMagnetometerActive
                if (isMagnetometerActive) {
                    repository.registerListener(
                        repository.magnetometer,
                        magnetometerListener,
                        selectedSamplingRate
                    )
                } else {
                    repository.unregisterListener(magnetometerListener)
                    magnetometerData = SensorData()
                }
            }
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Column(modifier = Modifier
                .padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Light Sensor",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Switch(
                        checked = isLightSensorActive,
                        onCheckedChange = {
                            isLightSensorActive = !isLightSensorActive
                            if (isLightSensorActive) {
                                repository.registerListener(
                                    repository.lightSensor,
                                    lightSensorListener,
                                    selectedSamplingRate
                                )
                            } else {
                                repository.unregisterListener(lightSensorListener)
                                lightLevel = 0f
                            }
                        },
                        enabled = repository.lightSensor != null
                    )
                }

                if (repository.lightSensor == null) {
                    Text(
                        text = "Light sensor not available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (isLightSensorActive) {
                    Text(
                        text = "Light Level: %.2f lux"
                            .format(lightLevel),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SensorCard(
    title: String,
    sensorData: SensorData,
    isActive: Boolean,
    isAvailable: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                Switch(
                    checked = isActive,
                    onCheckedChange = { onToggle() },
                    enabled = isAvailable
                )
            }

            if (!isAvailable) {
                Text(
                    text = "Sensor not available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else if (isActive) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        SensorValueRow("X", sensorData.x)
                        SensorValueRow("Y", sensorData.y)
                        SensorValueRow("Z", sensorData.z)
                        SensorValueRow("Magnitude", sensorData.magnitude)
                    }

                    SensorVisualization(
                        x = sensorData.x,
                        y = sensorData.y,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SensorValueRow(label: String, value: Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "%.3f".format(value),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SensorVisualization(x: Float, y: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.minDimension / 2

        drawCircle(
            color = Color.Gray,
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )

        drawCircle(
            color = Color.Gray,
            radius = radius / 2,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1f)
        )

        val scale = radius / 10f
        val dotX = centerX + (x * scale).coerceIn(-radius, radius)
        val dotY = centerY + (y * scale).coerceIn(-radius, radius)

        drawCircle(
            color = Color.Blue,
            radius = 8f,
            center = Offset(dotX, dotY)
        )

        drawLine(
            color = Color.Red,
            start = Offset(centerX, centerY),
            end = Offset(dotX, dotY),
            strokeWidth = 2f
        )
    }
}
