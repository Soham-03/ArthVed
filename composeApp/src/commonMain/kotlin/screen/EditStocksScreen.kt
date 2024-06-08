package screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class EditStocksScreen(
    val risk: Int,
    val periodOfInvestment: Int,
    val ROI: Float,
    val principalAmount: Float,
    val index: Int
): Screen {

    @Serializable
    data class ResultsContainer(
        val results: List<Result>
    )

    @Serializable
    data class Result(
        @SerialName("Annual Return") val expectedAnnualReturn: Double,
        @SerialName("Array_of_allocations") val arrayOfAllocation: List<ArrayOfAllocation>,
        @SerialName("Sharpe Ratio") val sharpeRatio: Double,
        val Symbols: String,
        val Volatility: Double,
        val Weights: WeightEachStock
    )

    @Serializable
    @SerialName("Array_of_allocations")
    data class ArrayOfAllocation(
//        val Portfolio: Double,
        val Returns: Double,
        @SerialName("Sharpe Ratio") val Sharpe_Ratio: Double,
        val Volatility: Double,
        val Weights: Map<String, Double>
    )

    @Serializable
    data class WeightEachStock(
        val Weights: Map<String, Double>
    )

    @Composable
    override fun Content() {
        val green = Color(0xFFC2F63F)
        val blue = Color(0xFF0F2737)
        val lightBlue = Color(0xFF15AEE2)
        val gradientColors = listOf(
            Color(0xFF0F2737), // dark color at the top
            Color(0xFF06151C),
            Color(0xFF000000),
            Color(0xFF000000)// lighter color at the bottom
        )
        var riskInText by remember {
            mutableStateOf("")
        }
        when(index){
            0 -> {
                riskInText = "Low"
            }
            1 -> {
                riskInText = "High"
            }
            2 -> {
                riskInText = "Medium"
            }
        }
        var resultContainer by remember {
            mutableStateOf(ResultsContainer(emptyList()))
        }
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 60000 // Set timeout to 60 seconds
            }
        }
        val requestBody = ClustersScreen.InvestmentRequest(
            lifestyle_risk = risk,
            expected_annual_roi = ROI.toDouble(),
            principal_amount = principalAmount.toInt(),
            current_age = SharedObj.currentAge,
        )
        var testPieChartData by remember {
            mutableStateOf(ArrayList<PieChartData>())
        }

        var sizeOfAllocations by remember {
            mutableStateOf(0)
        }
        var selectedAllocationIndex by remember {
            mutableStateOf(0)
        }
        var allocations by remember {
            mutableStateOf(ArrayList<ArrayOfAllocation>())
        }
        var weight by remember {
            mutableStateOf(HashMap<String, Double>())
        }

        LaunchedEffect(Unit) {
            val response = client.post(urlString = "https://assetallocate.onrender.com/optimize") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            println("Response: " + response.body<String>())
            client.close()
            val res = Json { ignoreUnknownKeys = true }.decodeFromString<ResultsContainer>(response.body())
            resultContainer = res
            println("Portfolio Response: $res")
        }
        Box(){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.linearGradient(gradientColors))
                    .padding(16.dp)
            ){
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Customize Your", color = Color.White, fontSize = 32.sp)
                Text(text = "Stocks", color = green, fontSize = 32.sp)


                val listOfColors = listOf(
                    Color.Red,
                    Color.Blue,
                    Color.Cyan,
                    Color.Magenta,
                    Color.Yellow,
                    green,
                    lightBlue,
                    blue,
                    Color.White,
                    Color(0xFFE04C00),
                )

                var i = 0
                if(resultContainer.results.isNotEmpty()){
                    allocations = resultContainer.results[index].arrayOfAllocation as ArrayList<ArrayOfAllocation>
                    sizeOfAllocations = allocations.size
                    val result = resultContainer.results[index]
                    val weights = result.Weights
                    weight = weights.Weights as HashMap<String, Double>
                    val lst = arrayListOf<PieChartData>()
                    for(w in weight){
                        lst.add(
                            PieChartData(
                                data = w.value.toDouble(),
                                partName = w.key.toString(),
                                color = listOfColors.get(i++),
                            )
                        )
                    }
                    testPieChartData = lst
                    AnimatedVisibility(resultContainer.results.isNotEmpty()){
                        DonutChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(horizontal = 10.dp),
                            pieChartData = testPieChartData,
                            centerTitle = "Stocks",
                            centerTitleStyle = TextStyle(color = Color.White),
                            descriptionStyle = TextStyle(color = Color.White),
                            textRatioStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                            outerCircularColor = Color.White,
                            innerCircularColor = Color.White,
                            ratioLineColor = Color.White,
                            legendPosition = LegendPosition.BOTTOM,
                        )
                    }
//                    Text("Volatility: ${resultContainer.results[selectedAllocationIndex].Volatility}", color = Color.White, fontSize = 18.sp)
//                    Text("Age: ${resultContainer.results[index].Volatility}")
                    var sliderPosition by remember { mutableFloatStateOf(0f) }

                    Button(
                        onClick = {
//                    navigator.push(HomeScreen(risk, periodOfInvestment, ROI, amountInvestedMonthly*periodOfInvestment))
                        },
                        content = {
                            if(allocations.isNotEmpty()){
                                Text("Risk Level: ${sliderPosition.toInt()}")
                                Column {
                                    Slider(
                                        value = sliderPosition,
                                        onValueChange = {
                                            sliderPosition = it
                                            sizeOfAllocations = allocations.size
                                            val list = arrayListOf<PieChartData>()
                                            val iin = if(it.toInt()>=1){it.toInt()-1}else{
                                                it.toInt()
                                            }
                                            selectedAllocationIndex = iin
                                            var colorIndex = 0
                                            for (weigh in allocations[iin].Weights){
                                                list.add(
                                                    PieChartData(
                                                        data = weigh.value.toDouble(),
                                                        color = listOfColors[colorIndex++],
                                                        partName = weigh.key,
                                                    )
                                                )
                                            }
                                            testPieChartData = list
                                        },
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color.Black,
                                            activeTrackColor = lightBlue,
                                            inactiveTrackColor = Color.Black,
                                            inactiveTickColor = Color.White,
                                            activeTickColor = Color.Black,
                                        ),
                                        steps = if(sizeOfAllocations==0){sizeOfAllocations}else{ sizeOfAllocations-1 },
                                        valueRange = 1f..sizeOfAllocations.toFloat()
                                    )
                                }
                            }


                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green,
                            contentColor = Color.Black
                        )
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
//                            .graphicsLayer { alpha = 0.99f }
                            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                            .drawWithContent {
                                val colors = listOf(Color.Transparent, Color.Black, Color.Black)
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(colors),
                                    blendMode = BlendMode.DstIn
                                )
                            }
                    ){
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        if(allocations.isNotEmpty()){
                            for(weight in allocations[selectedAllocationIndex].Weights) {
                                item {
                                    StockSingleRow(key = weight.key, value = weight.value)
                                }
                            }
                        }
                        else{
                            for(weight in weights.Weights) {
                                item {
                                    StockSingleRow(key = weight.key, value = weight.value)
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
            if(resultContainer.results.isEmpty()){

                    CircularProgressIndicator(
                        color = green,
                        strokeWidth = 8.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                    )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )
        }
    }

    @Composable
    fun StockSingleRow(
        key: String,
        value: Double,
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, Color(0xFF15AEE2)),
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                Text(text = key, color = Color.White, fontSize = 20.sp)
                Text(text = ((value*100)/principalAmount).toString()+" %", color =  Color(0xFFC2F63F), fontSize = 20.sp)
            }
        }
    }

}