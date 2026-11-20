package com.example.arenova.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

@Composable
fun RegisterSuccessScreen(
    onLoginClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE8F8F0),
            Color(0xFFF3FAF6),
            Color(0xFFFFFFFF),
            Color(0xFFFFFFFF)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // ==========================================
        // 1. TOP BAR (Help / Question Icon)
        // ==========================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onHelpClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_help_outline),
                    contentDescription = "Bantuan",
                    tint = ArenovaPlaceholderGray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // ==========================================
        // 2. KONTEN TENGAH (Checkmark & Teks Sukses)
        // ==========================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animasi / Ilustrasi Badge Sukses (Concentric Circles)
            SuccessCheckmarkBadge()

            Spacer(modifier = Modifier.height(36.dp))

            // Judul Sukses
            Text(
                text = "Registrasi Berhasil",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subjudul Deskripsi
            Text(
                text = "Akun anda berhasil dibuat. Siap\nuntuk memesan lapangan\nfavoritmu?",
                fontSize = 15.sp,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            // Spasi agar seimbang dengan tombol bawah
            Spacer(modifier = Modifier.height(60.dp))
        }

        // ==========================================
        // 3. BAGIAN BAWAH (Tombol MASUK & Disclaimer)
        // ==========================================
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            // Tombol MASUK (Primary Green)
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = ArenovaPrimaryGreen.copy(alpha = 0.25f),
                        spotColor = ArenovaPrimaryGreen.copy(alpha = 0.4f)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArenovaPrimaryGreen,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "MASUK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Teks Persetujuan Ketentuan Layanan
            Text(
                text = "Dengan masuk, kamu menyetujui Ketentuan Layanan\nArenova",
                fontSize = 11.5.sp,
                color = ArenovaPlaceholderGray,
                lineHeight = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
        }
    }
}

/**
 * Komponen lingkaran konsentris bertingkat dengan centang hijau
 */
@Composable
private fun SuccessCheckmarkBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(130.dp),
        contentAlignment = Alignment.Center
    ) {
        // Lingkaran luar paling lembut
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(
                    color = Color(0xFFE6F9F0),
                    shape = CircleShape
                )
        )
        // Lingkaran tengah putih/sangat terang
        Box(
            modifier = Modifier
                .size(106.dp)
                .background(
                    color = Color(0xFFF3FCF7),
                    shape = CircleShape
                )
        )
        // Lingkaran dalam mint lembut dengan ikon centang
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color = Color(0xFFCEF4E1),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(36.dp)) {
                val strokeWidth = 4.5.dp.toPx()
                val path = Path().apply {
                    moveTo(size.width * 0.20f, size.height * 0.52f)
                    lineTo(size.width * 0.44f, size.height * 0.76f)
                    lineTo(size.width * 0.82f, size.height * 0.26f)
                }
                drawPath(
                    path = path,
                    color = Color(0xFF00B368),
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterSuccessScreenPreview() {
    ArenovaTheme {
        RegisterSuccessScreen()
    }
}
