package com.example.google_firebase.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.google_firebase.R
import com.example.google_firebase.data.Response
import com.example.google_firebase.ui.theme.MyButton
import com.example.google_firebase.ui.theme.MyColor
import com.example.google_firebase.ui.theme.MyTextField

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory(LocalContext.current))
) {
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Microsoft OneNote",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = MyColor,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Welcome to OneNote",
                        fontSize = 14.sp,
                        color = MyColor,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            MyTextField(value = "", label = "Email Address", onValueChange = {})
            Spacer(modifier = Modifier.height(16.dp))
            MyTextField(value = "", label = "Password", onValueChange = {})

            Spacer(modifier = Modifier.height(32.dp))

            MyButton(label = "LOGIN", modifier = Modifier, onClick = {
            })

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Forgot Password ?",
                color = MyColor,
                fontWeight = FontWeight.W600
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (authState) {
                is Response.Loading -> {
                    GoogleSignInButton(
                        onClick = { viewModel.signInWithGoogle() },
                        isEnable = false
                    )
                }
                is Response.Success -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                is Response.Error -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Login Failed: ${(authState as Response.Error).errorMessage}")
                    }
                    GoogleSignInButton(
                        onClick = { viewModel.signInWithGoogle() },
                        isEnable = true
                    )
                }
                else -> {
                    GoogleSignInButton(
                        onClick = { viewModel.signInWithGoogle() },
                        isEnable = true
                    )
                }
            }
        }
    }
}

@Composable
fun GoogleSignInButton(modifier: Modifier = Modifier, onClick: () -> Unit, isEnable: Boolean) {
    Button(
        onClick = {
            onClick()
        },
        enabled = isEnable,
        modifier = modifier.padding(16.dp)

    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign In with Google")
            Spacer(modifier = Modifier.width(8.dp))

            if(!isEnable) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }


    }

}