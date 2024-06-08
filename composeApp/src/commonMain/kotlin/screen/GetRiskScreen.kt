package screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.aggressive
import tiaahackathon.composeapp.generated.resources.background_element
import tiaahackathon.composeapp.generated.resources.conservative
import tiaahackathon.composeapp.generated.resources.king
import tiaahackathon.composeapp.generated.resources.moderate

data class GetRiskScreen(val ROI: Float, val amountInvestedMonthly: Float, val periodOfInvestment: Int, val currentAge: Int): Screen {

    @Composable
    override fun Content() {
        val gradientColors = listOf(
            Color(0xFF0F2737), // dark color at the top
            Color(0xFF06151C),
            Color(0xFF000000),
            Color(0xFF000000)// lighter color at the bottom
        )
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
                    .padding(top = 60.dp)
                    .width(3000.dp)
                    .height(300.dp)
                    .align(Alignment.TopCenter)
            )
            val green = Color(0xFFC2F63F)
            val blue = Color(0xFF0F2737)
            val lightBlue = Color(0xFF15AEE2)
            var risk by remember {
                mutableStateOf(0)
            }
            var buttonActive by remember {
                mutableStateOf(false)
            }
            var risk0Selected by remember {
                mutableStateOf(false)
            }
            var risk1Selected by remember {
                mutableStateOf(false)
            }
            var risk2Selected by remember {
                mutableStateOf(false)
            }
            when {
                risk2Selected -> {
                    risk = 2
                }
                risk1Selected -> {
                    risk = 1
                }
                risk0Selected -> {
                    risk = 0
                }
            }
            if(risk0Selected or risk1Selected or risk2Selected){
                buttonActive = true
            }
            else{
                buttonActive = true
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())

            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Build an", color = Color.White, fontSize = 32.sp)
                Text(text = "Investment", color = Color.White, fontSize = 32.sp)
                Text(text = "portfolio for you", color = green, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(120.dp))


                Text(text = "What type of Investor", color = Color(0xFF1292BE), fontSize = 24.sp)
                Text(text = "are you?", color = lightBlue, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(32.dp))

                //1
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            risk2Selected = true
                            risk1Selected = false
                            risk0Selected = false
                        },
                    shape = RoundedCornerShape(16.dp),
                    border = if(!risk2Selected){
                        BorderStroke(1.dp, Color(0xFF0E7699))
                    }else{
                        BorderStroke(1.dp, green)
                         },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                    ),
                ){
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            painter = painterResource(Res.drawable.aggressive),
                            contentDescription = "king",
                            tint = green,
                            modifier = Modifier
                                .size(100.dp)
//                                .border(1.dp, Color(0xFF0E7699), RoundedCornerShape(16.dp))
                                .padding(20.dp)
                        )
                        Column(
                        ){
                            Text(
                                "I'm",
                                color = Color.White,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Aggressive",
                                color = green,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Included high risk*",
                                color = Color.White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                //2
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            risk1Selected = true
                            risk2Selected = false
                            risk0Selected = false
                        },
                    shape = RoundedCornerShape(16.dp),
                    border = if(!risk1Selected){
                        BorderStroke(1.dp, Color(0xFF0E7699))
                    }else{
                        BorderStroke(1.dp, green)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            painter = painterResource(Res.drawable.moderate),
                            contentDescription = "king",
                            tint = green,
                            modifier = Modifier
                                .size(100.dp)
//                                .border(1.dp, Color(0xFF0E7699), RoundedCornerShape(16.dp))
                                .padding(20.dp)
                        )
                        Column(
                        ){
                            Text(
                                "I'm",
                                color = Color.White,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Moderate",
                                color = green,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Includes mid risk*",
                                color = Color.White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                //3
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            risk0Selected = true
                            risk1Selected = false
                            risk2Selected = false
                        },
                    shape = RoundedCornerShape(16.dp),
                    border = if(!risk0Selected){
                        BorderStroke(1.dp, Color(0xFF0E7699))
                    }else{
                        BorderStroke(1.dp, green)
                    },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                ){
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Icon(
                            painter = painterResource(Res.drawable.conservative),
                            contentDescription = "conservative",
                            tint = green,
                            modifier = Modifier
                                .size(100.dp)
//                                .border(1.dp, Color(0xFF0E7699), RoundedCornerShape(16.dp))
                                .padding(20.dp)
                        )
                        Column(
                        ){
                            Text(
                                "I'm",
                                color = Color.White,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Conservative",
                                color = green,
                                fontSize = 22.sp,
                            )
                            Text(
                                "Includes low risk*",
                                color = Color.White,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }

                val navigator = LocalNavigator.currentOrThrow
                Button(
                    onClick = {
                        if (buttonActive) {
                            val r = risk
                            navigator.push(FinalDetailsScreen(periodOfInvestment, ROI, amountInvestedMonthly , risk = r, currentAge = currentAge))
                        } else {

                        }
                    },
                    content = {
                        Text(text = "Proceed", fontSize = 18.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (risk0Selected or risk1Selected or risk2Selected) {
                            green
                        } else {
                            blue
                        },
                        contentColor = Color.Black,
                    )
                )
            }
        }
    }
}