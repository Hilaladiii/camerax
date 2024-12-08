package com.example.google_firebase.ui.create


import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.google_firebase.data.Response
import com.example.google_firebase.utils.CameraView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateScreen(
    viewModel: CreateViewModel = viewModel(factory = CreateViewModel.Factory),
    navigateBack: () -> Unit
) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val addHeroState by viewModel.addHeroState.collectAsState()

    // Camera state
    val shouldShowCamera = remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    if (shouldShowCamera.value) {
        CameraView(
            outputDirectory = context.getExternalFilesDir(null)?.absolutePath ?: "",
            onImageCaptured = { uri ->
                imageUri.value = uri
                shouldShowCamera.value = false
            },
            onError = { exception ->
                Toast.makeText(
                    context, "Error capturing image: ${exception.message}",
                    Toast.LENGTH_LONG
                ).show()
                shouldShowCamera.value = false
            }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Hero Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (permissionState.status.isGranted) {
                        shouldShowCamera.value = true
                    } else {
                        permissionState.launchPermissionRequest()
                    }
                }
            ) {
                Text("Take Picture")
            }

            imageUri.value?.let { uri ->
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    imageUri.value?.let { uri ->
                        viewModel.addHero(name.value, description.value, uri)
                    }
                },
                enabled = name.value.isNotEmpty() && description.value.isNotEmpty() && imageUri.value != null && addHeroState !is Response.Loading
            ) {
                if (addHeroState is Response.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Add Hero")
                }
            }
        }
        HandleAddHeroState(addHeroState, navigateBack, context)
    }
}

@Composable
fun HandleAddHeroState(addHeroState: Response<Unit>, onNavigateBack: () -> Unit, context: Context) {
    LaunchedEffect(addHeroState) {
        when (addHeroState) {
            is Response.Success -> {
                Toast.makeText(context, "Hero added successfully!", Toast.LENGTH_SHORT).show()
                onNavigateBack()
            }

            is Response.Error -> {
                Toast.makeText(
                    context,
                    (addHeroState as Response.Error).errorMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> Unit
        }
    }
}


@Preview(showBackground = true, showSystemUi = true, device = Devices.PIXEL_3A)
@Composable
fun CreateScreenPreview() {
    CreateScreen(navigateBack = {})
}