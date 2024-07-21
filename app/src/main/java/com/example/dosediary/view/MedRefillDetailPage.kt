package com.example.dosediary.view


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.dosediary.model.entity.Medication
import com.example.dosediary.state.MedRefillState
import com.example.dosediary.state.MedicationWithNextRefillDate
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MedicationRefillDetailPage(
    navController: NavController,
    medRefillState: MedRefillState,
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                header = stringResource(R.string.medication_refill_detail),
                showNavigationIcon = true,
                navController = navController,
                imageResId = R.drawable.icon,  // Customizable icon
                imageDescription = stringResource(R.string.app_icon)
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
            MedicationRefillDetail(medRefillState.selectedRefillDetail)
        }
    }

}

@Composable
fun MedicationRefillDetail(medicationWithNextRefillDate: MedicationWithNextRefillDate) {
    Card(
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = ContainerBackground),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(8.dp))
            BasicText(
                text = "${medicationWithNextRefillDate.medication.medicationName}:", // fix it later
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MedicationRefillGoogleMaps(medicationWithNextRefillDate.medication)
            Spacer(modifier = Modifier.height(8.dp))
            Row() {
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Directions,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.direction)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.direction))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier.height(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        tint = Color.White,
                        contentDescription = stringResource(R.string.call)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(R.string.call))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                BasicText(
                    text = stringResource(R.string.refill_information),
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Medium, fontSize = 14.sp)
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append(stringResource(R.string.next_refill_date))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationWithNextRefillDate.nextRefillDate.toString()) // fix later
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append(stringResource(R.string.pharmacy_name))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append(stringResource(R.string.pharmacy_contact))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append("") // fix later
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append(stringResource(R.string.pharmacy_address))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationWithNextRefillDate.medication.address) // fix later
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
                    text = stringResource(R.string.more_information),
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
                            append(stringResource(R.string.quantity))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationWithNextRefillDate.medication.frequency) // fix later
                        }
                    }
                )

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp)) {
                            append(stringResource(R.string.notes))
                        }
                        withStyle(style = SpanStyle(fontSize = 12.sp)) {
                            append(medicationWithNextRefillDate.medication.note) // fix later
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
            state = rememberMarkerState(position = location),
            title = "Current Location",
            snippet = "Marker in ${location.latitude}, ${location.longitude}"
        )
    }
}
@SuppressLint("UnrememberedMutableState")
@Composable
fun MedicationRefillGoogleMaps(medication: Medication) {
//    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(medication.addressLatLng, 10f)
    }
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = medication.addressLatLng),
            title = "Pharmacy Name",
            snippet = "Pharmacy Address"
        )
    }
}
