package com.example.arenova.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme
import java.text.NumberFormat
import java.util.Locale

private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

data class TimeSlotItem(
    val time: String,
    val period: String,
    val startHour: Int,
    val isAvailable: Boolean = true
)

@Composable
fun ScheduleSelectionScreen(
    court: CourtDetailItem = CourtDetailItem(),
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onProceedClick: (CourtDetailItem, String, String, Int, String) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val defaultDate = "01 Jan 2026"

    val timeSlots = remember {
        listOf(
            TimeSlotItem("08–09", "Pagi", 8, isAvailable = true),
            TimeSlotItem("09–10", "Pagi", 9, isAvailable = true),
            TimeSlotItem("10–11", "Siang", 10, isAvailable = true),
            TimeSlotItem("11–12", "Siang", 11, isAvailable = true),
            TimeSlotItem("13–14", "Siang", 13, isAvailable = true),
            TimeSlotItem("14–15", "Siang", 14, isAvailable = true),
            TimeSlotItem("15–16", "Sore", 15, isAvailable = true),
            TimeSlotItem("16–17", "Sore", 16, isAvailable = true),
            TimeSlotItem("17–18", "Sore", 17, isAvailable = true)
        )
    }

    var dateInputText by remember { mutableStateOf(defaultDate) }
    var selectedTimeSlot by remember { mutableStateOf("14–15") }
    var selectedStartHour by remember { mutableIntStateOf(14) }
    var selectedDurationHours by remember { mutableIntStateOf(1) }

    val basePriceNumber = remember(court.price) {
        val digits = court.price.filter { it.isDigit() }
        digits.toIntOrNull() ?: 100000
    }

    val totalPriceNumber = basePriceNumber * selectedDurationHours
    val formattedTotalPrice = remember(totalPriceNumber) {
        val formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))
        "Rp ${formatter.format(totalPriceNumber)}"
    }

    val endHour = selectedStartHour + selectedDurationHours
    val timeRangeLabel = String.format(Locale.getDefault(), "%02d:00 - %02d:00 WIB", selectedStartHour, endHour)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ArenovaScreenBg,
        topBar = {
            ScheduleTopBar(
                onBackClick = onBackClick,
                onShareClick = {
                    onShareClick()
                    Toast.makeText(context, "Membagikan jadwal ${court.name}", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            ScheduleBottomBar(
                totalPrice = formattedTotalPrice,
                onProceedClick = {
                    val finalDate = dateInputText.trim().ifBlank { defaultDate }
                    onProceedClick(
                        court,
                        finalDate,
                        timeRangeLabel,
                        selectedDurationHours,
                        formattedTotalPrice
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ==========================================
            // 1. ARENA SUMMARY CARD (TOP)
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                color = RevoraGreenSoftBg,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (court.sportType.contains("Badminton", ignoreCase = true) ||
                                    court.sportType.contains("Padel", ignoreCase = true)
                                ) {
                                    R.drawable.ic_racket
                                } else {
                                    R.drawable.ic_ball_soccer
                                }
                            ),
                            contentDescription = "Sport Icon",
                            tint = RevoraGreenPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (court.name.isNotBlank()) court.name else "Arenova Grand Arena",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${court.sportType} / Mini • Indoor AC",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Rp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFEA580C)
                        )
                        Text(
                            text = court.price.replace("Rp", "").trim(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEA580C)
                        )
                        Text(
                            text = "/ jam",
                            fontSize = 11.5.sp,
                            color = ArenovaSubtitleGray
                        )
                    }
                }
            }

            // ==========================================
            // 2. SECTION: PILIH TANGGAL (INPUT KETIK)
            // ==========================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pilih Tanggal",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Text(
                        text = "Hari Ini",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RevoraGreenPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                dateInputText = defaultDate
                                Toast.makeText(context, "Tanggal disetel ke Hari Ini", Toast.LENGTH_SHORT).show()
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dateInputText,
                    onValueChange = { dateInputText = it },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Contoh: 25 Nov 2026",
                            fontSize = 13.5.sp,
                            color = ArenovaSubtitleGray
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .size(36.dp)
                                .background(RevoraGreenSoftBg, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar),
                                contentDescription = "Kalender",
                                tint = RevoraGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = RevoraGreenPrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedTextColor = ArenovaDarkText,
                        unfocusedTextColor = ArenovaDarkText
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ==========================================
            // 3. SECTION: JAM TERSEDIA
            // ==========================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Jam Tersedia",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Pilih jam mulai latihan Anda",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(Color(0xFFDBEAFE), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Kosong",
                                fontSize = 11.sp,
                                color = ArenovaSubtitleGray
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(RevoraGreenPrimary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Dipilih",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RevoraGreenPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val rows = timeSlots.chunked(3)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { slot ->
                                val isSelected = selectedTimeSlot == slot.time && slot.isAvailable
                                val bgBrush = if (isSelected) RevoraGreenGradient else null
                                val bgColor = when {
                                    isSelected -> Color.Transparent
                                    !slot.isAvailable -> Color(0xFFF1F5F9)
                                    else -> Color.White
                                }

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(64.dp)
                                        .shadow(
                                            elevation = if (isSelected || !slot.isAvailable) 0.dp else 1.dp,
                                            shape = RoundedCornerShape(14.dp),
                                            clip = false
                                        ),
                                    shape = RoundedCornerShape(14.dp),
                                    color = bgColor,
                                    border = if (!isSelected && slot.isAvailable) {
                                        BorderStroke(1.dp, Color(0xFFE2E8F0))
                                    } else {
                                        null
                                    }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .then(
                                                if (bgBrush != null) {
                                                    Modifier.background(bgBrush)
                                                } else {
                                                    Modifier
                                                }
                                            )
                                            .clickable(enabled = slot.isAvailable) {
                                                selectedTimeSlot = slot.time
                                                selectedStartHour = slot.startHour
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = slot.time,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when {
                                                isSelected -> Color.White
                                                !slot.isAvailable -> Color(0xFF94A3B8)
                                                else -> ArenovaDarkText
                                            }
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = if (isSelected) "Dipilih" else if (!slot.isAvailable) "sore" else slot.period,
                                            fontSize = 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                            color = when {
                                                isSelected -> Color.White.copy(alpha = 0.95f)
                                                !slot.isAvailable -> Color(0xFF94A3B8)
                                                else -> ArenovaSubtitleGray
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 4. SECTION: DURASI (JAM)
            // ==========================================
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Durasi (jam)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Text(
                        text = "Maksimal 3 jam",
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 0.5.dp,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(RevoraGreenSoftBg, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_timer),
                                contentDescription = "Durasi",
                                tint = RevoraGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "$selectedDurationHours jam",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = timeRangeLabel,
                                fontSize = 12.sp,
                                color = ArenovaSubtitleGray
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_chevron_down),
                            contentDescription = "Pilih Durasi",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(1, 2, 3).forEach { hours ->
                        val isSelected = selectedDurationHours == hours

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .then(
                                    if (isSelected) {
                                        Modifier.background(brush = RevoraGreenGradient)
                                    } else {
                                        Modifier
                                            .background(Color.White)
                                            .border(
                                                1.dp,
                                                Color(0xFFE2E8F0),
                                                RoundedCornerShape(20.dp)
                                            )
                                    }
                                )
                                .clickable { selectedDurationHours = hours },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$hours Jam",
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else ArenovaDarkText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ScheduleTopBar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ArenovaScreenBg
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Pilih Jadwal",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "Bagikan",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            brush = RevoraGreenGradient,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Profil Pengguna",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduleBottomBar(
    totalPrice: String,
    onProceedClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawLine(
                        color = Color(0xFFF1F5F9),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Pembayaran:",
                        fontSize = 13.sp,
                        color = ArenovaSubtitleGray
                    )

                    Text(
                        text = totalPrice,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEA580C)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onProceedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(14.dp),
                            ambientColor = RevoraGreenPrimary.copy(alpha = 0.35f),
                            spotColor = RevoraGreenPrimary.copy(alpha = 0.45f)
                        ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = RevoraGreenGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "LANJUT",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_forward),
                                contentDescription = "Lanjut Pembayaran",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScheduleSelectionScreenPreview() {
    ArenovaTheme {
        ScheduleSelectionScreen()
    }
}