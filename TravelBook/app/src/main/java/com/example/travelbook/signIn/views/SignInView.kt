package com.example.travelbook.signIn.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelbook.R
import com.example.travelbook.signIn.viewModels.SignInViewModel

@Composable
fun SignInView(
    viewModel: SignInViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
                    modifier = Modifier.size(16.dp)
                )
                Icon(
                    ImageVector.vectorResource(id = R.drawable.logo_text),
                    contentDescription = "TravelBook Icon",
                    modifier = Modifier.height(41.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(
                onClick = { viewModel.onSignInClicked() },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(47.dp),
                modifier = Modifier.width(230.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        ImageVector.vectorResource(id = R.drawable.google_icon),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Text(
                        text = "Sign in with Google",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .size(24.dp)
            )
            OutlinedButton(
                onClick = { viewModel.onSignInClicked()  },
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(47.dp),
                modifier = Modifier.width(230.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        ImageVector.vectorResource(id = R.drawable.baseline_email_24),
                        contentDescription = "Email Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                    Text(
                        text = "Sign in with Email",
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .size(24.dp)
            )
            TextButton(onClick = { viewModel.onSignInClicked() }) {
                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Sign in")
                        }
                    }
                )
            }
        }
    }
}