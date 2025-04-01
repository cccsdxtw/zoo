package com.hi.zoo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import coil.compose.AsyncImage
import com.hi.zoo.R
import com.hi.zoo.model.Plant

class PlantDetailFragment : DialogFragment() {

    private var plant: Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plant = arguments?.getParcelable(ARG_PLANT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PlantDetailScreen(plant)
            }
        }
    }

    @Composable
    fun PlantDetailScreen(plant: Plant?) {
        if (plant == null) {
            Text(text = "找不到植物資訊", modifier = Modifier.padding(16.dp))
            return
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = plant.safePic01URL,
                    contentDescription = plant.fNameCh,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(text = plant.fNameCh ?: "無資料", style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(20.dp))
                if (!plant.fAlsoKnown.isNullOrEmpty()) {
                    Text(text = "別名:", style = MaterialTheme.typography.bodySmall)
                    Text(text = "${plant.fAlsoKnown}", style = MaterialTheme.typography.bodySmall)

                }
                Spacer(modifier = Modifier.height(16.dp))
                if (!plant.fLocation.isNullOrEmpty()) {
                    Text(text = "地點:", style = MaterialTheme.typography.bodySmall)
                    Text(text = "${plant.fLocation}", style = MaterialTheme.typography.bodySmall)

                }
                Spacer(modifier = Modifier.height(16.dp))
                if (!plant.fBrief.isNullOrEmpty()) {
                    Text(text = "簡介:", style = MaterialTheme.typography.bodySmall)
                    Text(text = "${plant.fBrief}", style = MaterialTheme.typography.bodySmall)

                }
            }
            // 關閉按鈕固定在右上角
            IconButton(
                onClick = { dismiss() },
                modifier = Modifier
                    .align(Alignment.TopEnd)  // 讓按鈕固定在右上角
                    .padding(10.dp)  // 增加間距
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary

                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            val width = (resources.displayMetrics.widthPixels * 0.9).toInt()  // 設定寬度為螢幕的 90%
            val height = (resources.displayMetrics.heightPixels * 0.7).toInt()  // 設定高度為螢幕的 70%
            it.window?.setLayout(width, height)  // 設定對話框的寬高
        }
    }

    companion object {
        private const val ARG_PLANT = "plant"

        fun newInstance(plant: Plant): PlantDetailFragment {
            return PlantDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PLANT, plant)
                }
            }
        }
    }
}
