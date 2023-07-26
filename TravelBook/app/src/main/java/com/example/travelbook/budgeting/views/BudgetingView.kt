package com.example.travelbook.budgeting.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.travelbook.R
import kotlin.math.roundToInt


@Composable
fun BudgetingView(
    viewModel: BudgetingViewModel,
    tripId: String,
    onNavigateToBudgetDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.loadBudgets(tripId)
    val totalTripBudget = viewModel.totalTripBudget.toFloatOrNull() ?: 0f
    val utilizedTripBudget = viewModel.totalEventBudgets.toFloatOrNull() ?: 0f

    PieChartView(totalTripBudget, utilizedTripBudget)

    Spacer(modifier = Modifier.height(32.dp))

    BudgetCard(totalTripBudget, utilizedTripBudget)

}



@Composable
fun BudgetCard(
    totalTripBudget : Float,
    utilizedTripBudget : Float,
) {
    val overBudget = utilizedTripBudget >= totalTripBudget;
    val percentUsed = ((utilizedTripBudget / totalTripBudget) * 100).roundToInt()
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            if (overBudget) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, // Set horizontal alignment to Center
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Warning, contentDescription = "Warning: above budget.")

                    Text(
                        text = "Whoa! You are over budget.",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(16.dp)
                    )
                }

            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center, // Set horizontal alignment to Center
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = "Currently within budget.")

                    Text(
                        text = "Nice. Currently under budget.",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Text(
                text = "You have used $$utilizedTripBudget out of your total budget of ~$$totalTripBudget. That's $percentUsed % of your budget used so far.",
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Edit your expenses below.",
                color = Color.Gray
            )
        }
    }
}