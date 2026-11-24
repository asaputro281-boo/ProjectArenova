package com.example.arenova.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
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
import com.example.arenova.ui.theme.ArenovaBorderGray
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaErrorRedDark
import com.example.arenova.ui.theme.ArenovaInputBg
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

// Palet warna hijau disesuaikan dengan referensi kartu
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)
private val RevoraGreenSoftBg = Color(0xFFE8F5E9)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

@Composable
fun LoginScreen(
    onLoginClick: (email: String, pass: String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onDismissError: () -> Unit = {},
    isError: Boolean? = null,
    errorMessage: String = "Email atau password salah",
    isLoading: Boolean = false,
    initialShowError: Boolean = false,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var internalError by remember { mutableStateOf(initialShowError) }
    var showTopBanner by remember { mutableStateOf(initialShowError) }

    val hasError = isError ?: internalError

    LaunchedEffect(isError) {
        if (isError != null) {
            internalError = isError
            if (isError) {
                showTopBanner = true
            }
        }
    }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun performLogin() {
        if (isLoading) return
        focusManager.clearFocus()
        if (email.isBlank() || password.isBlank()) {
            internalError = true
            showTopBanner = true
        }
        onLoginClick(email, password)
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
            // ==========================================
            // BAGIAN LOGIN GAGAL: FLOATING BANNER ATAS
            // ==========================================
            AnimatedVisibility(
                visible = hasError && showTopBanner,
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
                            contentDescription = "Error Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = errorMessage,
                            color = ArenovaErrorRedDark,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { showTopBanner = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Tutup Peringatan",
                                tint = ArenovaErrorRedDark,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (hasError && showTopBanner) 16.dp else 42.dp))

            // ==========================================
            // 1. LOGO ARENOVA BARU
            // ==========================================
            Image(
                painter = painterResource(id = R.drawable.logo_arenova),
                contentDescription = "Logo Arenova",
                modifier = Modifier
                    .size(76.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = RevoraGreenPrimary.copy(alpha = 0.3f),
                        spotColor = RevoraGreenPrimary.copy(alpha = 0.4f)
                    )
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 2. JUDUL APLIKASI & TAGLINE
            // ==========================================
            Text(
                text = "Arenova",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "SEWA LAPANGAN OLAHRAGA JADI MUDAH",
                fontSize = 15.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArenovaSubtitleGray,
                letterSpacing = 0.8.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ==========================================
            // 3. KARTU PUTIH FORM LOGIN
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // --- LABEL EMAIL + "PERIKSA KEMBALI" JIKA LOGIN GAGAL ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Email",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ArenovaDarkText
                        )
                        if (hasError) {
                            Text(
                                text = "Periksa kembali",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = ArenovaErrorRed
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- INPUT FIELD EMAIL ---
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (internalError) {
                                internalError = false
                                onDismissError()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Masukkan email Anda",
                                color = ArenovaPlaceholderGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gmail),
                                contentDescription = "Email Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        trailingIcon = if (email.isNotEmpty() && hasError) {
                            {
                                IconButton(
                                    onClick = {
                                        email = ""
                                        if (internalError) {
                                            internalError = false
                                            onDismissError()
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cancel_circle),
                                        contentDescription = "Hapus Email",
                                        tint = ArenovaErrorRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        } else null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ArenovaInputBg,
                            unfocusedContainerColor = ArenovaInputBg,
                            focusedBorderColor = if (hasError) ArenovaErrorRed else RevoraGreenPrimary,
                            unfocusedBorderColor = if (hasError) ArenovaErrorRed.copy(alpha = 0.5f) else Color.Transparent,
                            focusedTextColor = ArenovaDarkText,
                            unfocusedTextColor = ArenovaDarkText,
                            cursorColor = RevoraGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- LABEL PASSWORD ---
                    Text(
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- INPUT FIELD PASSWORD ---
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (internalError) {
                                internalError = false
                                onDismissError()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Masukkan kata sandi",
                                color = ArenovaPlaceholderGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = "Password Icon",
                                tint = ArenovaDarkText,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (passwordVisible) R.drawable.ic_eye_off else R.drawable.ic_eye
                                    ),
                                    contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Lihat sandi",
                                    tint = ArenovaSubtitleGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                performLogin()
                            }
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ArenovaInputBg,
                            unfocusedContainerColor = ArenovaInputBg,
                            focusedBorderColor = if (hasError) ArenovaErrorRed else RevoraGreenPrimary,
                            unfocusedBorderColor = if (hasError) ArenovaErrorRed.copy(alpha = 0.5f) else Color.Transparent,
                            focusedTextColor = ArenovaDarkText,
                            unfocusedTextColor = ArenovaDarkText,
                            cursorColor = RevoraGreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    // --- TOMBOL MASUK (DENGAN GRADASI HIJAU REFERENSI) ---
                    Button(
                        onClick = { performLogin() },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White,
                            disabledContainerColor = RevoraGreenPrimary.copy(alpha = 0.6f),
                            disabledContentColor = Color.White
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = RevoraGreenGradient),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Text(
                                    text = "MASUK",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }

                    // ==========================================
                    // BAGIAN LOGIN GAGAL: BANNER BAWAH TOMBOL MASUK
                    // ==========================================
                    AnimatedVisibility(
                        visible = hasError,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                color = ArenovaErrorLightBg
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_warning_triangle),
                                        contentDescription = "Peringatan Login Gagal",
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = errorMessage,
                                        color = ArenovaErrorRedDark,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // --- LINK LUPA PASSWORD? ---
                    Text(
                        text = "Lupa password?",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArenovaDarkText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onForgotPasswordClick()
                            }
                            .padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- DIVIDER PEMISAH "ATAU" ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = ArenovaBorderGray
                        )
                        Text(
                            text = "ATAU",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ArenovaSubtitleGray,
                            letterSpacing = 0.6.sp,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = ArenovaBorderGray
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- TOMBOL DAFTAR AKUN BARU ---
                    Surface(
                        onClick = { onRegisterClick() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = RevoraGreenSoftBg
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_person_add),
                                contentDescription = "Daftar Akun Baru",
                                tint = RevoraGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Daftar Akun Baru",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = RevoraGreenPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ==========================================
            // 4. FOOTER "Pemesanan aman & instant konfirmasi 24/7"
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_shield_check),
                    contentDescription = "Shield Check",
                    tint = RevoraGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Pemesanan aman & instant konfirmasi 24/7",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArenovaSubtitleGray
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    ArenovaTheme {
        LoginScreen()
    }
}

@Preview(name = "Preview Login Gagal", showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenErrorPreview() {
    ArenovaTheme {
        LoginScreen(
            initialShowError = true
        )
    }
}