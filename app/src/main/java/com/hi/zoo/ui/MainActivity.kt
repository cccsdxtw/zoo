package com.hi.zoo.ui

import ZooTheme
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.PavilionViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlin.jvm.java

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZooTheme {
                PavilionMainScreen()
            }
        }
    }
}

@Composable
fun PavilionMainScreen(viewModel: PavilionViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val pavilions by viewModel.pavilions.collectAsState()  // ✅ 監聽 StateFlow，UI 會自動更新

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(pavilions) { pavilion ->
                PavilionListItem(pavilion = pavilion, onClick = { onItemClick(context, pavilion)   })
            }
        } }

}

@Composable
fun PavilionListItem(pavilion: Pavilion, onClick: () -> Unit) {
    var isLoading by remember { mutableStateOf(true) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp) // 設定固定高度
        ) {
            // 背景圖片
            Log.e("MainActivity", "pavilion.safePicURL::"+pavilion.safePicURL)
            AsyncImage(
                model = pavilion.safePicURL,  // 使用 pavilion.safePicURL 當作圖片 URL
                contentDescription = "展館背景圖片",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                onLoading = {
                    isLoading = true  // 開始載入時，顯示進度圈
                    Log.e("Coil", "載入圖片開始")

                },
                onSuccess = { result ->
                    isLoading = false  // 成功載入後，隱藏進度圈
                    Log.e("Coil", "載入圖片成功")
                },
                onError = { error ->
                    isLoading = false  // 失敗時也隱藏進度圈
                    Log.e("Coil", "載入圖片失敗: ${error?.result ?: "未知錯誤"}")
                }
            )

            // 如果圖片還在載入中，顯示進度圈
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
            }

            // 內容區塊
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = pavilion.eName ?: "未知展館",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White, // 讓文字更清楚
                    modifier = Modifier.shadow(8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pavilion.eMemo ?: "無描述",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.shadow(8.dp)
                )
            }
        }
    }
}



fun onItemClick(context: Context, pavilion: Pavilion) {
    Intent(context, PavilionActivity::class.java).apply {
        putExtra(PavilionActivity.PAVILION, pavilion)
    }.let {
        context.startActivity(it)
    }
}
