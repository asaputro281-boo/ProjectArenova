package com.example.arenova.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaErrorRedDark
import com.example.arenova.ui.theme.ArenovaInputBg
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

// Palet warna hijau disamakan persis dengan tombol MASUK (Login) dan DAFTAR (Register)
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

/**
 * Tampilan Lupa Sandi (Forgot Password)
 * Mendukung kondisi normal dan kondisi jika email belum/tidak terdaftar di sistem.
 */
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = onBackClick,
    onRegisterClick: () -> Unit = {},
    onSendCodeClick: (email: String, onNotRegistered: (Boolean) -> Unit) -> Unit = { _, _ -> },
    onProfileClick: () -> Unit = {},
    initialEmail: String = "",
    initialIsEmailNotRegistered: Boolean = false,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf(initialEmail) }
    var isEmailNotRegistered by remember { mutableStateOf(initialIsEmailNotRegistered) }
    var showTopBanner by remember { mutableStateOf(initialIsEmailNotRegistered) }
    var formatErrorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun handleSendCode() {
        focusManager.clearFocus()
        val trimmed = email.trim()
        if (trimmed.isBlank()) {
            formatErrorMessage = "Silakan masukkan alamat email Anda"
            isEmailNotRegistered = false
            showTopBanner = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            formatErrorMessage = "Format email tidak valid"
            isEmailNotRegistered = false
            showTopBanner = false
        } else {
            formatErrorMessage = null
            onSendCodeClick(trimmed) { notRegistered ->
                isEmailNotRegistered = notRegistered
                showTopBanner = notRegistered
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR (Back, Title, Avatar/Profile)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Kembali
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali ke Login",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Judul Screen "Forgot Password"
                Text(
                    text = "Forgot Password",
                    fontSize = 18.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                // Ikon Profil Bulat Hijau di Kanan Atas (Warna sama persis dengan button)
                Box(
                    modifier = Modifier
                        .size(38.dp)
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
                        contentDescription = "Profil",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // ==========================================
            // BANNER PERINGATAN "EMAIL TIDAK TERDAFTAR"
            // ==========================================
            AnimatedVisibility(
                visible = isEmailNotRegistered && showTopBanner,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = ArenovaErrorLightBg
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_circle_error),
                            contentDescription = "Peringatan",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Email tidak terdaftar",
                                color = ArenovaErrorRedDark,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Periksa kembali alamat email akun Anda atau daftar baru.",
                                color = ArenovaErrorRedDark.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }

                        IconButton(
                            onClick = { showTopBanner = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Tutup Banner",
                                tint = ArenovaErrorRedDark,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Format error sederhana jika kosong atau format salah
            if (formatErrorMessage != null && !isEmailNotRegistered) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = ArenovaErrorLightBg
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_circle_error),
                            contentDescription = "Error",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatErrorMessage ?: "",
                            color = ArenovaErrorRedDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isEmailNotRegistered && showTopBanner) 18.dp else 28.dp))

            // ==========================================
            // 2. BADGE ILUSTRASI TENGAH (RESET LOCK & SEARCH)
            // ==========================================
            Box(
                modifier = Modifier.size(92.dp),
                contentAlignment = Alignment.Center
            ) {
                // Lingkaran Luar dengan Efek Gradasi Lembut (Aura)
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFDCFCE7),
                                    Color(0xFFE0F2FE),
                                    Color(0xFFF0FDF4).copy(alpha = 0.2f)
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Lingkaran Dalam Putih Bersih
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .shadow(
                            elevation = 3.dp,
                            shape = CircleShape,
                            ambientColor = RevoraGreenPrimary.copy(alpha = 0.2f),
                            spotColor = RevoraGreenPrimary.copy(alpha = 0.3f)
                        )
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Ikon Panah Melingkar + Gembok di Tengah
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock_repeat),
                        contentDescription = "Reset Sandi",
                        tint = RevoraGreenPrimary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                // Badge Kecil di Pojok Kanan Bawah:
                // Berwarna Oranye jika email tidak terdaftar (pencarian tidak ditemukan),
                // atau Hijau jika normal.
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.BottomEnd)
                        .background(
                            color = if (isEmailNotRegistered) Color(0xFFF59E0B) else ArenovaPrimaryGreen,
                            shape = CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 3. JUDUL & DESKRIPSI
            // ==========================================
            Text(
                text = "Lupa Sandi?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Masukkan email anda dan kami akan\nmengirimkan kode verifikasi untuk mengatur\nulang sandi",
                fontSize = 13.5.sp,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 4. KARTU PUTIH FORM INPUT & TOMBOL
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isEmailNotRegistered) ArenovaErrorRed.copy(alpha = 0.3f) else Color(0xFFF1F5F9)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 20.dp)
                ) {
                    // Header Form
                    if (isEmailNotRegistered) {
                        // Label "Alamat Email" pada kondisi error tidak terdaftar
                        Text(
                            text = "Alamat Email",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                    } else {
                        // Header "Email Terdaftar" & "Akun Arenova" pada kondisi normal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Email Terdaftar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_shield_check),
                                    contentDescription = null,
                                    tint = ArenovaPrimaryGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Akun Arenova",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ArenovaPrimaryGreen
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input Field Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (formatErrorMessage != null) formatErrorMessage = null
                            if (isEmailNotRegistered) {
                                isEmailNotRegistered = false
                                showTopBanner = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Masukkan alamat email anda",
                                color = ArenovaPlaceholderGray,
                                fontSize = 13.5.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                contentDescription = "Email Icon",
                                tint = if (isEmailNotRegistered) ArenovaErrorRed else ArenovaPlaceholderGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = if (email.isNotEmpty()) {
                            {
                                IconButton(onClick = {
                                    email = ""
                                    formatErrorMessage = null
                                    isEmailNotRegistered = false
                                    showTopBanner = false
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cancel_circle),
                                        contentDescription = "Hapus",
                                        tint = if (isEmailNotRegistered) ArenovaErrorRed else ArenovaPlaceholderGray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        } else null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleSendCode() }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ArenovaInputBg,
                            unfocusedContainerColor = ArenovaInputBg,
                            focusedBorderColor = if (isEmailNotRegistered) ArenovaErrorRed else RevoraGreenPrimary,
                            unfocusedBorderColor = if (isEmailNotRegistered) Color(0xFFF87171) else Color.Transparent,
                            focusedTextColor = ArenovaDarkText,
                            unfocusedTextColor = ArenovaDarkText,
                            cursorColor = if (isEmailNotRegistered) ArenovaErrorRed else RevoraGreenPrimary
                        )
                    )

                    // Pesan Keterangan Error di Bawah Input jika Email Tidak Terdaftar
                    if (isEmailNotRegistered) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_circle_info_error),
                                contentDescription = "Peringatan",
                                tint = ArenovaErrorRed,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Alamat email belum terdaftar di sistem Arenova",
                                color = ArenovaErrorRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Kotak Tips / Info Kedaluwarsa Kode jika dalam kondisi normal
                    if (!isEmailNotRegistered) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEFF5FF)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_lightbulb),
                                    contentDescription = "Info",
                                    tint = Color(0xFFF59E0B),
                                    modifier = Modifier
                                        .padding(top = 1.dp)
                                        .size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Kode 6 digit akan kedaluwarsa dalam 10 menit setelah dikirim ke inbox atau folder spam Anda.",
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF334155),
                                    lineHeight = 16.5.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // ==========================================
                    // TOMBOL "KIRIM KODE ->"
                    // ==========================================
                    Button(
                        onClick = { handleSendCode() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
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
                                    text = "KIRIM KODE",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_right),
                                    contentDescription = "Kirim",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 5. KARTU "BELUM PUNYA AKUN ARENOVA? DAFTAR"
            // (Hanya muncul jika email belum terdaftar)
            // ==========================================
            AnimatedVisibility(
                visible = isEmailNotRegistered,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFFEFF5FF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ikon ID Card / Badge Registrasi
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        color = Color(0xFFDBEAFE),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_badge_id),
                                    contentDescription = null,
                                    tint = Color(0xFF0D9488),
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Belum punya akun Arenova?",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ArenovaDarkText
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Daftar sekarang untuk mulai booking lapangan.",
                                    fontSize = 12.sp,
                                    color = ArenovaSubtitleGray,
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Tombol "Daftar"
                            Button(
                                onClick = onRegisterClick,
                                shape = RoundedCornerShape(18.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD1FAE5),
                                    contentColor = RevoraGreenPrimary
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text(
                                    text = "Daftar",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 6. LINK KEMBALI KE LOGIN
            // ==========================================
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onLoginClick() }
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Kembali ke ",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArenovaDarkText
                )
                Text(
                    text = "Login",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = RevoraGreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenNotRegisteredPreview() {
    ArenovaTheme {
        ForgotPasswordScreen(
            initialEmail = "pengguna.lama@domain.com",
            initialIsEmailNotRegistered = true
        )
    }
}
