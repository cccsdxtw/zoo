package com.hi.zoo.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.PavilionViewModel
import com.hi.zoo.ui.theme.ZooTheme
import dagger.hilt.android.AndroidEntryPoint

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
    val pavilions by viewModel.pavilions.collectAsState()  // ✅ 監聽 StateFlow，UI 會自動更新

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(pavilions) { pavilion ->
                PavilionListItem(pavilion = pavilion, onClick = { /* 點擊事件 */ })
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PavilionListItem(pavilion: Pavilion, onClick: () -> Unit) {
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
            var isLoading by remember { mutableStateOf(true) }
            // 背景圖片
            Log.d("MainActivity","pavilion.safePicURL::"+pavilion.safePicURL)
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pavilion.safePicURL)
//                    .data("https://www.gstatic.com/webp/gallery/1.jpg")
                    .crossfade(true)
                    .listener(
                        onStart = { Log.d("Coil", "開始載入圖片") },
                        onSuccess = { _, _ -> Log.d("Coil", "成功載入圖片") },
                        onError = { _, result ->

                            Log.e("Coil", "載入圖片失敗")
                            Log.e("Coil", "錯誤訊息: ${result.throwable?.message}")
                            Log.e("Coil", "錯誤原因: ${result.throwable?.cause}")
                        }
                    )
                    .build(),
                contentDescription = pavilion.eName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

//            GlideImage(
//                model = pavilion.safePicURL,
//                contentDescription = "動物園展館圖片",
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//            // 載入中的圈圈
//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .size(48.dp)
//                )
//            }

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
