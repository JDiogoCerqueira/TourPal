package com.tourpal.ui.components

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.*
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.tourpal.R
import com.tourpal.data.model.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@Composable
fun MapComponent(
    modifier: Modifier = Modifier,
    destinations: List<Destination> = emptyList(),
) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val coroutineScope = rememberCoroutineScope()
    var arrowIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var bearing by remember { mutableFloatStateOf(0f) }
    var locationPermissionGranted by remember { mutableStateOf(false) }
    var isMapLoaded by remember { mutableStateOf(false) }
    var customMarker by remember { mutableStateOf<BitmapDescriptor?>(null) }
    var routeData by remember { mutableStateOf<List<RouteSegment>>(emptyList()) }
    
    // Add state for dark mode
    var isDarkMode by remember { mutableStateOf(false) }
    // Light threshold - adjust as needed based on testing
    val LIGHT_THRESHOLD = 30f
    
    // Load dark map style
    val darkMapStyle by remember {
        mutableStateOf(MapStyleOptions.loadRawResourceStyle(context, R.raw.dark_map_style))
    }
    
    // Load custom marker icon
    LaunchedEffect(Unit) {
        customMarker = createCustomMarker(context)
    }

    // Default camera position
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(40.6318, -8.6576), 15f)
    }

    // Load arrow icon
    LaunchedEffect(Unit) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow)
        arrowIcon = drawable?.toBitmap(64, 64)?.let { BitmapDescriptorFactory.fromBitmap(it) }
    }

    // Sensor handling
    val sensorListener = remember {
        object : SensorEventListener {
            private val accelerometerReading = FloatArray(3)
            private val magnetometerReading = FloatArray(3)
            private val rotationMatrix = FloatArray(9)
            private val orientationAngles = FloatArray(3)

            override fun onSensorChanged(event: SensorEvent?) {
                when (event?.sensor?.type) {
                    Sensor.TYPE_ACCELEROMETER -> System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                    Sensor.TYPE_MAGNETIC_FIELD -> System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                    Sensor.TYPE_LIGHT -> {
                        // Update dark mode based on ambient light level
                        val lightLevel = event.values[0]
                        isDarkMode = lightLevel < LIGHT_THRESHOLD
                        Log.d("MapComponent", "Light sensor: $lightLevel lux, Dark mode: $isDarkMode")
                    }
                }
                SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                bearing = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    // Lifecycle handling for sensors
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, sensorManager) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
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
                        sensorListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                        SensorManager.SENSOR_DELAY_UI
                    )
                }
                Lifecycle.Event.ON_PAUSE -> sensorManager.unregisterListener(sensorListener)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            sensorManager.unregisterListener(sensorListener)
        }
    }

    // Calculate walking routes between destinations
    LaunchedEffect(destinations) {
        if (destinations.size >= 2) {
            coroutineScope.launch {
                routeData = calculateWalkingRoutes(destinations, context)
            }
        }
    }

    // Adjust camera to fit all destinations
    LaunchedEffect(isMapLoaded, destinations) {
        if (isMapLoaded && destinations.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            destinations.forEach { dest ->
                dest.coordinates?.let { geoPoint ->
                    boundsBuilder.include(LatLng(geoPoint.latitude, geoPoint.longitude))
                }
            }
            val bounds = boundsBuilder.build()
            coroutineScope.launch {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngBounds(bounds, 100),
                    1000
                )
            }
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = locationPermissionGranted,
            mapType = MapType.NORMAL,
            mapStyleOptions = if (isDarkMode) darkMapStyle else null
        ),
        uiSettings = MapUiSettings(
            myLocationButtonEnabled = true,
            zoomControlsEnabled = true,
            compassEnabled = true,
            rotationGesturesEnabled = true,
            scrollGesturesEnabled = true,
            zoomGesturesEnabled = true,
            tiltGesturesEnabled = true
        ),
        onMapLoaded = {
            isMapLoaded = true
        }
    )  {
        if (isMapLoaded && destinations.isNotEmpty() && customMarker != null) {
            // Add markers for each destination
            destinations.forEach { destination ->
                destination.coordinates?.let { geoPoint ->
                    val position = LatLng(geoPoint.latitude, geoPoint.longitude)
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = destination.name,
                        snippet = destination.description,
                        icon = customMarker
                    )
                }
            }
            
            // Draw walking routes
            routeData.forEach { segment ->
                // Draw the route polyline
                Polyline(
                    points = segment.path,
                    color = Color.Blue,
                    width = 25f,
                    pattern = listOf(Dot(), Gap(15f)),
                )
                
                // Add time marker at the midpoint
                val midPoint = if (segment.path.size > 1) {
                    segment.path[segment.path.size / 2]
                } else null
                
                midPoint?.let { point ->
                    val markerState = rememberMarkerState(position = point)
                    
                    // Make the info window visible by default
                    LaunchedEffect(markerState) {
                        markerState.showInfoWindow()
                    }
                    
                    MarkerInfoWindow(
                        state = markerState,
                        icon = BitmapDescriptorFactory.fromBitmap(
                            createBitmap(1, 1).apply {
                                eraseColor(Color.Transparent.toArgb())
                            }
                        ),
                        anchor = Offset(0.5f, 0.5f)
                    ) {
                        androidx.compose.material3.Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray),
                            shadowElevation = 4.dp
                        ) {
                            androidx.compose.material3.Text(
                                text = "Walking time: ${segment.duration}",
                                modifier = Modifier.padding(8.dp),
                                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }

        if (arrowIcon != null) {
            Marker(
                state = rememberMarkerState(position = cameraPositionState.position.target),
                icon = arrowIcon,
                rotation = bearing,
                flat = true,
            )
        }
    }
}

// Data class to store route segment information    
data class RouteSegment(
    val path: List<LatLng>,
    val duration: String,
)

// Function to calculate walking routes between destinations
private suspend fun calculateWalkingRoutes(
    destinations: List<Destination>,
    context: Context,
): List<RouteSegment> = withContext(Dispatchers.IO) {
    val routeSegments = mutableListOf<RouteSegment>()
    
    try {
        // Get API key from AndroidManifest.xml
        val apiKey = getGoogleMapsApiKey(context)
        if (apiKey.isEmpty()) {
            Log.e("MapComponent", "Google Maps API key not found")
            return@withContext emptyList<RouteSegment>()
        }
        
        // Create GeoApiContext with the API key
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .connectTimeout(2, TimeUnit.SECONDS)
            .readTimeout(2, TimeUnit.SECONDS)
            .build()
            
        // Calculate routes between consecutive destinations
        for (i in 0 until destinations.size - 1) {
            val origin = destinations[i].coordinates
            val destination = destinations[i + 1].coordinates
            
            if (origin != null && destination != null) {
                val originLatLng = com.google.maps.model.LatLng(origin.latitude, origin.longitude)
                val destLatLng = com.google.maps.model.LatLng(destination.latitude, destination.longitude)
                
                try {
                    // Request directions
                    val result = DirectionsApi.newRequest(geoApiContext)
                        .mode(TravelMode.WALKING)
                        .origin(originLatLng)
                        .destination(destLatLng)
                        .await()
                    
                    if (result.routes.isNotEmpty() && result.routes[0].legs.isNotEmpty()) {
                        val leg = result.routes[0].legs[0]
                        val duration = leg.duration.humanReadable
                        
                        // Decode the polyline path
                        val encodedPath = result.routes[0].overviewPolyline.encodedPath
                        val decodedPath = PolyUtil.decode(encodedPath)
                        val path = decodedPath.map { LatLng(it.latitude, it.longitude) }
                        
                        routeSegments.add(RouteSegment(path, duration))
                        Log.d("MapComponent", "Added route from ${destinations[i].name} to ${destinations[i+1].name}, duration: $duration")
                    } else {
                        Log.e("MapComponent", "No routes found between destinations")
                    }
                } catch (e: Exception) {
                    Log.e("MapComponent", "Error fetching route: ${e.message}", e)
                }
            }
        }
        
        geoApiContext.shutdown()
    } catch (e: Exception) {
        Log.e("MapComponent", "Error calculating routes: ${e.message}", e)
    }
    
    routeSegments
}

// Function to retrieve Google Maps API key from AndroidManifest.xml
private fun getGoogleMapsApiKey(context: Context): String {
    try {
        val applicationInfo = context.packageManager.getApplicationInfo(
            context.packageName, 
            PackageManager.GET_META_DATA
        )
        
        return applicationInfo.metaData.getString("com.google.android.geo.API_KEY") ?: ""
    } catch (e: Exception) {
        Log.e("MapComponent", "Error retrieving Google Maps API key: ${e.message}", e)
        return ""
    }
}

// Function to create a custom marker from a drawable resource
private fun createCustomMarker(context: Context): BitmapDescriptor {
    val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.ic_custom_marker, null)
    val bitmap = drawable?.toBitmap(158, 158) // Adjust size as needed
    return BitmapDescriptorFactory.fromBitmap(bitmap ?: createBitmap(182, 182))
}