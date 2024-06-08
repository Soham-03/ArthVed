package screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.background_element
import tiaahackathon.composeapp.generated.resources.king
import kotlin.math.pow

class FinalDetailsScreen(val periodOfInvestment:Int ,val ROI:Float , val amountInvestedMonthly:Float ,val risk:Int, val currentAge:Int): Screen {

    @Composable
    override fun Content() {
        val gradientColors = listOf(
            Color(0xFF0F2737), // dark color at the top
            Color(0xFF06151C),
            Color(0xFF000000),
            Color(0xFF000000)// lighter color at the bottom
        )
        val green = Color(0xFFC2F63F)
        val blue = Color(0xFF0F2737)
        val lightBlue = Color(0xFF15AEE2)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = Brush.linearGradient(gradientColors))
        ) {
            Image(
                painter = painterResource(Res.drawable.background_element),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(3000.dp)
                    .height(300.dp)
                    .align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "One Last", color = Color.White, fontSize = 32.sp)
                Text(text = "Step to Optimize", color = green, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(120.dp))
                val blueGradient = listOf(
                    Color(0xFF0E7699),
                    Color(0xFF1292BE),
                    Color(0xFF15AEE2)
                )
                Text(text = "Investment Summary", color = Color.White, fontSize = 24.sp)
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    //1
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(elevation = 30.dp, shape = RoundedCornerShape(16.dp), spotColor = lightBlue, ambientColor = lightBlue),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightBlue),
                        colors = CardDefaults.cardColors(
                            containerColor = blue
                        ),
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = "${ROI} %",
                                color = green,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                            Text(
                                text = "Return on Investment",
                                color = lightBlue,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                        }
                    }
                    var portfolioType = ""
                    when(risk){
                        0-> portfolioType = "Conservative"
                        1-> portfolioType = "Moderate"
                        2-> portfolioType = "Aggressive"
                    }
                    //2
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(elevation = 30.dp, shape = RoundedCornerShape(16.dp), spotColor = lightBlue, ambientColor = lightBlue),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, lightBlue),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF04212C)
                        ),
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = portfolioType,
                                color = green,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                            Text(
                                text = "Predicted Portfolio Type",
                                color = lightBlue,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth(),
                            )
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, lightBlue),
                    colors = CardDefaults.cardColors(
                        containerColor = blue
                    ),
                    elevation = CardDefaults.cardElevation(20.dp),
                    modifier = Modifier
                        .shadow(elevation = 30.dp, shape = RoundedCornerShape(16.dp), spotColor = lightBlue, ambientColor = lightBlue),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ){
                        Text(
                            text = "${amountInvestedMonthly*periodOfInvestment*12} ₹",
                            color = green,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                        Text(
                            text = "Total amount to be Investment over \n${periodOfInvestment} years",
                            color = lightBlue,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    }
                }
            }
            val navigator = LocalNavigator.currentOrThrow
            Button(
                onClick = {

                    SharedObj.peroidOfInvestment = periodOfInvestment
                    SharedObj.ROI = ROI
                    SharedObj.principalAmount = amountInvestedMonthly*periodOfInvestment*12
                    SharedObj.risk = risk
                    SharedObj.currentAge = currentAge

//                    Navigator(HomeScreen(risk, periodOfInvestment, ROI, amountInvestedMonthly*periodOfInvestment))
                    navigator.push(HomeScreen(risk, periodOfInvestment, ROI, amountInvestedMonthly*periodOfInvestment*12, age = currentAge))
//                          navigator.push(ClustersScreen(risk, periodOfInvestment, ROI, amountInvestedMonthly*periodOfInvestment))
                },
                content = {
                    Text(text = "Proceed", fontSize = 18.sp)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green,
                    contentColor = Color.Black
                )
            )
        }
    }
}