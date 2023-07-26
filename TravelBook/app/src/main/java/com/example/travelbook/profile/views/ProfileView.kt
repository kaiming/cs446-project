package com.example.travelbook.profile.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.profile.viewModels.ProfileViewModel

@Composable
fun ProfileView(
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        viewModel.getUser()?.name?.let {
            Text(
                text = "Welcome, $it!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Icon(Icons.Filled.AccountCircle, contentDescription = "Account Icon", modifier = Modifier.size(64.dp))

        viewModel.getUser()?.email?.let {
            Text(
                text = "$it",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Button(
            onClick = {
                val isSuccessful = viewModel.onSignOutClicked()
                if (isSuccessful) {
                    Toast.makeText(context, "Sign Out successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Sign Out failed!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Sign Out")
        }
    }
}