package screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.random.Random

class PlaygroundScreen: Screen {

    @Serializable
    data class portfolio(
        val Symbols: String,
        val sim_no: Int  = 1000
    )

    @Serializable
    data class PortfolioData(
        val annual_return: Double,
        val array_of_allocation: List<Allocation>
    )

    @Serializable
    data class Allocation(
        val Returns: Double,
        @SerialName("Sharpe Ratio") val Sharpe_Ratio: Double,
        val Volatility: Double,
        val Weights: Map<String, Double>
    )

    val lightBlue = Color(0xFF15AEE2)
//    @Composable
//    fun CardsList() {
//    var mResponse by remember {
//        mutableStateOf("")
//    }
//    var dataFromPortfolio by remember {
//        mutableStateOf(PortfolioData(0.0, listOf()))
//    }
//    val selectedValues = remember { mutableStateListOf<String>() }
//    val green = Color(0xFFC2F63F)
//        val gradientColors = listOf(
//            Color(0xFF0F2737), // dark color at the top
//            Color(0xFF06151C),
//            Color(0xFF000000),
//            Color(0xFF000000) // lighter color at the bottom
//        )
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .background(brush = Brush.linearGradient(gradientColors))
//                .padding(top = 16.dp),
//            contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            item {
//                Text(text = "Portfolio", color = Color.White, fontSize = 28.sp, modifier = Modifier.padding(start = 16.dp, top = 16.dp))
//                Text(text = "Playground", color = green, fontSize = 28.sp, modifier = Modifier.padding(start = 16.dp))
//            }
//
//            val cardData = mapOf(
//                "Technology" to mapOf(
//                    "High Cap" to listOf("INFY.NS", "TCS.NS", "HCLTECH.NS", "TECHM.NS", "WIPRO.NS"),
//                    "Mid Cap" to listOf("LTIM.NS", "KPITTECH.NS", "MPHASIS.NS", "LTI.NS", "COFORGE.NS"),
//                    "Low Cap" to listOf("TVSELECT.NS", "VAKRANGEE.NS", "MASTEK.NS", "GTLINFRA.NS", "FSL.NS")
//                ),
//                "Healthcare" to mapOf(
//                    "High Cap" to listOf("SUNPHARMA.NS", "DRREDDY.NS", "DIVISLAB.NS", "LUPIN.NS", "METROPOLIS.NS"),
//                    "Mid Cap" to listOf("AUROPHARMA.NS", "ALKEM.NS", "BIOCON.NS", "TORNTPHARM.NS", "IPCALAB.NS"),
//                    "Low Cap" to listOf("BLISSGVS.NS", "MARKSANS.NS", "KMCSHIL.NS", "SMSLIFE.NS", "INDOCO.NS")
//                ),
//                "Finance" to mapOf(
//                    "High Cap" to listOf("HDFCBANK.NS", "ICICIBANK.NS", "KOTAKBANK.NS", "SBIN.NS", "AXISBANK.NS"),
//                    "Mid Cap" to listOf("BAJFINANCE.NS", "BANDHANBNK.NS", "CHOLAFIN.NS", "L&TFH.NS", "M&MFIN.NS"),
//                    "Low Cap" to listOf("AUBANK.NS", "ABFRL.NS", "BATAINDIA.NS", "BHARTIARTL.NS", "CIPLA.NS")
//                ),
//                "Consumer Goods" to mapOf(
//                    "High Cap" to listOf("HINDUNILVR.NS", "NESTLEIND.NS", "DABUR.NS", "GODREJCP.NS", "MARICO.NS"),
//                    "Mid Cap" to listOf("JUBLFOOD.NS", "UBL.NS", "PIDILITIND.NS", "BRITANNIA.NS", "COLPAL.NS"),
//                    "Low Cap" to listOf("VENKEYS.NS", "VADILALIND.NS", "ZENSARTECH.NS", "VSTIND.NS", "EMAMILTD.NS")
//                ),
//                "Energy" to mapOf(
//                    "High Cap" to listOf("RELIANCE.NS", "ONGC.NS", "IOC.NS", "BPCL.NS", "GAIL.NS"),
//                    "Mid Cap" to listOf("IGL.NS", "GUJGAS.NS", "MGL.NS", "PETRONET.NS", "COALINDIA.NS"),
//                    "Low Cap" to listOf("MRPL.NS", "IOB.NS", "IWEL.NS", "NFL.NS", "HINDPETRO.NS")
//                ),
//                "Industrial" to mapOf(
//                    "High Cap" to listOf("LT.NS", "BAJAJ-AUTO.NS", "TITAN.NS", "TATASTEEL.NS", "JSWSTEEL.NS"),
//                    "Mid Cap" to listOf("SAIL.NS", "VEDL.NS", "ADANIGREEN.NS", "ADANIPORTS.NS", "JINDALSTEL.NS"),
//                    "Low Cap" to listOf("AIAENG.NS", "ATUL.NS", "KSB.NS", "APLAPOLLO.NS", "CROMPTON.NS")
//                )
//            )
//
//            cardData.forEach { (title, subtitles) ->
//                item {
//                    CollapsibleCard(cardTitle = title, subtitles = subtitles, selectedValues = selectedValues)
//                }
//            }
//            item {
//                Text(text = "Selected Stocks: ${selectedValues.toList()}", color = Color.White, modifier = Modifier.padding(start = 16.dp))
//            }
//            item{
//                val coroutineScope = rememberCoroutineScope()
//                    Button(onClick = {
//                        coroutineScope.launch {
//                            val client = HttpClient(CIO) {
//                                install(ContentNegotiation) {
//                                    json()
//                                }
//                                install(HttpTimeout) {
//                                    requestTimeoutMillis = 60000 // Set timeout to 60 seconds
//                                }
//                            }
//                            if(selectedValues.isNotEmpty()){
//                                val requestBody = portfolio(
//                                    selectedValues.joinToString(",")
//                                )
//                                val response = client.post(urlString = "https://assetallocate.onrender.com/portfolio") {
//                                    contentType(ContentType.Application.Json)
//                                    setBody(requestBody)
//                                }
//                                println("Response: " + response.body<String>())
//                                mResponse = response.body()
//                                val jsonParser = Json {
//                                    ignoreUnknownKeys = true
//                                }
//                                dataFromPortfolio = jsonParser.decodeFromString<PortfolioData>(response.body())
//
//                            }
//                            else{
//
//                            }
//                        }
//
//                    }){
//                        Text("Submit")
//                    }
//                var testPieChartData by remember {
//                    mutableStateOf(ArrayList<PieChartData>())
//                }
//                if(dataFromPortfolio.array_of_allocation.isNotEmpty()){
//                    val list = ArrayList<PieChartData>()
//                    for (allocation in dataFromPortfolio.array_of_allocation[0].Weights) {
//                        list.add(
//                            PieChartData(
//                                data = allocation.value.toDouble(),
//                                partName = allocation.key,
//                                color = randomColor()
//                            )
//                        )
//                    }
//                    testPieChartData = list
//                }
//
//                AnimatedVisibility(testPieChartData.isNotEmpty()){
//                    Column {
//                        Text("Annual Returns"+dataFromPortfolio.annual_return.toString(), color = Color.White)
//                        DonutChart(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(280.dp)
//                                .padding(10.dp),
//                            pieChartData = testPieChartData,
//                            centerTitle = "Stocks",
//                            centerTitleStyle = TextStyle(color = Color.White),
//                            descriptionStyle = TextStyle(color = Color.White),
//                            textRatioStyle = TextStyle(color = Color.White, fontSize = 12.sp),
//                            outerCircularColor = Color.White,
//                            innerCircularColor = Color.White,
//                            ratioLineColor = Color.White,
//                            legendPosition = LegendPosition.BOTTOM,
//                        )
//
//                    }
//                }
//            }
//            item {
//                Spacer(Modifier.height(120.dp))
//            }
//        }
//        if(mResponse.isNotEmpty()){
//            println(mResponse)
//            mResponse = ""
//        }
//    }



    @Composable
    fun CardsList() {
        var mResponse by remember { mutableStateOf("") }
        var dataFromPortfolio by remember { mutableStateOf(PortfolioData(0.0, listOf())) }
        val selectedValues = remember { mutableStateListOf<String>() }
        val gradientColors = listOf(
            Color(0xFF0F2737),
            Color(0xFF06151C),
            Color(0xFF000000),
            Color(0xFF000000)
        )

        val testPieChartData = remember { mutableStateOf(emptyList<PieChartData>()) }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(brush = Brush.linearGradient(gradientColors))
                .padding(top = 16.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(text = "Portfolio", color = Color.White, fontSize = 28.sp, modifier = Modifier.padding(start = 16.dp, top = 16.dp))
                Text(text = "Playground", color = Color(0xFFC2F63F), fontSize = 28.sp, modifier = Modifier.padding(start = 16.dp))
            }

            val cardData = mapOf(
                "Technology" to mapOf(
                    "High Cap" to listOf("INFY.NS", "TCS.NS", "HCLTECH.NS", "TECHM.NS", "WIPRO.NS"),
                    "Mid Cap" to listOf("LTIM.NS", "KPITTECH.NS", "MPHASIS.NS", "LTI.NS", "COFORGE.NS"),
                    "Low Cap" to listOf("TVSELECT.NS", "VAKRANGEE.NS", "MASTEK.NS", "GTLINFRA.NS", "FSL.NS")
                ),
                "Healthcare" to mapOf(
                    "High Cap" to listOf("SUNPHARMA.NS", "DRREDDY.NS", "DIVISLAB.NS", "LUPIN.NS", "METROPOLIS.NS"),
                    "Mid Cap" to listOf("AUROPHARMA.NS", "ALKEM.NS", "BIOCON.NS", "TORNTPHARM.NS", "IPCALAB.NS"),
                    "Low Cap" to listOf("BLISSGVS.NS", "MARKSANS.NS", "KMCSHIL.NS", "SMSLIFE.NS", "INDOCO.NS")
                ),
                "Finance" to mapOf(
                    "High Cap" to listOf("HDFCBANK.NS", "ICICIBANK.NS", "KOTAKBANK.NS", "SBIN.NS", "AXISBANK.NS"),
                    "Mid Cap" to listOf("BAJFINANCE.NS", "BANDHANBNK.NS", "CHOLAFIN.NS", "L&TFH.NS", "M&MFIN.NS"),
                    "Low Cap" to listOf("AUBANK.NS", "ABFRL.NS", "BATAINDIA.NS", "BHARTIARTL.NS", "CIPLA.NS")
                ),
                "Consumer Goods" to mapOf(
                    "High Cap" to listOf("HINDUNILVR.NS", "NESTLEIND.NS", "DABUR.NS", "GODREJCP.NS", "MARICO.NS"),
                    "Mid Cap" to listOf("JUBLFOOD.NS", "UBL.NS", "PIDILITIND.NS", "BRITANNIA.NS", "COLPAL.NS"),
                    "Low Cap" to listOf("VENKEYS.NS", "VADILALIND.NS", "ZENSARTECH.NS", "VSTIND.NS", "EMAMILTD.NS")
                ),
                "Energy" to mapOf(
                    "High Cap" to listOf("RELIANCE.NS", "ONGC.NS", "IOC.NS", "BPCL.NS", "GAIL.NS"),
                    "Mid Cap" to listOf("IGL.NS", "GUJGAS.NS", "MGL.NS", "PETRONET.NS", "COALINDIA.NS"),
                    "Low Cap" to listOf("MRPL.NS", "IOB.NS", "IWEL.NS", "NFL.NS", "HINDPETRO.NS")
                ),
                "Industrial" to mapOf(
                    "High Cap" to listOf("LT.NS", "BAJAJ-AUTO.NS", "TITAN.NS", "TATASTEEL.NS", "JSWSTEEL.NS"),
                    "Mid Cap" to listOf("SAIL.NS", "VEDL.NS", "ADANIGREEN.NS", "ADANIPORTS.NS", "JINDALSTEL.NS"),
                    "Low Cap" to listOf("AIAENG.NS", "ATUL.NS", "KSB.NS", "APLAPOLLO.NS", "CROMPTON.NS")
                )
            )

            cardData.forEach { (title, subtitles) ->
                item {
                    CollapsibleCard(cardTitle = title, subtitles = subtitles, selectedValues = selectedValues)
                }
            }
            item {
                Text(text = "Selected Stocks: ${selectedValues.toList()}", color = Color.White, modifier = Modifier.padding(start = 16.dp))
            }
            item {
                val coroutineScope = rememberCoroutineScope()
                val green = Color(0xFFC2F63F)
                Button(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = ButtonDefaults
                        .buttonColors(
                            containerColor = green,
                            contentColor = Color.Black
                        ),
                    onClick = {
                    coroutineScope.launch {
                        val client = HttpClient(CIO) {
                            install(ContentNegotiation) {
                                json()
                            }
                            install(HttpTimeout) {
                                requestTimeoutMillis = 60000 // Set timeout to 60 seconds
                            }
                        }
                        if (selectedValues.isNotEmpty()) {
                            val requestBody = portfolio(selectedValues.joinToString(","))
                            val response = client.post(urlString = "https://assetallocate.onrender.com/portfolio") {
                                contentType(ContentType.Application.Json)
                                setBody(requestBody)
                            }
                            println("Response: " + response.body<String>())
                            mResponse = response.body()
                            val jsonParser = Json { ignoreUnknownKeys = true }
                            dataFromPortfolio = jsonParser.decodeFromString<PortfolioData>(response.body())
                        } else {
                            // Handle empty selection case
                        }
                    }
                }) {
                    Text("Submit")
                }

                if (dataFromPortfolio.array_of_allocation.isNotEmpty()) {
                    LaunchedEffect(dataFromPortfolio) {
                        val list = ArrayList<PieChartData>()
                        for (allocation in dataFromPortfolio.array_of_allocation[0].Weights) {
                            list.add(
                                PieChartData(
                                    data = allocation.value.toDouble(),
                                    partName = allocation.key,
                                    color = randomColor()
                                )
                            )
                        }
                        testPieChartData.value = list
                    }
                }

                AnimatedVisibility(testPieChartData.value.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        DonutChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(10.dp),
                            pieChartData = testPieChartData.value,
                            centerTitle = "Stocks",
                            centerTitleStyle = TextStyle(color = Color.White),
                            descriptionStyle = TextStyle(color = Color.White),
                            textRatioStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                            outerCircularColor = Color.White,
                            innerCircularColor = Color.White,
                            ratioLineColor = Color.White,
                            legendPosition = LegendPosition.BOTTOM,
                        )
                        Text("Annual Returns: ${dataFromPortfolio.annual_return}", color = Color.White)
                        Text("Volatility: ${dataFromPortfolio.array_of_allocation[0].Volatility}", color = Color.White)
                        Text("Sharpe Ratio: ${dataFromPortfolio.array_of_allocation[0].Sharpe_Ratio}", color = Color.White)
                    }
                }
            }
            item {
                Spacer(Modifier.height(120.dp))
            }
        }
        if (mResponse.isNotEmpty()) {
            println(mResponse)
            mResponse = ""
        }
    }

    @Composable
    fun CollapsibleCard(
        cardTitle: String,
        subtitles: Map<String, List<String>>,
        selectedValues: MutableList<String>
    ) {
        val green = Color(0xFFC2F63F)
        var isExpanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { isExpanded = !isExpanded },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, Color(0xFF15AEE2))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(cardTitle, fontSize = 20.sp, color = green)

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = expandVertically(animationSpec = spring()),
                    exit = shrinkVertically(animationSpec = spring())
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            subtitles.forEach { (subtitle, checkboxes) ->
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(subtitle, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally), color = Color.White)
                                    checkboxes.forEach { checkboxLabel ->
                                        val isChecked = selectedValues.contains(checkboxLabel)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = { isSelected ->
                                                    if (isSelected) {
                                                        selectedValues.add(checkboxLabel)
                                                    } else {
                                                        selectedValues.remove(checkboxLabel)
                                                    }
                                                },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = green,
                                                    uncheckedColor = Color.White,
                                                    checkmarkColor = Color.Black
                                                )
                                            )
                                            Text(checkboxLabel, color = lightBlue)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun Content() {
        CardsList()
    }

    fun randomColor(): Color {
        return Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1.0f
        )
    }

}