package com.tourpal.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.tourpal.R
import kotlinx.coroutines.launch

@Composable
fun MapComponent(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var bearing by remember { mutableStateOf(0f) }
    var isUserInteracting by remember { mutableStateOf(false) }
    var lastUserInteractionTime by remember { mutableStateOf(0L) }
    var isMapLoaded by remember { mutableStateOf(false) }
    var arrowIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var isDarkMode by remember { mutableStateOf(false) } // Track light/dark mode

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.6318, -8.6576), 15f) // Aveiro University
    }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_arrow, null)
        arrowIcon = drawable?.toBitmap(64, 64)?.let { BitmapDescriptorFactory.fromBitmap(it) }
    }

    // Track camera movements to detect user interaction
    LaunchedEffect(cameraPositionState.isMoving) {
        if (cameraPositionState.isMoving) {
            isUserInteracting = true
            lastUserInteractionTime = System.currentTimeMillis()
        } else {
            if (System.currentTimeMillis() - lastUserInteractionTime > 2000) {
                isUserInteracting = false
            }
        }
    }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted = isGranted
        if (isGranted) {
            startLocationUpdates(context, fusedLocationClient) { location ->
                currentLocation = location
                Log.d("MapComponent", "Location updated: $location")
                if (!isUserInteracting) {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.builder()
                                    .target(location)
                                    .zoom(cameraPositionState.position.zoom)
                                    .bearing(bearing)
                                    .tilt(if (cameraPositionState.position.zoom > 14f) 45f else 0f)
                                    .build()
                            ),
                            durationMs = 1000
                        )
                    }
                }
            }
        }
    }

    // Check location permission
    LaunchedEffect(Unit) {
        if (checkLocationPermission(context)) {
            locationPermissionGranted = true
            startLocationUpdates(context, fusedLocationClient) { location ->
                currentLocation = location
                Log.d("MapComponent", "Initial location: $location")
                if (!isUserInteracting) {
                    coroutineScope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.fromLatLngZoom(location, 15f)
                            )
                        )
                    }
                }
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Sensor handling for orientation (accelerometer and magnetometer)
    val sensorListener = remember {
        object : SensorEventListener {
            private val accelerometerReading = FloatArray(3)
            private val magnetometerReading = FloatArray(3)
            private val rotationMatrix = FloatArray(9)
            private val orientationAngles = FloatArray(3)
            private var lastUpdateTime = 0L
            private val smoothingFactor = 0.3f

            override fun onSensorChanged(event: SensorEvent) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastUpdateTime < 100) return
                lastUpdateTime = currentTime

                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER ->
                        System.arraycopy(event.values, 0, accelerometerReading, 0, 3)
                    Sensor.TYPE_MAGNETIC_FIELD ->
                        System.arraycopy(event.values, 0, magnetometerReading, 0, 3)
                }

                if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)) {
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                    val newBearing = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                    val normalizedBearing = (newBearing + 360) % 360
                    bearing = bearing + smoothingFactor * (normalizedBearing - bearing)
                    Log.d("MapComponent", "Bearing updated: $bearing")

                    currentLocation?.let { latLng ->
                        if (!isUserInteracting) {
                            val currentZoom = cameraPositionState.position.zoom
                            val tilt = if (currentZoom > 14f) 45f else 0f
                            coroutineScope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newCameraPosition(
                                        CameraPosition.builder()
                                            .target(latLng)
                                            .zoom(currentZoom)
                                            .bearing(bearing)
                                            .tilt(tilt)
                                            .build()
                                    ),
                                    durationMs = 300
                                )
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Light sensor handling
    val lightSensorListener = remember {
        object : SensorEventListener {
            private var lastUpdateTime = 0L
            private val darkThreshold = 10f // Lux threshold for dark mode
            private val lightThreshold = 30f // Lux threshold to go back to light mode

            override fun onSensorChanged(event: SensorEvent) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastUpdateTime < 1000) return // Update every 1 second
                lastUpdateTime = currentTime

                if (event.sensor.type == Sensor.TYPE_LIGHT) {
                    val lux = event.values[0] // Light level in lux
                    Log.d("MapComponent", "Light level: $lux lux")
                    isDarkMode = if(isDarkMode){
                        lux < lightThreshold
                    }else{
                        lux < darkThreshold
                    } // Switch to dark mode if light is low
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Lifecycle handling for sensors
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, sensorManager) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // Register orientation sensors
                    sensorManager.registerListener(
                        sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI
                    )
                    sensorManager.registerListener(
                        sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        SensorManager.SENSOR_DELAY_UI
                    )
                    // Register light sensor
                    sensorManager.registerListener(
                        lightSensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
                Lifecycle.Event.ON_PAUSE -> {
                    sensorManager.unregisterListener(sensorListener)
                    sensorManager.unregisterListener(lightSensorListener)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            sensorManager.unregisterListener(sensorListener)
            sensorManager.unregisterListener(lightSensorListener)
        }
    }

    // Map component
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = false,
            mapType = MapType.NORMAL,
            mapStyleOptions = if (isDarkMode) {
                MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map_style)
            } else {
                null // Default light style
            }
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = locationPermissionGranted,
            zoomControlsEnabled = true,
            compassEnabled = true,
            rotationGesturesEnabled = true,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            tiltGesturesEnabled = true
        ),
        onMyLocationButtonClick = {
            currentLocation?.let { latLng ->
                coroutineScope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition.builder()
                                .target(latLng)
                                .zoom(15f)
                                .bearing(bearing)
                                .tilt(if (cameraPositionState.position.zoom > 14f) 45f else 0f)
                                .build()
                        ),
                        durationMs = 1000
                    )
                }
            }
            true // Consume the click event
        },
        onMapLoaded = {
            isMapLoaded = true
            Log.d("MapComponent", "Map loaded")
        }
    ) {
        if (isMapLoaded && arrowIcon != null && currentLocation != null) {
            Log.d("MapComponent", "Adding marker at $currentLocation with bearing $bearing")
            Marker(
                state = rememberMarkerState(position = currentLocation!!),
                icon = arrowIcon,
                rotation = bearing,
                flat = true,
            )
        }
    }

    // Handle compass click (reset to north)
    LaunchedEffect(cameraPositionState.position.bearing) {
        if (cameraPositionState.position.bearing == 0f && !cameraPositionState.isMoving) {
            bearing = 0f
            Log.d("MapComponent", "Compass reset to north")
        }
    }
}

private fun checkLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun startLocationUpdates(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (LatLng) -> Unit
) {
    try {
        if (checkLocationPermission(context)) {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : com.google.android.gms.location.LocationCallback() {
                    override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                        result.lastLocation?.let {
                            onLocationFetched(LatLng(it.latitude, it.longitude))
                        }
                    }
                },
                Looper.getMainLooper()
            )
        }
    } catch (e: SecurityException) {
        onLocationFetched(LatLng(40.6318, -8.6576)) // Fallback
    }
}