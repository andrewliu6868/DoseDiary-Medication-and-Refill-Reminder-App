package com.example.dosediary.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dosediary.R
import androidx.navigation.NavController
import com.example.dosediary.components.CustomTopAppBar
import com.example.dosediary.ui.theme.ContainerBackground
import com.example.dosediary.ui.theme.Primary
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.viewmodel.MedRefillDetailViewModel

@Composable
fun MedicationRefillDetailScreen(navController: NavController, medRefillDetailViewModel: MedRefillDetailViewModel, medicationId: Int){
    LaunchedEffect(medicationId) {
        medRefillDetailViewModel.fetchMedById(medicationId)
    }
    val state by medRefillDetailViewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                header = "Medication Refill Detail",
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = "App Icon"
            )
        }
    ){padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp)

        ) {
            state.medRefillsMedication.let { medication ->
                MedicationRefillDetail(medication)
            }
        }
    }

}

@Composable
fun MedicationRefillDetail(medication: Medication) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = medication.medicationName + ": " + 0 + " pills left", // fix it later
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MedicationRefillGoogleMaps()
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Directions,
                        tint = Color.White,
                        contentDescription = "Direction"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Direction")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        tint = Color.White,
                        contentDescription = "Call"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Call")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicText(
                    text = "Refill Information:",
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp)
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Next Refill Date: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Name: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Contact: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Pharmacy Address:")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicText(
                    text = "More information about the medication:",
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                )

//                Text(
//                    buildAnnotatedString {
//                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
//                            append("Dosage: ")
//                        }
//                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
//                            append(medication.dosage.toString())
//                        }
//                    }
//                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Quantity: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append("Notes: ")
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )

            }

        }
    }
}
@Composable
fun MedRefillGoogleMaps(location: LatLng) {
    val context = LocalContext.current

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .height(200.dp),
        cameraPositionState = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(location,12f)
        }
    ){
        Marker(
            state = MarkerState(position = location),
            title = "Current Location",
            snippet = "Marker in ${location.latitude}, ${location.longitude}"
        )
    }
}
@Composable
fun MedicationRefillGoogleMaps() {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
        /*Marker(
            state = MarkerState(position = singapore),
            title = "Singapore",
            snippet = "Marker in Singapore"
        )*/
    }
}
