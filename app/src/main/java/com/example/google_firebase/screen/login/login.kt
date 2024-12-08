package com.example.google_firebase.screen.login

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.google_firebase.R
import com.example.google_firebase.ui.theme.MyButton
import com.example.google_firebase.ui.theme.MyColor
import com.example.google_firebase.ui.theme.MyTextField

@Composable
fun LoginScreen(
    onSignInClick: () -> Unit,
) {
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
            MyButton(label = "LOGIN", modifier = Modifier, onClick = {})
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Forgot Password ?",
                color = MyColor,
                fontWeight = FontWeight.W600
            )
            Spacer(modifier = Modifier.height(32.dp))
            MyButton(label = "Sign In with Google", modifier = Modifier, onClick = onSignInClick)
        }
    }
}