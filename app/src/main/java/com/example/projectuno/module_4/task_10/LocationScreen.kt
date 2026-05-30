package com.example.projectuno.module_4.task_10

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationRepository(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getLocationUpdates(interval: Long = 5000L): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            interval
        ).apply {
            setMinUpdateIntervalMillis(interval / 2)
            setWaitForAccurateLocation(false)
        }.build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): Location? {
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen() {
    val context = LocalContext.current
    val repository = remember { LocationRepository(context) }
    val scope = rememberCoroutineScope()

    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var lastKnownLocation by remember { mutableStateOf<Location?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var locationHistory by remember { mutableStateOf<List<Location>>(emptyList()) }

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(isTracking) {
        if (isTracking && locationPermissionsState.allPermissionsGranted) {
            repository.getLocationUpdates().collect { location ->
                currentLocation = location
                locationHistory = (locationHistory + location).takeLast(10)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Location Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (!locationPermissionsState.allPermissionsGranted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Location Permission Required",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = "This app needs location permission to show your current position",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = { locationPermissionsState.launchMultiplePermissionRequest() }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)) {
                    Text(
                        text = "Current Location",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                    if (currentLocation != null) {
                        LocationInfo(currentLocation!!)
                    } else {
                        Text(
                            text = "No location data yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                isTracking = !isTracking
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                if (isTracking) "Stop Tracking" else "Start Tracking"
                            )
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    lastKnownLocation = repository.getLastKnownLocation()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text("Get Last Known")
                        }
                    }
                }
            }

            if (lastKnownLocation != null) {
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
                            text = "Last Known Location",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        )
                        LocationInfo(lastKnownLocation!!)
                    }
                }
            }

            if (locationHistory.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Location History (${locationHistory.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(onClick = { locationHistory = emptyList() }) {
                                Text("Clear")
                            }
                        }

                        locationHistory.reversed().forEach { location ->
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            LocationInfo(location, compact = true)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationInfo(location: Location, compact: Boolean = false) {
    Column {
        InfoRow("Latitude", "%.6f".format(location.latitude))
        InfoRow("Longitude", "%.6f".format(location.longitude))

        if (!compact) {
            if (location.hasAccuracy()) {
                InfoRow("Accuracy", "%.2f m"
                    .format(location.accuracy)
                )
            }
            if (location.hasAltitude()) {
                InfoRow("Altitude", "%.2f m"
                    .format(location.altitude)
                )
            }
            if (location.hasSpeed()) {
                InfoRow("Speed", "%.2f m/s"
                    .format(location.speed)
                )
            }
            if (location.hasBearing()) {
                InfoRow("Bearing", "%.2f°"
                    .format(location.bearing)
                )
            }
            InfoRow("Provider", location.provider ?: "Unknown")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
