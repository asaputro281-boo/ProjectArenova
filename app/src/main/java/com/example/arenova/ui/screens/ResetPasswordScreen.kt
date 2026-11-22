package com.example.arenova.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaBodyText
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaErrorRedDark
import com.example.arenova.ui.theme.ArenovaInputBg
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

// Palet warna hijau disamakan persis dengan tampilan sebelumnya (Login, Register, OTP Verification)
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

/**
 * Tampilan Buat Sandi Baru (Reset / Create New Password Screen)
 * Muncul setelah verifikasi OTP berhasil untuk mengatur kata sandi baru.
 */
@Composable
fun ResetPasswordScreen(
    onBackClick: () -> Unit = {},
    onPasswordResetSuccess: (newPassword: String) -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // 4 Kriteria Keamanan Sandi
    val isLengthMet = newPassword.length >= 8
    val isUppercaseMet = newPassword.any { it.isUpperCase() }
    val isDigitMet = newPassword.any { it.isDigit() }
    val isMatchMet = newPassword.isNotEmpty() && (newPassword == confirmPassword)

    // Perhitungan kekuatan sandi untuk 4 bar indikator
    val criteriaCount = listOf(isLengthMet, isUppercaseMet, isDigitMet, isMatchMet).count { it }
    val strengthBarColor = when (criteriaCount) {
        1 -> Color(0xFFEF4444) // Merah
        2 -> Color(0xFFF59E0B) // Oranye
        3 -> Color(0xFF84CC16) // Kuning Kehijauan
        4 -> RevoraGreenPrimary // Hijau Sempurna
        else -> Color(0xFFE2E8F0)
    }

    fun handleSavePassword() {
        focusManager.clearFocus()
        when {
            newPassword.isBlank() -> {
                errorMessage = "Silakan masukkan sandi baru Anda"
            }
            !isLengthMet -> {
                errorMessage = "Sandi baru minimal harus 8 karakter"
            }
            !isUppercaseMet -> {
                errorMessage = "Sandi baru harus mengandung minimal 1 huruf besar"
            }
            !isDigitMet -> {
                errorMessage = "Sandi baru harus mengandung minimal 1 angka (0-9)"
            }
            confirmPassword.isBlank() -> {
                errorMessage = "Silakan masukkan konfirmasi sandi baru"
            }
            newPassword != confirmPassword -> {
                errorMessage = "Konfirmasi sandi tidak cocok dengan sandi baru"
            }
            else -> {
                errorMessage = null
                onPasswordResetSuccess(newPassword)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
        ) {
            // ==========================================
            // HEADER BAR: BACK ARROW, TITLE, USER ICON
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Back
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Kembali",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Judul Screen "Otp Verification"
                Text(
                    text = "Otp Verification",
                    fontSize = 18.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                // Ikon Profil Bulat Hijau di Kanan Atas
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
            // FLOATING ERROR BANNER (JIKA ADA INPUT INVALID)
            // ==========================================
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
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
                            contentDescription = "Peringatan",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = ArenovaErrorRedDark,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { errorMessage = null },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Tutup",
                                tint = ArenovaErrorRedDark,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // ILUSTRASI IKON TENGAH DENGAN BADGE ORANYE
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(104.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Lingkaran luar dengan ring biru muda lembut
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .background(
                                color = Color(0xFFF0F9FF),
                                shape = CircleShape
                            )
                            .border(
                                width = 4.dp,
                                color = Color(0xFFE0F2FE),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock_repeat),
                            contentDescription = null,
                            tint = Color(0xFF0D9488),
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    // Badge bulat oranye dengan perisai putih di sudut kanan bawah
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp)
                            .background(
                                color = Color(0xFFEA580C),
                                shape = CircleShape
                            )
                            .border(
                                width = 2.5.dp,
                                color = Color.White,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shield_security),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // JUDUL & SUBTITLE
            // ==========================================
            Text(
                text = "Buat Sandi Baru",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sandi baru Anda harus berbeda dari sandi\nsebelumnya untuk keamanan akun Arenova.",
                fontSize = 13.5.sp,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // KARTU FORM INPUT SANDI BARU (WHITE CARD)
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(22.dp),
                color = Color.White,
                shadowElevation = 1.5.dp,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // --- INPUT 1: SANDI BARU ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sandi Baru",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Text(
                            text = "Minimal 8 karakter",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            if (errorMessage != null) errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Masukkan sandi baru",
                                color = ArenovaPlaceholderGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                tint = ArenovaSubtitleGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isNewPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                                    ),
                                    contentDescription = if (isNewPasswordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                                    tint = ArenovaSubtitleGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = ArenovaInputBg.copy(alpha = 0.5f),
                            focusedBorderColor = RevoraGreenPrimary,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            cursorColor = RevoraGreenPrimary,
                            focusedTextColor = ArenovaDarkText,
                            unfocusedTextColor = ArenovaDarkText
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4 Bar Indikator Kekuatan Sandi
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (i in 0 until 4) {
                            val isActive = newPassword.isNotEmpty() && (i < criteriaCount)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .background(
                                        color = if (isActive) strengthBarColor else Color(0xFFE2E8F0),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- INPUT 2: KONFIRMASI SANDI BARU ---
                    Text(
                        text = "Konfirmasi Sandi Baru",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            if (errorMessage != null) errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Ulangi sandi baru",
                                color = ArenovaPlaceholderGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_verified_badge),
                                contentDescription = null,
                                tint = ArenovaSubtitleGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isConfirmPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                                    ),
                                    contentDescription = if (isConfirmPasswordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                                    tint = ArenovaSubtitleGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { handleSavePassword() }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = ArenovaInputBg.copy(alpha = 0.5f),
                            focusedBorderColor = RevoraGreenPrimary,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            cursorColor = RevoraGreenPrimary,
                            focusedTextColor = ArenovaDarkText,
                            unfocusedTextColor = ArenovaDarkText
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- BOX KETENTUAN KEAMANAN SANDI (2x2 GRID) ---
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFFEEF2FF)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_shield_security),
                                    contentDescription = null,
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ketentuan keamanan sandi:",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ArenovaDarkText
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Baris 1: 8+ Karakter & Huruf Besar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RequirementPill(
                                    text = "8+ Karakter",
                                    isMet = isLengthMet,
                                    modifier = Modifier.weight(1f)
                                )
                                RequirementPill(
                                    text = "Huruf Besar",
                                    isMet = isUppercaseMet,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Baris 2: Angka (0–9) & Sandi Cocok
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RequirementPill(
                                    text = "Angka (0–9)",
                                    isMet = isDigitMet,
                                    modifier = Modifier.weight(1f)
                                )
                                RequirementPill(
                                    text = "Sandi Cocok",
                                    isMet = isMatchMet,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ==========================================
                    // TOMBOL "SIMPAN SANDI →"
                    // WARNA TOMBOL SESUAI TAMPILAN SEBELUMNYA (REVORA GREEN GRADIENT)
                    // ==========================================
                    Button(
                        onClick = { handleSavePassword() },
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
                                    text = "SIMPAN SANDI",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                                    contentDescription = "Simpan",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // FOOTER: ENKRIPSI SANDI & STATUS SESI
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_security),
                        contentDescription = null,
                        tint = RevoraGreenPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Enkripsi sandi 256–bit aman berstandar Arenova",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = ArenovaDarkText
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(
                                color = RevoraGreenPrimary,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sesi terlindungi • Aktivitas instan tersinkron",
                        fontSize = 11.5.sp,
                        color = ArenovaSubtitleGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Chip / Pill untuk item ketentuan keamanan sandi
 */
@Composable
private fun RequirementPill(
    text: String,
    isMet: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isMet) RevoraGreenPrimary.copy(alpha = 0.35f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indikator Lingkaran / Radio
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (isMet) RevoraGreenPrimary else Color.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isMet) RevoraGreenPrimary else Color(0xFF94A3B8),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isMet) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_circle),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (isMet) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isMet) ArenovaDarkText else ArenovaSubtitleGray,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ArenovaTheme {
        ResetPasswordScreen()
    }
}
