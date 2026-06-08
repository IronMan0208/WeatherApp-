package com.chotu.weatheruiclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chotu.weatheruiclone.data.api.RetrofitInstance
import com.chotu.weatheruiclone.ui.theme.WeatherUICloneTheme
import com.chotu.weatheruiclone.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherUICloneTheme {
                Weather_UI()
            }
        }
    }
}

@Composable
fun Weather_UI() {
    val viewModel: WeatherViewModel = viewModel()
    var cityInput by remember { mutableStateOf("Delhi") }

    LaunchedEffect(Unit) { viewModel.getWeather("Delhi") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0D1117),
                        Color(0xFF0F2027),
                        Color(0xFF1A1A2E)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = cityInput,
                    onValueChange = { cityInput = it },
                    placeholder = { Text("Enter city name", color = Color(0x55FFFFFF)) },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color(0x66FFFFFF)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0x22FFFFFF),
                        focusedContainerColor = Color(0x14FFFFFF),
                        unfocusedContainerColor = Color(0x0DFFFFFF),
                        focusedTextColor = Color(0xFFE2E8F0),
                        unfocusedTextColor = Color(0xFFE2E8F0),
                        cursorColor = Color(0xFF3B82F6)
                    )
                )
                Button(
                    onClick = { if (cityInput.isNotEmpty()) viewModel.getWeather(cityInput) },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text("Search", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }

            // Main Weather Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x10FFFFFF)),
                border = BorderStroke(1.dp, Color(0x14FFFFFF)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("🌤", fontSize = 52.sp, lineHeight = 56.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    if (viewModel.isLoading) {
                        CircularProgressIndicator(color = Color(0xFF3B82F6))
                    } else {
                        Text(
                            text = viewModel.temperature,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF0F4F8),
                            letterSpacing = (-2).sp
                        )
                    }
                    Text(
                        text = viewModel.cityName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xBFFFFFFF)
                    )
                    Text(
                        text = viewModel.description.uppercase(),
                        fontSize = 11.sp,
                        color = Color(0x66FFFFFF),
                        letterSpacing = 1.sp
                    )
                }
            }

            // Stats Row
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x0AFFFFFF)),
                border = BorderStroke(1.dp, Color(0x12FFFFFF)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(label = "HUMIDITY", value = viewModel.humidity, valueColor = Color(0xFF93C5FD))
                    VerticalDivider()
                    StatItem(label = "WIND", value = viewModel.windSpeed, valueColor = Color(0xFF93C5FD))
                    VerticalDivider()
                    StatItem(label = "PRESSURE", value = viewModel.pressure, valueColor = Color(0xFF93C5FD))
                }
            }

            // Forecast Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x0AFFFFFF)),
                border = BorderStroke(1.dp, Color(0x12FFFFFF)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        "TODAY'S FORECAST",
                        fontSize = 11.sp,
                        color = Color(0x59FFFFFF),
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(12.dp))

                    viewModel.forecastList.forEachIndexed { index, item ->
                        ForecastRow(
                            weather = item.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "Unknown",
                            temp = "${item.main.temp.toInt()}°C",
                            time = formatTime(item.dt_txt),
                            progress = ((item.main.temp.toInt() - 20f) / 25f).coerceIn(0f, 1f),
                            isLast = index == viewModel.forecastList.lastIndex
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Helper Composables

@Composable
fun StatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = valueColor)
        Spacer(Modifier.height(3.dp))
        Text(label, fontSize = 10.sp, color = Color(0x59FFFFFF), letterSpacing = 0.8.sp)
    }
}

@Composable
fun ForecastRow(
    weather: String,
    temp: String,
    time: String,
    progress: Float,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Weather icon based on description
        Text(
            text = when {
                weather.contains("rain", ignoreCase = true) -> "🌧"
                weather.contains("cloud", ignoreCase = true) -> "⛅"
                weather.contains("clear", ignoreCase = true) -> "☀️"
                weather.contains("snow", ignoreCase = true) -> "❄️"
                weather.contains("storm", ignoreCase = true) -> "⛈"
                weather.contains("mist", ignoreCase = true) ||
                        weather.contains("fog", ignoreCase = true)  -> "🌫"
                else -> "🌤"
            },
            fontSize = 22.sp,
            modifier = Modifier.width(36.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(weather, fontSize = 14.sp, color = Color(0xA6FFFFFF), fontWeight = FontWeight.Medium)
            Text(time, fontSize = 11.sp, color = Color(0x47FFFFFF))
        }

        // Progress bar
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x15FFFFFF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF3B82F6), Color(0xFF93C5FD))
                        )
                    )
            )
        }

        Spacer(Modifier.width(12.dp))

        Text(
            text = temp,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFE2E8F0)
        )
    }

    if (!isLast) {
        HorizontalDivider(color = Color(0x0DFFFFFF))
    }
}

fun formatTime(dateTime: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val date = inputFormat.parse(dateTime)
    return outputFormat.format(date!!)
}

// Basic UI
@Composable
fun Weather_UI1() {

    val viewModel: WeatherViewModel = viewModel()
    var cityInput by remember {
        mutableStateOf("Delhi")
    }

    LaunchedEffect(Unit) {
        viewModel.getWeather("Delhi")
        viewModel.getForecast("Delhi")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
    ) {
        OutlinedTextField(
            value = cityInput,

            onValueChange = {
                cityInput = it
            },
            label = {
                Text("Enter City")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (cityInput.isNotEmpty()) {
                    viewModel.getWeather(cityInput)
                    viewModel.getForecast(cityInput)
                }
            }
        ) {
            Text("Search")
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0A0A0A)
            ),

            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🌤", fontSize = 40.sp)
                Spacer(modifier = Modifier.height(8.dp))
                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        viewModel.temperature,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Text(
                    viewModel.cityName,
                    fontSize = 18.sp,
                    color = Color(0xFFCCCCCC)
                )
                Text(
                    viewModel.description,
                    fontSize = 14.sp,
                    color = Color(0xFF888888)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StateItem(label = "Humidity", value = viewModel.humidity)
            VerticalDivider()
            StateItem(label = "Wind", value = viewModel.windSpeed)
            VerticalDivider()
            StateItem(label = "Pressure", value = viewModel.pressure)
        }
        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0A0A0A)
            )
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Text(
                    "Today Forecast",
                    fontSize = 14.sp,
                    color = Color(0xFFAAAAAA),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 4.dp)
                )

                HorizontalDivider()

                Spacer(modifier = Modifier.height(10.dp))

                viewModel.forecastList.forEach { item ->

                    Forecast(
                        weather = item.weather.firstOrNull()?.description ?: "Unknown",
                        temp = "${item.main.temp.toInt()}°C"
                    )
                }
            }
        }
    }
}

@Composable
fun Forecast(
    weather: String,
    temp: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = weather,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = Color(0xFFDDDDDD)
        )

        Text(
            text = temp,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StateItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            fontSize = 11.sp,
            color = Color(0xFF888888)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            fontSize = 14.sp,
            color = Color(0xFFDDDDDD),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(36.dp)
            .background(Color(0xFF444444))
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherUICloneTheme {
        Weather_UI1()
    }
}