package com.example.arenova.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaErrorRedDark
import com.example.arenova.ui.theme.ArenovaPlaceholderGray
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: (nama: String, email: String, pass: String) -> Unit = { _, _, _ -> },
    onSupportClick: () -> Unit = {},
    initialShowError: Boolean = true,
    modifier: Modifier = Modifier
) {
    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiPasswordVisible by remember { mutableStateOf(false) }

    var showErrorBanner by remember { mutableStateOf(initialShowError) }
    var errorMessage by remember { mutableStateOf("Semua kolom wajib diisi") }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun handleRegister() {
        focusManager.clearFocus()
        if (namaLengkap.isBlank() || email.isBlank() || password.isBlank() || konfirmasiPassword.isBlank()) {
            errorMessage = "Semua kolom wajib diisi"
            showErrorBanner = true
        } else if (password != konfirmasiPassword) {
            errorMessage = "Konfirmasi password tidak cocok"
            showErrorBanner = true
        } else {
            showErrorBanner = false
            onRegisterSuccess(namaLengkap, email, password)
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
            // 1. TOP APP BAR (Back, Title, Support Icon)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tombol Back
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Kembali ke Login",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Judul Screen "Registrasi"
                Text(
                    text = "Registrasi",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )

                // Tombol Support
                IconButton(
                    onClick = { onSupportClick() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_support),
                        contentDescription = "Bantuan",
                        tint = ArenovaSubtitleGray,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 2. BANNER CARD "Buat Akun Arenova"
            // ==========================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFE8F1FD),
                                Color(0xFFE2ECFB)
                            )
                        )
                    )
            ) {
                // Ornamen Garis Lapangan / Watermark di Kanan
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val w = size.width
                    val h = size.height
                    val circleCenter = Offset(w * 0.84f, h * 0.48f)
                    val radius = w * 0.26f
                    val strokeStyle = Stroke(width = 2.dp.toPx())
                    val waterMarkColor = Color(0xFF00B368).copy(alpha = 0.16f)

                    drawCircle(
                        color = waterMarkColor,
                        radius = radius,
                        center = circleCenter,
                        style = strokeStyle
                    )
                    drawCircle(
                        color = waterMarkColor,
                        radius = radius * 0.55f,
                        center = circleCenter,
                        style = strokeStyle
                    )
                    // Garis crosshair
                    drawLine(
                        color = waterMarkColor,
                        start = Offset(circleCenter.x, 0f),
                        end = Offset(circleCenter.x, h),
                        strokeWidth = 2.dp.toPx()
                    )
                    drawLine(
                        color = waterMarkColor,
                        start = Offset(w * 0.60f, circleCenter.y),
                        end = Offset(w, circleCenter.y),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Konten Logo & Teks Banner
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo Squircle Kecil "A"
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = ArenovaPrimaryGreen,
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Buat Akun Arenova",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Satu langkah lagi untuk mulai bermain",
                            fontSize = 12.5.sp,
                            color = ArenovaSubtitleGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ==========================================
            // 3. FORM INPUT FIELDS
            // ==========================================

            // --- 3.1 NAMA LENGKAP ---
            FormLabel(
                label = "Nama Lengkap",
                isRequired = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = namaLengkap,
                onValueChange = {
                    namaLengkap = it
                    if (showErrorBanner) showErrorBanner = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(14.dp)),
                placeholder = {
                    Text(
                        text = "Masukkan nama lengkap Anda",
                        color = ArenovaPlaceholderGray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "User Icon",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (namaLengkap.isBlank() && showErrorBanner) {
                        Text(
                            text = "!",
                            color = ArenovaErrorRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ArenovaPrimaryGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = ArenovaDarkText,
                    unfocusedTextColor = ArenovaDarkText,
                    cursorColor = ArenovaPrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // --- 3.2 EMAIL ---
            FormLabel(
                label = "Email",
                isRequired = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (showErrorBanner) showErrorBanner = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(14.dp)),
                placeholder = {
                    Text(
                        text = "contoh@domain.com",
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
                trailingIcon = {
                    if (email.isBlank() && showErrorBanner) {
                        Text(
                            text = "!",
                            color = ArenovaErrorRed,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ArenovaPrimaryGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = ArenovaDarkText,
                    unfocusedTextColor = ArenovaDarkText,
                    cursorColor = ArenovaPrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // --- 3.3 PASSWORD ---
            FormLabel(
                label = "Password",
                isRequired = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (showErrorBanner) showErrorBanner = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(14.dp)),
                placeholder = {
                    Text(
                        text = "Minimal 8 karakter",
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
                                id = if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                            ),
                            contentDescription = if (passwordVisible) "Sembunyikan password" else "Lihat password",
                            tint = ArenovaSubtitleGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ArenovaPrimaryGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = ArenovaDarkText,
                    unfocusedTextColor = ArenovaDarkText,
                    cursorColor = ArenovaPrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // --- 3.4 KONFIRMASI PASSWORD ---
            FormLabel(
                label = "Konfirmasi Password",
                isRequired = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = konfirmasiPassword,
                onValueChange = {
                    konfirmasiPassword = it
                    if (showErrorBanner) showErrorBanner = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(14.dp)),
                placeholder = {
                    Text(
                        text = "Ulangi password Anda",
                        color = ArenovaPlaceholderGray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_lock_repeat),
                        contentDescription = "Confirm Password Icon",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { konfirmasiPasswordVisible = !konfirmasiPasswordVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (konfirmasiPasswordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                            ),
                            contentDescription = if (konfirmasiPasswordVisible) "Sembunyikan password" else "Lihat password",
                            tint = ArenovaSubtitleGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (konfirmasiPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        handleRegister()
                    }
                ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ArenovaPrimaryGreen,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = ArenovaDarkText,
                    unfocusedTextColor = ArenovaDarkText,
                    cursorColor = ArenovaPrimaryGreen
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 4. TOMBOL DAFTAR (PRIMARY GREEN)
            // ==========================================
            Button(
                onClick = { handleRegister() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArenovaPrimaryGreen,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "DAFTAR",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // ==========================================
            // 5. ERROR BANNER BAWAH TOMBOL DAFTAR
            // ==========================================
            AnimatedVisibility(
                visible = showErrorBanner,
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
                                onClick = { showErrorBanner = false },
                                modifier = Modifier.size(22.dp)
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 6. FOOTER "Sudah punya akun? Masuk"
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sudah punya akun? ",
                    fontSize = 13.5.sp,
                    color = ArenovaDarkText
                )
                Text(
                    text = "Masuk",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF007A3D),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onLoginClick()
                        }
                )
            }
        }
    }
}

/**
 * Komponen Label Form dengan indikator wajib merah di kanan
 */
@Composable
private fun FormLabel(
    label: String,
    isRequired: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArenovaDarkText
            )
            if (isRequired) {
                Text(
                    text = " *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaErrorRed
                )
            }
        }

        if (isRequired) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle_error),
                    contentDescription = null,
                    tint = ArenovaErrorRed,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Wajib",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ArenovaErrorRed
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    ArenovaTheme {
        RegisterScreen()
    }
}
