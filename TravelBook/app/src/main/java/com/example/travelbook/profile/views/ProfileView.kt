package com.example.travelbook.profile.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelbook.profile.viewModels.ProfileViewModel

@Composable
fun ProfileView(
    viewModel: ProfileViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        viewModel.getUser()?.name?.let {
            Text(
                text = "Hi $it",
                fontSize = 20.sp
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