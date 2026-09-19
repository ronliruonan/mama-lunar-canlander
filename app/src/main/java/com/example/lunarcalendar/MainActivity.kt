package com.example.lunarcalendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.lunarcalendar.calendar.CalendarDay
import com.example.lunarcalendar.calendar.CalendarRepository
import com.example.lunarcalendar.widget.LunarWidgetProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var day by mutableStateOf(CalendarRepository.today())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalendarScreen(day, onAddWidget = ::addWidget, onRefresh = {
                refreshCalendar()
                Toast.makeText(this, "已更新为当前日期", Toast.LENGTH_SHORT).show()
            })
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    delay(60_000)
                    val current = CalendarRepository.today()
                    if (current != day) {
                        day = current
                        LunarWidgetProvider.refreshAll(this@MainActivity)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCalendar()
    }

    private fun refreshCalendar() {
        day = CalendarRepository.today()
        LunarWidgetProvider.refreshAll(this)
    }

    private fun addWidget() {
        val manager = AppWidgetManager.getInstance(this)
        if (manager.isRequestPinAppWidgetSupported) {
            val requested = manager.requestPinAppWidget(
                ComponentName(this, LunarWidgetProvider::class.java), null, null
            )
            if (requested) {
                Toast.makeText(this, "请在桌面弹窗中确认添加", Toast.LENGTH_LONG).show()
                return
            }
        }
        Toast.makeText(this, "请长按桌面空白处，在小部件中找到「今日农历」", Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun CalendarScreen(day: CalendarDay, onAddWidget: () -> Unit, onRefresh: () -> Unit) {
    val dark = isSystemInDarkTheme()
    val colors = if (dark) darkColorScheme(
        primary = Color(0xFFCFDCC5), onPrimary = Color(0xFF203A2C),
        background = Color(0xFF17211B), surface = Color(0xFF233127),
        onBackground = Color(0xFFF0ECDD), onSurface = Color(0xFFF0ECDD),
        onSurfaceVariant = Color(0xFFB8C2B4), tertiary = Color(0xFFEEB396)
    ) else lightColorScheme(
        primary = Color(0xFF304F3E), onPrimary = Color.White,
        background = Color(0xFFF3F0E7), surface = Color(0xFFFFFCF4),
        onBackground = Color(0xFF18251C), onSurface = Color(0xFF18251C),
        onSurfaceVariant = Color(0xFF3F4A40), tertiary = Color(0xFF8C3524)
    )
    val context = LocalContext.current
    var showLicense by remember { mutableStateOf(false) }
    val weekendColor = colorResource(R.color.widget_weekend)
    val weekday = buildAnnotatedString {
        if (day.isWeekend) {
            append(day.weekdayText.dropLast(1))
            withStyle(SpanStyle(color = weekendColor)) { append(day.weekdayText.takeLast(1)) }
        } else {
            append(day.weekdayText)
        }
    }
    val termColor = if (day.isSolarTermToday) colors.tertiary else colors.onSurface
    MaterialTheme(colorScheme = colors) {
        Scaffold(containerColor = colors.background) { insets ->
            Box(Modifier.fillMaxSize().padding(insets), contentAlignment = Alignment.TopCenter) {
                Column(
                    Modifier.widthIn(max = 600.dp).fillMaxWidth()
                        .verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Surface(color = colors.primary, shape = RoundedCornerShape(14.dp), modifier = Modifier.size(46.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("历", color = colors.onPrimary, fontSize = 25.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        Column {
                            Text("今日农历", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                            Text("日日有时，岁岁有节", fontSize = 13.sp, color = colors.onSurfaceVariant)
                        }
                    }
                    Card(
                        Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(day.lunarText, Modifier.fillMaxWidth(), fontSize = 44.sp, lineHeight = 54.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Text(weekday, Modifier.fillMaxWidth(), fontSize = 32.sp, lineHeight = 42.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            Spacer(Modifier.height(4.dp))
                            Text(day.fullSolarText, fontSize = 20.sp, color = colors.onSurfaceVariant)
                            Text(day.yearText, fontSize = 18.sp, color = colors.onSurfaceVariant)
                            Surface(color = termColor.copy(alpha = 0.05f), shape = RoundedCornerShape(14.dp)) {
                                Text(day.solarTermText, Modifier.padding(14.dp), color = termColor, fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold)
                            }
                            if (day.festivalText.isNotEmpty()) {
                                Text("今日${day.festivalText}", color = colors.tertiary, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                            }
                            Text("北京时间 · 离线计算", fontSize = 16.sp, color = colors.onSurfaceVariant)
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("把日子放在桌面上", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "大字看农历，随手知节气。添加小部件后，无需每天打开应用。",
                            fontSize = 18.sp, lineHeight = 28.sp, color = colors.onSurfaceVariant
                        )
                        Button(
                            onClick = onAddWidget, modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) { Text("添加到桌面", Modifier.padding(vertical = 6.dp), fontSize = 20.sp) }
                        OutlinedButton(onClick = onRefresh, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                            Text("刷新日期与桌面卡片", Modifier.padding(vertical = 6.dp), fontSize = 20.sp)
                        }
                    }
                    Text(
                        "推荐使用 4×3 大字卡片。旧卡片如仍然较小，可长按卡片向下拉大，或重新添加。\n\nOnePlus 添加入口：长按桌面空白处 → 小部件 → 全部 → 列表底部 Home screen widgets → 今日农历。\n\n卡片按北京时间换日。系统省电可能延迟自动刷新，点击卡片右上角 ↻ 可立即更新。",
                        fontSize = 16.sp, lineHeight = 26.sp, color = colors.onSurfaceVariant
                    )
                    TextButton(onClick = { showLicense = true }) { Text("历法来源与开源许可") }
                }
            }
        }
        if (showLicense) {
            val license = remember { context.assets.open("lunar-LICENSE.txt").bufferedReader().use { it.readText() } }
            AlertDialog(
                onDismissRequest = { showLicense = false }, title = { Text("历法来源") },
                text = {
                    Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("本地历法计算使用 lunar-java 1.7.7。\n项目：https://github.com/6tail/lunar-java\n农历、节气统一采用北京时间；干支年以农历正月初一换年。")
                        Text(license, fontSize = 12.sp)
                    }
                },
                confirmButton = { TextButton(onClick = { showLicense = false }) { Text("知道了") } }
            )
        }
    }
}
