package com.example.arenova.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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

// Model data bantuan FAQ
data class FaqItem(
    val question: String,
    val answer: String
)

private val faqList = listOf(
    FaqItem(
        question = "Bagaimana cara memesan lapangan?",
        answer = "Pilih kategori dan lapangan, tentukan tanggal dan jam, lalu lanjutkan ke pembayaran."
    ),
    FaqItem(
        question = "Berapa lama batas waktu pembayaran?",
        answer = "Pembayaran harus diselesaikan maksimal 2 jam setelah booking, baik Lunas maupun DP 50%."
    ),
    FaqItem(
        question = "Bagaimana cara membatalkan booking?",
        answer = "Buka menu Riwayat, pilih booking yang aktif, lalu pilih Batalkan Booking di halaman Detail Booking."
    ),
    FaqItem(
        question = "Bagaimana dengan sisa pembayaran DP 50%?",
        answer = "Sisa 50% dibayar langsung di lokasi sebelum bermain. Tunjukkan kode booking ke kasir."
    )
)

// Warna spesifik layar bantuan sesuai tema Revora
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

private val HelpBannerGreenBg = RevoraGreenSoftBg
private val HelpIconGreen = RevoraGreenPrimary
private val HelpSectionGray = Color(0xFF64748B)
private val HelpCardBorder = Color(0xFFF1F5F9)
private val HelpContactIconBg = RevoraGreenSoftBg

@Composable
fun HelpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label disalin ke papan klip", Toast.LENGTH_SHORT).show()
    }

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
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR (Back, Title "Bantuan", Profile Avatar)
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
                    text = "Bantuan",
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

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 2. KARTU PUSAT BANTUAN (Header Card)
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = HelpBannerGreenBg
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikon tanda tanya dengan background putih lingkaran
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_question_circle),
                            contentDescription = "Pusat Bantuan",
                            tint = HelpIconGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pusat Bantuan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Jawaban untuk pertanyaan yang sering ditanyakan",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF475569),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ==========================================
            // 3. SECTION: PERTANYAAN UMUM
            // ==========================================
            Text(
                text = "PERTANYAAN UMUM",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = HelpSectionGray,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Daftar FAQ Cards
            faqList.forEach { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 1.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = Color.Black.copy(alpha = 0.02f),
                            spotColor = Color.Black.copy(alpha = 0.04f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, HelpCardBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = item.question,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText,
                            lineHeight = 21.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.answer,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Normal,
                            color = ArenovaSubtitleGray,
                            lineHeight = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 4. SECTION: HUBUNGI KAMI
            // ==========================================
            Text(
                text = "HUBUNGI KAMI",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = HelpSectionGray,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Kartu Hubungi Kami
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 1.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.02f),
                        spotColor = Color.Black.copy(alpha = 0.04f)
                    ),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, HelpCardBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Item 1: Email
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                try {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:support@arenova.id")
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    copyToClipboard("support@arenova.id", "Email Bantuan")
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = HelpContactIconBg,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail_bantuan),
                                contentDescription = "Email",
                                tint = HelpIconGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Email",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = ArenovaSubtitleGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "support@arenova.id",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                        }
                    }

                    // Divider horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(HelpCardBorder)
                    )

                    // Item 2: WhatsApp
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                try {
                                    val waNumber = "6281234567890"
                                    val url = "https://api.whatsapp.com/send?phone=$waNumber&text=Halo%20Arenova%20Support,%20saya%20butuh%20bantuan"
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse(url)
                                    }
                                    context.startActivity(intent)
                                } catch (_: Exception) {
                                    copyToClipboard("+62 812-3456-7890", "Nomor WhatsApp")
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = HelpContactIconBg,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_chat_bubble_lines),
                                contentDescription = "WhatsApp",
                                tint = HelpIconGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "WhatsApp",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = ArenovaSubtitleGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "+62 812–3456–7890",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                        }
                    }

                    // Divider horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(HelpCardBorder)
                    )

                    // Item 3: Jam Operasional
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = HelpContactIconBg,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock_bantuan),
                                contentDescription = "Jam Operasional",
                                tint = HelpIconGreen,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Jam Operasional",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = ArenovaSubtitleGray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Setiap hari, 08.00–21.00 WIB",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpScreenPreview() {
    ArenovaTheme {
        HelpScreen()
    }
}
