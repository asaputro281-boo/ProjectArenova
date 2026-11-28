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

// Data model untuk item Kebijakan Privasi
data class PrivacyPolicyItem(
    val iconRes: Int,
    val title: String,
    val description: String
)

private val privacyPolicyList = listOf(
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_badge_id,
        title = "Data yang Kami Kumpulkan",
        description = "Nama, email, nomor HP, riwayat booking, dan data pembayaran yang diperlukan untuk transaksi."
    ),
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_calendar,
        title = "Penggunaan Data",
        description = "Data digunakan untuk memproses booking, pembayaran, dan memberi pengingat jadwal bermain."
    ),
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_lock,
        title = "Keamanan Data",
        description = "Kami menjaga data anda dengan perlindungan yang wajar dan membatasi akses hanya pada pihak berwenang."
    ),
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_tag_privacy,
        title = "Berbagi Data",
        description = "Data tidak dijual. Data hanya dibagikan seperlunya kepada penyedia pembayaran dan pengelola lapangan."
    ),
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_user_privacy,
        title = "Hak Pengguna",
        description = "Anda dapat memperbarui data pribadi kapan saja melalui halaman Profil."
    ),
    PrivacyPolicyItem(
        iconRes = R.drawable.ic_mail,
        title = "Hubungi Kami",
        description = "Pertanyaan tentang privasi dapat dikirim ke support@arenova.id."
    )
)

// Warna spesifik layar Kebijakan Privasi sesuai tema Revora
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

private val PrivacyBannerBg = RevoraGreenSoftBg
private val PrivacyIconBg = RevoraGreenSoftBg
private val PrivacyIconTint = RevoraGreenPrimary
private val PrivacyCardBorder = Color(0xFFF1F5F9)

@Composable
fun PrivacyPolicyScreen(
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
                    text = "Kebijakan Privasi",
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
            // 2. HERO BANNER: PRIVASI ANDA PENTING BAGI KAMI
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = PrivacyBannerBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // White circular badge with shield checkmark
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shield_badge_green),
                            contentDescription = "Privasi Terlindungi",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Privasi anda penting bagi kami",
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Terakhir diperbarui: 28 November 2026",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 3. MAIN CARD: DAFTAR KEBIJAKAN PRIVASI
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
                border = BorderStroke(1.dp, PrivacyCardBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    privacyPolicyList.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Rounded squircle container for icon
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        color = PrivacyIconBg,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = item.iconRes),
                                    contentDescription = item.title,
                                    tint = PrivacyIconTint,
                                    modifier = Modifier.size(20.dp)
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
                                Spacer(modifier = Modifier.height(4.dp))
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
                        if (index < privacyPolicyList.size - 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .padding(horizontal = 18.dp)
                                    .background(PrivacyCardBorder)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    ArenovaTheme {
        PrivacyPolicyScreen()
    }
}
