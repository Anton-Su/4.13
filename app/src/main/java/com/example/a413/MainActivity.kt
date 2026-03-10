package com.example.a413

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.a413.ui.theme._413Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _413Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModelHot: ViewModelHot = ViewModelHot()) {
    val state by viewModelHot.rate.collectAsStateWithLifecycle()
    val timedate by viewModelHot.timedate.collectAsStateWithLifecycle()
    val isRising = state.currentRate >= state.previousRate
    val arrowSymbol = if (isRising) "▲" else "▼"
    val arrowColor = if (isRising) Color(0xFF2E7D32) else Color(0xFFC62828)
    val animatedArrowColor by animateColorAsState(
        targetValue = arrowColor,
        animationSpec = tween(durationMillis = 400),
        label = "arrowColor"
    )
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Курс USD → RUB",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Card (
                modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp),
                shape = RoundedCornerShape(20.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "${"%.2f".format(state.currentRate)} ₽",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = arrowSymbol,
                        fontSize = 40.sp,
                        color = animatedArrowColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Предыдущий курс: ${"%.2f".format(state.previousRate)} ₽",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Последнее обновление: $timedate",
                    fontSize = 12.sp,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModelHot.refreshRate() },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(52.dp)
            ) {
                Text(text = "Обновить", fontSize = 16.sp)
            }
        }
    }
}
