package com.example.travelbook.signIn.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelbook.signIn.viewModels.NewSignInViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.travelbook.R


@Composable
fun NewSignInView(viewModel: NewSignInViewModel) {
    val context = LocalContext.current

    val authResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val response = IdpResponse.fromResultIntent(result.data)
        if (result.resultCode == Activity.RESULT_OK) {
            // Sign-in successful, handle the signed-in user
            val user = FirebaseAuth.getInstance().currentUser
            viewModel.handleAuthResult(user)
        } else {
            // Sign-in failed, handle the failure
            viewModel.handleSignInFailure(response?.error?.message ?: "Sign-in failed")
        }
        viewModel.setLoadingState(false)
    }

    val providers = listOf(
        AuthUI.IdpConfig.EmailBuilder().build(), // Email authentication
        AuthUI.IdpConfig.GoogleBuilder().build() // Google authentication
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                ImageVector.vectorResource(id = R.drawable.logo),
                contentDescription = "TravelBook Icon",
                modifier = Modifier.height(41.dp)
            )
            Spacer(
                modifier = Modifier.size(16.dp)
            )
            Icon(
                ImageVector.vectorResource(id = R.drawable.logo_text),
                contentDescription = "TravelBook Icon",
                modifier = Modifier.height(41.dp)
            )
        }

        Text(
            text = "Welcome to TravelBook.\nYour one stop app for storing all the memories created on your trip",
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp  // Adjust this for the desired size
            )
        )
//        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                val signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build()
                authResultLauncher.launch(signInIntent)
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Sign In")
        }
    }

    val loadingState by viewModel.loadingState.collectAsState()
    if (loadingState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }

    val errorMessage by viewModel.errorMessage.collectAsState()
    if (errorMessage.isNotEmpty()) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}
