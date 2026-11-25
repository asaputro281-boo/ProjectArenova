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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme
import kotlinx.coroutines.delay
import java.util.Locale

private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

enum class PaymentMethodType(
    val title: String,
    val subtitle: String,
    val iconRes: Int,
    val iconTint: Color,
    val iconBg: Color,
    val badge: String? = null,
    val bottomLabel: String
) {
    E_WALLET(
        title = "E-Wallet",
        subtitle = "GoPay, OVO, ShopeePay, DANA",
        iconRes = R.drawable.ic_wallet,
        iconTint = Color(0xFFEA580C),
        iconBg = Color(0xFFFFF7ED),
        bottomLabel = "E-Wallet"
    ),
    QRIS(
        title = "QRIS",
        subtitle = "Scan via m-Banking atau E-Wallet",
        iconRes = R.drawable.ic_qr_code,
        iconTint = Color(0xFF059669),
        iconBg = Color(0xFFE6F4EA),
        badge = "Instan",
        bottomLabel = "QRIS Instan"
    ),
    CASH(
        title = "Tunai (Bayar di Tempat)",
        subtitle = "Bayar langsung di lokasi lapangan",
        iconRes = R.drawable.ic_cash,
        iconTint = Color(0xFF475569),
        iconBg = Color(0xFFF1F5F9),
        bottomLabel = "Tunai"
    )
}

@Composable
fun PaymentMethodScreen(
    court: CourtDetailItem = CourtDetailItem(),
    bookingDate: String = "01 Jan 2026",
    bookingTime: String = "15:00 – 16:00",
    totalPrice: String = "Rp 100.000",
    onBackClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onPaymentSuccess: (method: PaymentMethodType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedMethod by remember { mutableStateOf(PaymentMethodType.QRIS) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Countdown Timer (1 jam = 60 menit = 3600 detik)
    var remainingSeconds by remember { mutableIntStateOf(60 * 60) }

    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds--
        }
    }

    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timerString = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds)

    val displayCourtName = remember(court.name, court.courtTag) {
        if (court.name.isNotBlank()) {
            val tag = if (court.courtTag.isNotBlank()) court.courtTag else "Lap. A"
            "${court.name} • $tag"
        } else {
            "Arenova Indoor • Lap. A"
        }
    }

    val displaySchedule = remember(bookingDate, bookingTime) {
        val cleanTime = bookingTime.replace("WIB", "").trim()
        "$bookingDate, $cleanTime"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ArenovaScreenBg,
        topBar = {
            PaymentTopBar(
                onBackClick = onBackClick,
                onHelpClick = {
                    onHelpClick()
                    Toast.makeText(context, "Pusat Bantuan & Pembayaran Arenova", Toast.LENGTH_SHORT).show()
                },
                onProfileClick = onProfileClick
            )
        },
        bottomBar = {
            PaymentBottomBar(
                selectedMethod = selectedMethod,
                onLanjutkanClick = {
                    showSuccessDialog = true
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFDF0E7),
                border = BorderStroke(1.dp, Color(0xFFFED7AA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 11.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_timer),
                            contentDescription = "Timer Pembayaran",
                            tint = Color(0xFFC2410C),
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selesaikan dalam",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF9A3412)
                        )
                    }

                    Text(
                        text = timerString,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9A3412),
                        letterSpacing = 1.sp
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Pembayaran",
                            fontSize = 13.5.sp,
                            color = ArenovaSubtitleGray,
                            fontWeight = FontWeight.Medium
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = RevoraGreenSoftBg
                        ) {
                            Text(
                                text = "ARENOVA PAY",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = RevoraGreenEnd,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = totalPrice,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00593B)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Booking Court",
                                    fontSize = 12.sp,
                                    color = ArenovaSubtitleGray
                                )
                                Text(
                                    text = displayCourtName,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ArenovaDarkText
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Jadwal Bermain",
                                    fontSize = 12.sp,
                                    color = ArenovaSubtitleGray
                                )
                                Text(
                                    text = displaySchedule,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ArenovaDarkText
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Metode Pembayaran",
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_check),
                        contentDescription = "Transaksi Aman",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Transaksi Aman",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF059669)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PaymentMethodType.entries.forEach { method ->
                    val isSelected = selectedMethod == method

                    PaymentMethodItemCard(
                        method = method,
                        isSelected = isSelected,
                        onClick = { selectedMethod = method }
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFFDBEAFE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_community),
                        contentDescription = "Main bareng teman",
                        tint = Color(0xFF0D9488),
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Main bareng teman?",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Setelah bayar, kamu bisa langsung share link split bill ke grup WhatsApp tim kamu.",
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    if (showSuccessDialog) {
        Dialog(onDismissRequest = { showSuccessDialog = false }) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(RevoraGreenSoftBg, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = "Berhasil",
                            tint = RevoraGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Instruksi Pembayaran",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Metode ${selectedMethod.title} siap diproses. Silakan selesaikan tagihan sebesar $totalPrice sebelum batas waktu habis.",
                        fontSize = 13.sp,
                        color = ArenovaSubtitleGray,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showSuccessDialog = false
                            onPaymentSuccess(selectedMethod)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .shadow(
                                elevation = 3.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = RevoraGreenPrimary.copy(alpha = 0.35f),
                                spotColor = RevoraGreenPrimary.copy(alpha = 0.45f)
                            ),
                        shape = RoundedCornerShape(12.dp),
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
                            Text(
                                text = "MENGERTI",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodItemCard(
    method: PaymentMethodType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        BorderStroke(2.dp, Color(0xFF006837)),
                        RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier.border(
                        BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        RoundedCornerShape(16.dp)
                    )
                }
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) Color(0xFFF7FCF8) else Color.White,
        shadowElevation = if (isSelected) 0.dp else 0.5.dp
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
                    .background(method.iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = method.iconRes),
                    contentDescription = method.title,
                    tint = method.iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = method.title,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    if (!method.badge.isNullOrBlank()) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEA580C)
                        ) {
                            Text(
                                text = method.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = method.subtitle,
                    fontSize = 12.sp,
                    color = ArenovaSubtitleGray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            PaymentRadioButton(isSelected = isSelected)
        }
    }
}

@Composable
private fun PaymentRadioButton(isSelected: Boolean) {
    if (isSelected) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(Color.Transparent, CircleShape)
                .border(BorderStroke(2.dp, Color(0xFF006837)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(11.dp)
                    .background(Color(0xFF006837), CircleShape)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(Color(0xFFDBEAFE), CircleShape)
        )
    }
}

@Composable
private fun PaymentTopBar(
    onBackClick: () -> Unit,
    onHelpClick: () -> Unit,
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

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Pembayaran",
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
                    onClick = onHelpClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_help_outline),
                        contentDescription = "Bantuan",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(21.dp)
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
private fun PaymentBottomBar(
    selectedMethod: PaymentMethodType,
    onLanjutkanClick: () -> Unit
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Metode: ",
                            fontSize = 13.sp,
                            color = ArenovaDarkText
                        )
                        Text(
                            text = selectedMethod.bottomLabel,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                    }

                    Text(
                        text = "Bebas Biaya Layanan",
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onLanjutkanClick,
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
                        Text(
                            text = "LANJUTKAN",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentMethodScreenPreview() {
    ArenovaTheme {
        PaymentMethodScreen()
    }
}