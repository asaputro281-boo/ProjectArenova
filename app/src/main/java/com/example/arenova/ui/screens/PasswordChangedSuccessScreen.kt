package com.example.arenova.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

// Palet warna hijau disesuaikan persis dengan tampilan sebelumnya (Login, Register, OTP Verification, Reset Password)
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

/**
 * Tampilan Konfirmasi Sandi Berhasil Diubah (Password Changed Success Screen)
 * Ditampilkan setelah pengguna menekan "SIMPAN SANDI" pada ResetPasswordScreen.
 */
@Composable
fun PasswordChangedSuccessScreen(
    onLoginClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFCFD))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ==========================================
            // 1. TOP HEADER BAR: ARENOVA SAFEPASS BADGE
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEFF6FF).copy(alpha = 0.85f),
                    border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_safepass_key),
                            contentDescription = "SafePass",
                            tint = Color(0xFF0D9488),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ARENOVA SAFEPASS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // 2. ILUSTRASI TENGAH (CONCENTRIC EMBLEM DENGAN CENTANG)
            // ==========================================
            SuccessConcentricEmblem(
                modifier = Modifier.size(170.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ==========================================
            // 3. JUDUL UTAMA & DESKRIPSI
            // ==========================================
            Text(
                text = "Sandi Berhasil Diubah",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Silakan masuk dengan sandi baru anda untuk\nkembali menjadwalkan lapangan favorit.",
                fontSize = 13.5.sp,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 30.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 4. BADGE STATUS KEAMANAN
            // ==========================================
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFF1F5F9).copy(alpha = 0.9f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_check),
                        contentDescription = "Keamanan",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Keamanan Diperbarui",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(3.5.dp)
                            .background(Color(0xFF94A3B8), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hari ini, 14:14",
                        fontSize = 12.sp,
                        color = ArenovaSubtitleGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 5. KARTU PROMOSI VENUE (PADEL & BADMINTON)
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                shadowElevation = 1.5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Wadah Ikon Ungu/Biru Muda
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                color = Color(0xFFEEF2FF),
                                shape = RoundedCornerShape(13.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_venue_court),
                            contentDescription = "Venue",
                            tint = Color(0xFF0D9488),
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Padel & Badminton Menunggumu",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "3 venue populer punya slot kosong sore ini",
                            fontSize = 11.5.sp,
                            color = ArenovaSubtitleGray
                        )
                    }
                }
            }

            // Memberikan jarak fleksibel sebelum tombol bawah
            Spacer(modifier = Modifier.weight(1f, fill = false))
            Spacer(modifier = Modifier.height(48.dp))

            // ==========================================
            // 6. TOMBOL "MASUK →"
            // WARNA DISESUAIKAN PERSIS DENGAN TOMBOL-TOMBOL SEBELUMNYA
            // ==========================================
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 24.dp)
                    .shadow(
                        elevation = 4.dp,
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
                            text = "MASUK",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_forward),
                            contentDescription = "Masuk",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 7. FOOTER LINK: "Perlu bantuan? Pusat Bantuan Arenova"
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perlu bantuan? ",
                    fontSize = 12.5.sp,
                    color = ArenovaSubtitleGray
                )
                Text(
                    text = "Pusat Bantuan Arenova",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RevoraGreenPrimary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onHelpClick()
                    }
                )
            }
        }
    }
}

/**
 * Komponen emblem konsentris khusus dengan aksen oranye dan bintang emerald,
 * cincin mint bergradasi, dan lingkaran hijau gelap dengan centang putih tebal di pusatnya.
 */
@Composable
private fun SuccessConcentricEmblem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            val outerRadius = size.minDimension * 0.44f
            val midRadius = size.minDimension * 0.35f
            val whiteGapRadius = size.minDimension * 0.26f
            val centerRadius = size.minDimension * 0.19f

            // 1. Cincin luar paling lembut (light mint halo)
            drawCircle(
                color = Color(0xFFD1FAE5).copy(alpha = 0.55f),
                radius = outerRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 1.8.dp.toPx())
            )

            // 2. Aksen Chevron Oranye di kanan atas (~ 35 derajat dari vertikal)
            val chevronAngle = Math.toRadians(-40.0)
            val chevronX = cx + (outerRadius * Math.cos(chevronAngle)).toFloat()
            val chevronY = cy + (outerRadius * Math.sin(chevronAngle)).toFloat()

            val orangePath = Path().apply {
                val sizePx = 6.5.dp.toPx()
                moveTo(chevronX - sizePx * 0.7f, chevronY - sizePx)
                lineTo(chevronX + sizePx * 0.6f, chevronY)
                lineTo(chevronX - sizePx * 0.7f, chevronY + sizePx)
            }
            drawPath(
                path = orangePath,
                color = Color(0xFFF97316),
                style = Stroke(
                    width = 2.8.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 3. Aksen Bintang Kemilau (Sparkle Star) di kiri bawah (~ 145 derajat)
            val starAngle = Math.toRadians(145.0)
            val starX = cx + (outerRadius * Math.cos(starAngle)).toFloat()
            val starY = cy + (outerRadius * Math.sin(starAngle)).toFloat()

            // Bintang utama (4-point sparkle)
            val sparklePath = Path().apply {
                val r1 = 8.dp.toPx()
                val r2 = 2.2.dp.toPx()
                for (i in 0 until 8) {
                    val angle = Math.toRadians((i * 45.0) - 90.0)
                    val r = if (i % 2 == 0) r1 else r2
                    val px = starX + (r * Math.cos(angle)).toFloat()
                    val py = starY + (r * Math.sin(angle)).toFloat()
                    if (i == 0) moveTo(px, py) else lineTo(px, py)
                }
                close()
            }
            drawPath(
                path = sparklePath,
                color = Color(0xFF10B981)
            )

            // Titik kemilau kecil di samping bintang
            drawCircle(
                color = Color(0xFF34D399),
                radius = 1.8.dp.toPx(),
                center = Offset(starX + 7.dp.toPx(), starY - 6.dp.toPx())
            )

            // 4. Cincin lingkaran tengah mint lembut (mid ring)
            drawCircle(
                color = Color(0xFFCCFBF1),
                radius = midRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 11.dp.toPx())
            )

            // 5. Cincin putih pemisah antara mint ring dan lingkaran tengah
            drawCircle(
                color = Color.White,
                radius = whiteGapRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 6.dp.toPx())
            )

            // 6. Lingkaran pusat hijau tua emerald
            drawCircle(
                color = Color(0xFF007A4D),
                radius = centerRadius,
                center = Offset(cx, cy)
            )

            // 7. Centang Putih di Pusat Lingkaran
            val checkWidth = 3.6.dp.toPx()
            val checkPath = Path().apply {
                moveTo(cx - 7.5.dp.toPx(), cy + 0.5.dp.toPx())
                lineTo(cx - 2.dp.toPx(), cy + 6.dp.toPx())
                lineTo(cx + 9.5.dp.toPx(), cy - 5.5.dp.toPx())
            }
            drawPath(
                path = checkPath,
                color = Color.White,
                style = Stroke(
                    width = checkWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PasswordChangedSuccessScreenPreview() {
    ArenovaTheme {
        PasswordChangedSuccessScreen()
    }
}
