package com.example.projectuno

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectuno.ui.theme.ProjectUnoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectUnoTheme {
                MishinGlass()
                }
            }
        }
    }



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjectUnoTheme {
        Greeting("Android")
    }
}
@Composable
fun Hello(name: String?, modifier: Modifier = Modifier){
    if((name != null) and (name != "")){
        Text(
            text = "Hello $name.",
            modifier = Modifier
                .padding(
                    top = 30.dp,
                    start = 10.dp
                )
                .fillMaxSize()
                .background(Color.White)
        )
    }
    else
        Text(
            text = "Имя не задано",
            modifier = Modifier
                .padding(
                    top = 30.dp,
                    start = 10.dp
                )
                .fillMaxSize()
        )
}
@Preview(
    name = "portrait",
    showSystemUi = true,
    device = "spec:orientation=portrait,width=411dp,height=891dp"
)
@Composable
fun HelloPreviewPortrait() {
    ProjectUnoTheme {
        Hello("Мишин глаз")
    }
}

@Preview(
    name = "landscape",
    showSystemUi = true,
    device = "spec:orientation=landscape,width=411dp,height=891dp"
)
@Composable
fun HelloPreviewLandscape() {
    ProjectUnoTheme {
        Hello("Мишин глаз")
    }
}

@Preview(
    name = "round",
    showSystemUi = (true),
    device = "spec:width=200dp,height=200dp, isRound=true",
    backgroundColor = 0xFFFFEB3B,
    showBackground = true
)
@Composable
fun HelloPreviewRound() {
    ProjectUnoTheme {
        Hello(null)
    }
}

@Preview(showSystemUi = (true))
@Composable
fun Mihilo1(){
    Text(
        text = stringResource(R.string.MihilinGlass),
        color = Color.Green,
        fontSize = 16.sp,
        fontStyle = FontStyle.Italic,
        textAlign = TextAlign.Center

    )
}

@Preview(showSystemUi = (true))
@Composable
fun Mihilo2(){
    Text(
        text = stringResource(R.string.MihilinGlass),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(top = 30.dp)
    )
}

@Preview(showSystemUi = (true),
    backgroundColor = 0xFF4CAF50,
    showBackground = true)
@Composable
fun Mihilo3(){
    Text(
        text = stringResource(R.string.MihilinGlass),
        modifier = Modifier.padding(top = 48.dp).fillMaxSize(),
        color = Color.Black,
        fontSize = 24.sp,
        textDecoration = TextDecoration.Underline,
    )
}

@Preview(showBackground = true)
@Composable
fun MishinGlass(){
    Button(
        onClick = {},
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color.Gray),
        colors = ButtonDefaults.buttonColors(Color(0xFFADADAD)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
            .shadow(4.dp)
    ){
        Text(
            text = "выдави меня",
            color = Color(0xFF000000)
        )
    }
}
//fewfef.CLMFROL3GRO;RG