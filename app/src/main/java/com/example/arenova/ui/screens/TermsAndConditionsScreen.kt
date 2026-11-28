package com.example.arenova.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

// Data model untuk item Syarat dan Ketentuan
data class TermsItem(
    val number: Int,
    val title: String,
    val description: String
)

private val termsList = listOf(
    TermsItem(
        number = 1,
        title = "Penggunaan Aplikasi",
        description = "Arenova digunakan untuk memesan lapangan olahraga. Pengguna wajib memberikan data yang benar saat mendaftar."
    ),
    TermsItem(
        number = 2,
        title = "Booking dan Jadwal",
        description = "Slot jadwal dikunci setelah booking dibuat dan dilepas jika pembayaran tidak selesai tepat waktu."
    ),
    TermsItem(
        number = 3,
        title = "Pembayaran",
        description = "Pembayaran Lunas atau DP 50% harus diselesaikan maksimal 2 jam setelah booking. Sisa DP dibayar di lokasi."
    ),
    TermsItem(
        number = 4,
        title = "Pembatalan",
        description = "Pembatalan dilakukan melalui halaman Detail Booking dan tidak dapat dibatalkan kembali setelah dikonfirmasi."
    ),
    TermsItem(
        number = 5,
        title = "Tanggung Jawab Pengguna",
        description = "Pengguna menjaga kerapian dan fasilitas lapangan selama bermain."
    ),
    TermsItem(
        number = 6,
        title = "Perubahan Ketentuan",
        description = "Arenova dapat memperbarui ketentuan ini sewaktu-waktu dan akan memberi tahu pengguna melalui aplikasi."
    )
)

// Warna spesifik layar Syarat dan Ketentuan
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

private val TermsBadgeBg = Color(0xFFE0F2FE)
private val TermsBadgeText = Color(0xFF0284C7)
private val TermsCardBorder = Color(0xFFF1F5F9)
private val TermsFooterIconBg = Color(0xFFE0E7FF)
private val TermsUpdateGreen = RevoraGreenPrimary

@Composable
fun TermsAndConditionsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding(),
        containerColor = ArenovaScreenBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR (Back Button, Title, Profile Avatar)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onBackClick() },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Syarat dan Ketentuan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )

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
                        contentDescription = "Akun Pengguna",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 2. SUBHEADER: INFO TERAKHIR DIPERBARUI & DESKRIPSI
            // ==========================================
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_history_update),
                        contentDescription = null,
                        tint = TermsUpdateGreen,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Terakhir diperbarui: 28 November 2026",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF334155)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Dengan menggunakan Arenova, Anda menyetujui ketentuan penggunaan fasilitas dan layanan berikut.",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF334155),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 3. KARTU UTAMA: DAFTAR SYARAT DAN KETENTUAN (1-6)
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(18.dp),
                        ambientColor = Color.Black.copy(alpha = 0.02f),
                        spotColor = Color.Black.copy(alpha = 0.04f)
                    ),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = BorderStroke(1.dp, TermsCardBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    termsList.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Badge nomor melingkar
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = TermsBadgeBg,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.number.toString(),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TermsBadgeText
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArenovaDarkText
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = ArenovaSubtitleGray,
                                    lineHeight = 19.sp
                                )
                            }
                        }

                        // Garis pemisah antar item kecuali item terakhir
                        if (index < termsList.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(horizontal = 18.dp)
                                    .background(TermsCardBorder)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // ==========================================
            // 4. FOOTER: SHIELD BADGE & ECOSYSTEM COPYRIGHT
            // ==========================================
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = TermsFooterIconBg,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_badge_green),
                        contentDescription = "Keamanan Arenova",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Arenova Sport Ecosystem © 2026",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    ArenovaTheme {
        TermsAndConditionsScreen()
    }
}
