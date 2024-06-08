package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.painterResource
import tiaahackathon.composeapp.generated.resources.Res
import tiaahackathon.composeapp.generated.resources.background_element

class GetAgeDetailsScreen: Screen {
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
        ){
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
            var denominatorForROI = 0
            var currentAge by remember {
                mutableStateOf(TextFieldValue(""))
            }
            var retirementAge by remember {
                mutableStateOf(TextFieldValue(""))
            }
            val green = Color(0xFFC2F63F)
            val blue = Color(0xFF0F2737)
            val lightBlue = Color(0xFF15AEE2)
            var buttonActive by remember {
                mutableStateOf(false)
            }
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
            ){
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Build an", color = Color.White, fontSize = 32.sp)
                Text(text = "Investment", color = Color.White, fontSize = 32.sp)
                Text(text = "portfolio for you", color = green, fontSize = 32.sp)
                Spacer(modifier = Modifier.height(120.dp))
                var currentAgeError by remember{
                    mutableStateOf(false)
                }
                var retirementAgeError by remember{
                    mutableStateOf(false)
                }
                if(currentAge.text.isNotEmpty() && currentAge.text.toInt() < 18){
                    currentAgeError = true
                }
                else{
                    currentAgeError = false
                }
                if(retirementAge.text.isNotEmpty() && retirementAge.text.toInt() <= currentAge.text.toInt()){
                    retirementAgeError = true
                }
                else{
                    retirementAgeError = false
                }
                Text("Let's talk about age first", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = currentAge,
                    onValueChange = { currentAge = it },
                    label = { Text("Enter Your Age", fontSize = 18.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = green,
                        focusedTextColor = green,
                        focusedLabelColor = green,
                        unfocusedBorderColor = lightBlue,
                        unfocusedTextColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        cursorColor = green,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Red
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = {
                        Text(text = "Years", fontSize = 18.sp, color = Color.White)
                    },
                    isError = currentAgeError,
                    textStyle = TextStyle(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = retirementAge,
                    onValueChange = { retirementAge = it },
                    label = { Text("What age are you planning to retire?", fontSize = 18.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = green,
                        focusedTextColor = green,
                        focusedLabelColor = green,
                        unfocusedBorderColor = lightBlue,
                        unfocusedTextColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        cursorColor = green,
                        errorLabelColor = Color.Red,
                        errorTextColor = Color.Red
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = {
                        Text(text = "Years", fontSize = 18.sp, color = Color.White)
                    },
                    isError = retirementAgeError,
                    textStyle = TextStyle(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                if(currentAge.text.isNotEmpty() && retirementAge.text.isNotEmpty()){
                    buttonActive = true
                }
                else{
                    buttonActive = false
                }
                val navigator = LocalNavigator.currentOrThrow
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = {
                        if (buttonActive){
                            denominatorForROI = retirementAge.text.toInt() - currentAge.text.toInt()
                            navigator.push(GetAmountDetailsScreen(denominatorForROI, currentAge.text.toInt()))
                            println("Denomainator: "+retirementAge.text.toInt()+""+currentAge.text.toInt()+"")
                        }
                        else{

                        }
                    },
                    content = {
                        Text(text = "Proceed", fontSize = 18.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(buttonActive){green} else {blue},
                        contentColor = Color.Black,
                    )
                )
            }
        }
    }
}