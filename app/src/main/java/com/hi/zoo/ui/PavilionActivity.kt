package com.hi.zoo.ui

import ZooTheme
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hi.zoo.model.Pavilion
import com.hi.zoo.model.PavilionViewModel
import com.hi.zoo.model.Plant
import com.hi.zoo.model.PlantViewModel
import com.hi.zoo.ui.dialog.PlantDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PavilionActivity : AppCompatActivity() {
    private val pavilionViewModel by viewModels<PavilionViewModel>()
    private val plantViewModel by viewModels<PlantViewModel>() // 使用 PlantViewModel 加載植物資料
    private var pavilion: Pavilion? = null

    companion object {
        const val PAVILION = "PAVILION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pavilion = intent.getParcelableExtra(PAVILION)
        supportActionBar?.hide()
        // 在進入頁面後才去加載植物資料
        pavilion?.eName?.let {
            plantViewModel.loadPlants(it)  // 使用 PlantViewModel 加載植物資料
        }

        setContent {
            ZooTheme {
                PavilionScreen(pavilion = pavilion, plantViewModel = plantViewModel) // 傳入 PlantViewModel
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PavilionScreen(pavilion: Pavilion?, plantViewModel: PlantViewModel) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity
    val plants = plantViewModel.plants.collectAsState().value  // 從 PlantViewModel 收集植物資料

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zoo") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // ✅ 這裡直接設定主題色
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)  // 避免內容被 AppBar 擋住
                .padding(16.dp)
        ) {
            // 顯示當前展館詳細資訊
            pavilion?.let { pavilionData ->
                item {
                    Log.d("PavilionActivity", "pavilionData.ePicURL::" + pavilionData.ePicURL)

                    // 展館圖片
                    AsyncImage(
                        model = pavilionData.safePicURL,
                        contentDescription = "Pavilion Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        onError = {
                            Log.e("Coil", "圖片載入失敗: ${it.result.throwable}")
                        }
                    )

                    // 展館名稱
                    Text(
                        text = pavilionData.eName ?: "未知展館",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // 展館資訊
                    Text(
                        text = pavilionData.eInfo ?: "No Info Available",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // 展館分類
                    Text(
                        text = pavilionData.eCategory ?: "未知分類",
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 休館資訊
                    Text(
                        text = if (pavilionData.eMemo.isNullOrBlank()) "無休館資訊" else pavilionData.eMemo,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // 打開網站按鈕
                    Button(
                        onClick = {
                            val url = pavilionData.eURL ?: "https://www.zoo.gov.tw/introduce/"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent) // 使用 Intent 開啟網址
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "在網站上預覽")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // 顯示植物列表
            items(plants) { plant ->
                PlantItem(plant = plant, onClick = { /* 點擊事件處理 */ })
            }
        }
    }
}

@Composable
fun PlantItem(plant: Plant, onClick: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? AppCompatActivity  // 取得 Activity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                activity?.supportFragmentManager?.let { fragmentManager ->
                    val dialog = PlantDetailFragment.newInstance(plant)
                    dialog.show(fragmentManager, "PlantDetailDialog")  // ✅ 直接在 PavilionActivity 上彈出
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                var isLoading by remember { mutableStateOf(true) }

                AsyncImage(
                    model = plant.safePic01URL,
                    contentDescription = "Plant Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onLoading = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false }
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = plant.fNameCh ?: "未知植物",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = plant.fAlsoKnown ?: "無別名",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
