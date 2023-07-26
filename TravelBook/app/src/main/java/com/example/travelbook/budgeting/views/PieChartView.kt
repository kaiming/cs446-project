package com.example.travelbook.budgeting.views

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import com.example.newcanaryproject.PieChartData
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import java.util.*


@Composable
fun PieChartView(
    totalTripBudget: Float,
    utilizedTripBudget: Float,
) {

    val utilizedPercentage : Float;
    var remainingPercentage : Float;

    if (utilizedTripBudget >= totalTripBudget) {
        utilizedPercentage = 100.toFloat();
        remainingPercentage = 0.toFloat();
    } else {
        utilizedPercentage = (utilizedTripBudget / totalTripBudget) * 100;
        remainingPercentage = 100 - utilizedPercentage;
    }


    val pieChartdata = listOf(
        PieChartData("Used", utilizedTripBudget),
        PieChartData("Remaining", totalTripBudget - utilizedTripBudget),
    )

    // Referenced GeeksForGeeks for documentation on this pie chart
    // Column with modifier
    // as max size
    Column(modifier = Modifier.fillMaxSize()) {
        // Column
        // vertical alignment
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Text
            // Font family styling
            Text(
                text = "Budget Breakdown",

                // on below line we are specifying style for our text
                style = TextStyle.Default,

                // on below line we are specifying font family.
                fontFamily = FontFamily.Default,

                // on below line we are specifying font style
                fontStyle = FontStyle.Normal,

                // on below line we are specifying font size.
                fontSize = 20.sp
            )

            // Column
            // Horizontal & Vertical alignment
            // Padding on all sides
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .size(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Cross fade
                // Target state is the pie chart
                // method within "PieChartData"
                Crossfade(targetState = pieChartdata) { pieChartData ->
                    // Android View
                    // for the pie chart.
                    AndroidView(factory = { context ->
                        // Pie Chart
                        // Match to use parent layout
                        PieChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                // on below line we are specifying layout
                                // params as MATCH PARENT for height and width.
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            // Enable description
                            // to pie chart
                            this.description.isEnabled = false

                            // Draw hole false
                            // as we don't need it on our pie chart
                            this.isDrawHoleEnabled = false

                            // Legend enabled
                            // on pie chart
                            this.legend.isEnabled = true

                            // Text styling
                            // for the legend
                            this.legend.textSize = 14F

                            // Specifying alignment styling
                            // for the legend
                            this.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.CENTER

                            // Specifying entry color
                            // as white
                            this.setEntryLabelColor(Color.White.toArgb())
                        }
                    },
                        // Specifying modifier
                        // Padding
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp), update = {
                            // on below line we are calling update pie chart
                            // method and passing pie chart and list of data.
                            updatePieChartWithData(it, pieChartData)
                        })
                }
            }
        }
    }
}

/*
updatePeChartWithData
Given a reference to a pie chart,
as well as a list of pie chart data objects,
this function will update the pie chart using that data.
this functions uses the styling specified for our budgeting
page.
 */
fun updatePieChartWithData(
    // Pie Chart and list of PieChartData
    // parameters
    chart: PieChart,
    data: List<PieChartData>
) {

    // adding colours that are specific to the
    // budgeting page style (red/green)
    val redColor = Color(0xFFF44336)
    val greenColor = Color(0xFF0F9D58)
    // creating an array list
    // for the pie Entry objects
    val entries = ArrayList<PieEntry>()

    // converting inputted data into the data
    // type (entries) that we can feed to the chart
    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.value ?: 0.toFloat(), item.name ?: ""))
    }

    // passing the entries array
    // to the variable ds
    val ds = PieDataSet(entries, "")

    // specify
    // color
    ds.colors = arrayListOf(
        redColor.toArgb(),
        greenColor.toArgb(),
    )
    // specifying position
    // for value
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // specifying position
    // for value withi nthe slice
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // specifying slice
    // space
    ds.sliceSpace = 2f

    // specifying text color
    ds.valueTextColor = Color.White.toArgb()

    // specifying the text size
    // within the values
    ds.valueTextSize = 18f

    // specifying the type face
    // as bold
    ds.valueTypeface = Typeface.DEFAULT_BOLD

    // create the pie data object to be
    // passed to the chart
    val d = PieData(ds)

    // set data on passed chart to our new
    // data object
    chart.data = d

    // invalidate the previous chart
    // to ensure updated data
    chart.invalidate()
}