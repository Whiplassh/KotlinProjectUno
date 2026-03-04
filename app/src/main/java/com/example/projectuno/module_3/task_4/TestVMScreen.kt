package com.example.projectuno.module_3.task_4


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Preview(showSystemUi = true)
@Composable
fun SaveDataScreen(){
    val counterViewModel: CounterViewModel = viewModel()
    val count by counterViewModel.count.collectAsState()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.secondaryContainer
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding( 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                   containerColor = MaterialTheme.colorScheme.primary
                )
            ){
                Text(
                    text = "Every time u click the number grows",
                    modifier = Modifier
                        .padding(all = 32.dp)
                        .align(alignment = Alignment.Start),
                    fontSize = 20.sp,
                    color = Color(0xFFEA2323),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = count.toString(),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Button(
                onClick = { counterViewModel.incrementCounter()},
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ){
                Text(
                    text = "Click meee",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }


    }
}




