//package com.example.projectuno.module_3.task_4
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.LineHeightStyle
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun SaveDataScreen(
//    modifier: Modifier = Modifier,
//    SaveDataModel: SaveDataModel = saveDataModel()
//){
//    Column(
//        modifier = modifier
//            .padding(horizontal = 60.dp)
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ){
//        Text(
//            text = "Every time u click the number grow",
//            modifier = Modifier
//                .padding(bottom = 16.dp, top=60.dp)
//                .align(alignment = Alignment.Start)
//        )
//        EditNumberField(
//            modifier = Modifier.padding(bottom = 32.dp)
//                .fillMaxWidth(),
//            text =
//        )
//    }
//}
//
//fun saveDataModel(): SaveDataModel {}
//
//
//annotation class SaveDataModel
