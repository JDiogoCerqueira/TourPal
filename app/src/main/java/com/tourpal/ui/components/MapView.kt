package com.tourpal.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory


import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.tourpal.ui.theme.TourPalTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    location: LatLng = LatLng(40.6273, -8.7488), // Aveiro, Portugal
    zoomLevel: Float = 12f,
    markerTitle: String = "Aveiro"
) {

    val markerState = remember {
        mutableStateOf(MarkerState(position = location))
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, zoomLevel)
    }

    GoogleMap(
        modifier = modifier.fillMaxWidth()
            .height(200.dp) ,
        cameraPositionState = cameraPositionState
    ) {
        Marker(

            state = markerState.value,
            title = markerTitle
        )
    }
}

@Preview
@Composable
fun MapViewPreview() {
    TourPalTheme {
        MapView(location = LatLng(51.5074, -0.1278), markerTitle = "London")
    }
}
