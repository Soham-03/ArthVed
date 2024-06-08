package screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import tiaahackathon.composeapp.generated.resources.compose_multiplatform
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.model.PieChartData
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import org.jetbrains.compose.resources.painterResource
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.compose_multiplatform
import tiaahackathon.composeapp.generated.resources.stock
import kotlin.coroutines.EmptyCoroutineContext

data class ClustersScreen(val risk:Int, val peroidOfInvestment: Int, val ROI:Float, val principalAmount: Float, val age: Int): Screen {

    @Serializable
    data class InvestmentRequest(
        val current_age:Int,
        val lifestyle_risk: Int,
        val expected_annual_roi: Double,
        val principal_amount: Int,
        val risk: Int = 1,
        val sim_no: Int = 1000,
    )
    @Serializable
    data class Cluster(
        val Overage_Flag: Int,
        val Symbols: String,
        val Underage_Flag: Int,
        val Weights: Double
    )

    @Serializable
    data class Clusters(
        val clusters: List<Cluster>
    )
    @OptIn(InternalAPI::class)
    @Composable
    override fun Content() {
        val gradientColors = listOf(
            Color(0xFF0F2737), // dark color at the top
            Color(0xFF06151C),
            Color(0xFF000000),
            Color(0xFF000000)// lighter color at the bottom
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(gradientColors))
                .verticalScroll(rememberScrollState())
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ){
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "profile_pfp",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Column{
                    Text("Good Day", fontSize = 18.sp, color = Color.White)
                    Text("Soham Parab", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.SemiBold)

                }
            }
            val green = Color(0xFFC2F63F)
            val blue = Color(0xFF0F2737)
            val lightBlue = Color(0xFF15AEE2)
            var tabIndex by remember { mutableStateOf(0) }

            val tabs = listOf("Assets Portfolio1", "Assets Portfolio2")
            //.tabs
//            TabRow(
//                selectedTabIndex = tabIndex,
//                containerColor = Color.Transparent,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                indicator = {
//                    TabRowDefaults.SecondaryIndicator(
//                        height = 4.dp,
//                        color = green,
//                        modifier = Modifier.tabIndicatorOffset(it[tabIndex])
//                    )
//                }
//            ) {
//                tabs.forEachIndexed { index, title ->
//                    Tab(text = { Text(title, fontWeight = FontWeight.SemiBold) },
//                        selected = tabIndex == index,
//                        onClick = { tabIndex = index },
//                        selectedContentColor = green,
//                        unselectedContentColor = Color.White
//                    )
//                }
//            }

            val requestBody = InvestmentRequest(
                lifestyle_risk = risk,
                expected_annual_roi = ROI.toDouble(),
                principal_amount = principalAmount.toInt(),
                current_age = age
            )
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json()
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 60000 // Set timeout to 60 seconds
                }
            }
            var listOfClusters by remember {
                mutableStateOf(ArrayList<Cluster>())
            }
            var responseFromGemini by remember {
                mutableStateOf("")
            }
            if(responseFromGemini.isNotEmpty()){
                println("Gemini Response: "+responseFromGemini)
            }
            else{
                responseFromGemini = "Loading....."
            }
            LaunchedEffect(Unit) {
                println("Iniotial Body: "+requestBody)
                val response = client.post(urlString = "https://assetallocate.onrender.com/weights") {
                    contentType(Json)
                    setBody(requestBody)
                }
                println("Response: " + response.body<String>())
//                client.close()
                listOfClusters = parseClusters(response.body())
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-pro",
                    apiKey = "AIzaSyAcODqO3muGpih3AISgU4Dr7hZfFm3GWqU"
                )

                val typeOfInvestor = when(risk){
                    0->"conservative"
                    1->"moderate"
                    2->"aggressive"
                    else -> {""}
                }

                val content = content {
                    text("This portfolio is optimized and sugested by my algorithm to an investor with ${requestBody.lifestyle_risk} (0 for low risk, 1 for mid, 2 for high risk) tolerance and an expected annual ROI of ${requestBody.expected_annual_roi}%. The principal amount is ${requestBody.principal_amount}. The portfolio is divided into three clusters (potentially low-cap, mid-cap, and high-cap stocks).\n" +
                            "\n" +
                            "* Cluster 1: Low Risk Asset Class - Stocks: ${listOfClusters[0].Symbols}\n" +
                            "* Cluster 2: High Risk Asset Class- Stocks: ${listOfClusters[1].Symbols}\n" +
                            "* Cluster 3: Mid Risk Asset Class- Stocks: ${listOfClusters[2].Symbols}\n" +
                            "\n" +
                            "Analyze this portfolio allocation and explain why it might be suitable for this ${typeOfInvestor} investor seeking a high ROI. Consider these aspects:\n" +
                            "\n" +
                            "* Justification for allocation percentages in each cluster.\n" +
                            "* How the risk-reward profile aligns with the aggressive investment strategy.\n" +
                            "* Benefits of diversification across multiple capitalizations.\n" +
                            "* Justification of why particular stocks were put in clusters of low, mid and high risks\n"+
                            "\n" +
                            "*Note:* \n" +
                            "* Avoid mentioning specific URLs or citing the source of the data.\n" +
                            "* Focus on the analysis and justification for the aggressive portfolio allocation."+
                            "* Only tell all the positive aspects of my allocation by my own algorithm"+
                            "* Also don't mention risk as 0-1-2 instead mention as type of investor 0 for conservative investor, 1 for moderate investor and 2 for aggressive investor "
                    )
                }

                responseFromGemini = generativeModel.generateContent(content).text.toString()

            }

            val testPieChartData: ArrayList<PieChartData> = arrayListOf()
            val listOfColors = listOf(
                Color(0xFFC2F63F),
                Color(0xFFA3CE36),
                Color(0xFF84A72D)
            )
            var i = 0
            var visible by remember {
                mutableStateOf(false)
            }
            if(listOfClusters.isNotEmpty()) {
                visible = true
                for (cluster in listOfClusters) {
                    testPieChartData.add(
                        PieChartData(
                            partName = cluster.Symbols,
                            data = (cluster.Weights / principalAmount.toDouble()),
                            color = listOfColors[i]
                        )
                    )
                    println("Percentage: " + cluster.Symbols + ":" + (cluster.Weights * 100 / principalAmount.toDouble()))
                    i++
                }
                AnimatedVisibility(listOfClusters.isNotEmpty()) {
                    DonutChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        pieChartData = testPieChartData,
                        centerTitle = "MonteCarlo",
                        centerTitleStyle = TextStyle(color = Color.White),
                        descriptionStyle = TextStyle(color = Color.White),
                        textRatioStyle = TextStyle(color = Color.White, fontSize = 12.sp),
                        outerCircularColor = Color.White,
                        innerCircularColor = Color.White,
                        ratioLineColor = Color.White,
                        legendPosition = LegendPosition.DISAPPEAR,
                    )
                }
            }
            else{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ){
                    CircularProgressIndicator(
                        color = green,
                        strokeWidth = 8.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp)
                    )
                }
            }
            AnimatedVisibility(visible){
                Column(
                ){
                    Text(
                        "Adjust Risk Factor",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                    var sliderPosition by remember { mutableFloatStateOf(1f) }
                    var changedRisk by remember { mutableStateOf(0) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
//                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly

                        ){
                            Text(changedRisk.toString(), color = green)
                            Slider(
                                value = sliderPosition,
                                onValueChange = {
                                    sliderPosition = it
                                    changedRisk = it.toInt()
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = green,
                                    inactiveTrackColor = lightBlue,
                                    inactiveTickColor = Color.White,
                                    activeTickColor = Color.Black,
                                ),
                                steps = 9,
                                valueRange = 1f..10f,
                            )
//                            Text(changedRisk.toString(), color = green)
                        }


                        val coroutineScope = rememberCoroutineScope()
                        val requestBodyUpdated = InvestmentRequest(
                            lifestyle_risk = risk,
                            expected_annual_roi = ROI.toDouble(),
                            principal_amount = principalAmount.toInt(),
                            current_age = age,
                            risk = changedRisk
                        )
                        var res by remember {
                            mutableStateOf("")
                        }
                        if(res.isNotEmpty()){
                            listOfClusters = ArrayList()
                            println("RESSS: "+res)
                            listOfClusters = parseClusters(res)
                            println("list"+listOfClusters)
                            res = ""
                        }
//                        if(res.isNotEmpty()){
//                            listOfClusters = ArrayList()
//                            println("RESSS: "+res)
//                            listOfClusters = parseClusters(res)
//                            println("list"+listOfClusters)
//                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = green,
                                contentColor = Color.Black
                            ),
                            onClick = {
//                            listOfClusters = ArrayList()
                                println("Body: "+requestBodyUpdated)

                                coroutineScope.launch {
                                    val response = client.post(urlString = "https://assetallocate.onrender.com/weights") {
                                        contentType(Json)
                                        setBody(requestBodyUpdated)
                                    }
                                    res = response.body()
                                }

//                            if(res.isNotEmpty()){
//                                listOfClusters = ArrayList()
//                                println("RESSS: "+res)
//                                listOfClusters = parseClusters(res)
//                                println("list"+listOfClusters)
//                            }

//                            coroutineScope.launch {
//                                println("Sending New Update Request")
//
//                                val response = client.post(urlString = "https://assetallocate.onrender.com/weights") {
//                                    contentType(Json)
//                                    setBody(requestBodyUpdated)
//                                }
//                                println("Response: " + response.body<String>())
////                                client.close()
////                                listOfClusters = parseClusters(res.body())
//                            }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ){
                            Text("Update Portfolio")
                        }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "Portfolio Allocation",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                    //clusters
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .height(300.dp)
                            .padding(horizontal = 24.dp),
                    ){
                        for(cluster in listOfClusters.indices){
                            item {
                                PortfolioClusterSingleRow(
                                    colorList = listOfColors,
                                    percentage = (listOfClusters[cluster].Weights/principalAmount.toDouble()),
                                    cluster = listOfClusters[cluster],
                                    index = cluster
                                )
                            }
                        }
                        item{
                            Card(
                                modifier = Modifier
                                    .size(140.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(2.dp, Color(0xFF15AEE2))
                            ){
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(10.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Card(
                                            modifier = Modifier
                                                .size(64.dp),
                                            shape = CircleShape,
                                            colors = CardDefaults.cardColors(
                                                containerColor = lightBlue,
                                                contentColor = Color.Black
                                            ),
                                        ){
                                            Box(
                                                modifier = Modifier.size(64.dp)
                                            ){
                                                Text(
                                                    "100 %",
                                                    color = Color.Black,
                                                    modifier = Modifier.padding(8.dp).align(
                                                        Alignment.Center)
                                                )
                                            }
                                        }
                                        Text("Total", color = Color.White, fontSize = 20.sp)
                                    }
                                    Text("Total", fontWeight = FontWeight.Light, color = Color.White)
                                    Text("₹ ${principalAmount}", color = Color(0xFFC2F63F), fontSize = 20.sp)
                                }
                            }
                        }
                    }
                    Text(
                        "Explanation",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )

                    StyledText(responseFromGemini)

                    Spacer(Modifier.height(120.dp))

                }
            }
        }
    }

    fun parseClusters(jsonInput: String): ArrayList<Cluster> {
        val json = Json { ignoreUnknownKeys = true }  // Create a Json object with a configuration to ignore unknown keys
        val clustersData = json.decodeFromString<Clusters>(jsonInput)
        return ArrayList(clustersData.clusters)
    }

    suspend fun sendRequest(client: HttpClient, requestBody: InvestmentRequest): HttpResponse {
        val response = client.post(urlString = "https://assetallocate.onrender.com/weights") {
            contentType(Json)
            setBody(requestBody)
        }
        println("Response: " + response.body<String>())
        client.close()
        return response
//        listOfClusters = parseClusters(response.body())
    }


    @Composable
    fun PortfolioClusterSingleRow(
        colorList:List<Color>,
        percentage:Double,
        cluster: Cluster,
        index: Int,
    ){
        //clustername
        var clusterName = ""
        when(index){
            0-> clusterName = "Low Risk"
            1-> clusterName = "High Risk"
            2-> clusterName = "Mid  Risk"
        }
        val navigator = LocalNavigator.currentOrThrow
        Card(
            modifier = Modifier
                .size(140.dp)
                .clickable {
//                    navigator.parent?.push(SelectedClusterSocksScreen(risk,peroidOfInvestment,ROI,principalAmount,index, age))
                    navigator.push(SelectedClusterSocksScreen(risk,peroidOfInvestment,ROI,principalAmount,index, age))
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, Color(0xFF15AEE2))
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Card(
                        modifier = Modifier
                            .size(64.dp),
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(
                            containerColor = colorList[index],
                            contentColor = Color.Black
                        ),
                    ){
                        Box(
                            modifier = Modifier.size(64.dp)
                        ){
                            Text(
                                ((percentage / 100)*(cluster.Weights).toDouble()).toString()+" %",
                                color = Color.Black,
                                modifier = Modifier.padding(8.dp).align(Alignment.Center)
                            )
                        }
                    }
                    Text(clusterName, color = Color.White, fontSize = 20.sp)
                }
                Text("Total", fontWeight = FontWeight.Light, color = Color.White)
                Text("₹ ${cluster.Weights.toFloat()}", color = Color(0xFFC2F63F), fontSize = 20.sp)
            }
        }
    }

    fun buildStyledText(text: String): AnnotatedString {
        val boldPattern = "\\*\\*(.*?)\\*\\*".toRegex()
        return buildAnnotatedString {
            var lastIndex = 0
            boldPattern.findAll(text).forEach { matchResult ->
                val startIndex = matchResult.range.first
                val endIndex = matchResult.range.last

                // Append normal text
                append(text.substring(lastIndex, startIndex))

                // Append bold text
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 18.sp)) {
                    append(matchResult.groups[1]?.value ?: "")  // groups[1] is the text inside ** **
                }

                lastIndex = endIndex + 1
            }

            // Append any remaining text after the last match
            if (lastIndex < text.length) {
                append(text.substring(lastIndex, text.length))
            }
        }
    }

    @Composable
    fun StyledText(text: String) {
        Text(text = buildStyledText(text), color = Color.White, modifier = Modifier.padding(horizontal = 16.dp))
    }
}