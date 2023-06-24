package com.example.travelbook.signup.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.travelbook.R

@Composable
fun SignUpView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
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
                    modifier = Modifier
                    .size(16.dp)
                )
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.logo_text),
                    contentDescription = "TravelBook",
                    modifier = Modifier.height(41.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Nav to home page*/},
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(47.dp),
                    ) {
                        Row {
                            Image(
                                bitmap = ImageBitmap.imageResource(id = R.drawable.google_icon),
                                contentDescription = "Google Icon",
                                modifier = Modifier.size(37.dp)
                            )
                        }
                        Text(text = "Sign in with Google")
                    }
                    Spacer(
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Text(
                        "Already have an account? Sign in"
                    )
                }
            }
        }
    }
}