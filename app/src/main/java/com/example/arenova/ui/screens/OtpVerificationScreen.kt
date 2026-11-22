package com.example.arenova.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
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

// Palet warna hijau disamakan persis dengan seluruh tombol tampilan sebelumnya
private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

/**
 * Tampilan Verifikasi Kode OTP (Otp Verification)
 * Mendukung pengisian manual digit demi digit, salin-tempel (copy-paste),
 * navigasi fokus otomatis, penanda kotak aktif dengan kursor berkedip,
 * serta tombol aksi cepat "Tempel Kode" dan "Hapus".
 */
@Composable
fun OtpVerificationScreen(
    email: String = "user@email.com",
    onBackClick: () -> Unit = {},
    onChangeEmailClick: () -> Unit = onBackClick,
    onVerificationSuccess: (code: String) -> Unit = {},
    onResendCodeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    initialOtp: String = "",
    initialIsCodeIncorrect: Boolean = false,
    correctCode: String = "123456",
    modifier: Modifier = Modifier
) {
    val cleanInitialOtp = initialOtp.filter { it.isDigit() }.take(6)
    var otpValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = cleanInitialOtp,
                selection = TextRange(cleanInitialOtp.length)
            )
        )
    }
    var isCodeIncorrect by remember { mutableStateOf(initialIsCodeIncorrect) }
    var showTopBanner by remember { mutableStateOf(initialIsCodeIncorrect) }
    var remainingAttempts by remember { mutableIntStateOf(3) }
    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    // Animasi kedipan kursor pada kotak OTP yang sedang aktif
    val infiniteTransition = rememberInfiniteTransition(label = "OtpCursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorAlpha"
    )

    fun handleVerify() {
        focusManager.clearFocus()
        keyboardController?.hide()
        val currentOtp = otpValue.text.trim()

        if (currentOtp.length < 6) {
            isCodeIncorrect = true
            showTopBanner = true
            Toast.makeText(context, "Silakan masukkan 6 digit kode verifikasi", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentOtp == correctCode) {
            // Kode Benar -> lanjut ke tampilan berikutnya!
            isCodeIncorrect = false
            showTopBanner = false
            onVerificationSuccess(currentOtp)
        } else {
            // Kode Salah / Kedaluwarsa -> aktifkan tampilan error kode salah
            isCodeIncorrect = true
            showTopBanner = true
            if (remainingAttempts > 1) {
                remainingAttempts -= 1
            }
        }
    }

    // Fungsi untuk menyalin dan menempelkan kode langsung dari clipboard
    fun pasteFromClipboard() {
        val clipText = clipboardManager.getText()?.text ?: ""
        val digits = clipText.filter { it.isDigit() }.take(6)
        if (digits.isNotEmpty()) {
            otpValue = TextFieldValue(
                text = digits,
                selection = TextRange(digits.length)
            )
            isCodeIncorrect = false
            showTopBanner = false
            Toast.makeText(context, "Kode berhasil ditempel: $digits", Toast.LENGTH_SHORT).show()
            if (digits.length == 6) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        } else {
            Toast.makeText(context, "Tidak ada kode angka di clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    // Buka keyboard dan fokus otomatis secara halus saat layar terbuka
    LaunchedEffect(Unit) {
        delay(250)
        focusRequester.requestFocus()
        keyboardController?.show()
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
                        contentDescription = "Kembali",
                        tint = ArenovaDarkText,
                        modifier = Modifier.size(22.dp)
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
            // FLOATING BANNER "KODE SALAH ATAU KEDALUWARSA"
            // ==========================================
            AnimatedVisibility(
                visible = isCodeIncorrect && showTopBanner,
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
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Garis strip merah tua di sisi kiri
                        Box(
                            modifier = Modifier
                                .width(5.dp)
                                .fillMaxHeight()
                                .background(
                                    color = Color(0xFFB91C1C),
                                    shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                                )
                        )

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
                                    text = "Kode salah atau sudah kedaluwarsa",
                                    color = ArenovaErrorRedDark,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Periksa kembali kode di inbox email Anda atau kirim ulang kode baru.",
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
            }

            Spacer(modifier = Modifier.height(if (isCodeIncorrect && showTopBanner) 16.dp else 24.dp))

            // ==========================================
            // 2. KARTU PUTIH UTAMA (FORM OTP VERIFIKASI)
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Badge Ikon Amplop Hijau dengan Bulatan Merah "!"
                    Box(
                        modifier = Modifier.size(78.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(78.dp)
                                .background(
                                    color = Color(0xFFD1FAE5),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail_notification),
                                contentDescription = null,
                                tint = ArenovaPrimaryGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Badge bulat merah tanda seru jika kode salah
                        if (isCodeIncorrect) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0xFFDC2626), CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Judul "Verifikasi Kode"
                    Text(
                        text = "Verifikasi Kode",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Deskripsi
                    Text(
                        text = "Kami telah mengirim kode 6 digit ke email anda",
                        fontSize = 13.sp,
                        color = ArenovaSubtitleGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Chip Alamat Email
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFEFF5FF),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE).copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                contentDescription = null,
                                tint = ArenovaSubtitleGray,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = email,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ==========================================
                    // 6 KOTAK KODE OTP (INTERAKTIF & BISA DIKLIK)
                    // ==========================================
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // BasicTextField yang mengelola input, IME keyboard, copy-paste, dan backspace
                        BasicTextField(
                            value = otpValue,
                            onValueChange = { newValue ->
                                // Filter hanya digit angka (mendukung salin-tempel kode yang ada spasi / strip)
                                val digits = newValue.text.filter { it.isDigit() }.take(6)
                                otpValue = TextFieldValue(
                                    text = digits,
                                    selection = TextRange(digits.length)
                                )
                                if (isCodeIncorrect) {
                                    isCodeIncorrect = false
                                    showTopBanner = false
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { handleVerify() }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { isFocused = it.isFocused },
                            textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
                            cursorBrush = SolidColor(Color.Transparent),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Tampilan 6 Kotak Angka
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val currentText = otpValue.text
                                        for (i in 0 until 6) {
                                            val char = currentText.getOrNull(i)?.toString() ?: ""
                                            val isFilled = char.isNotEmpty()
                                            // Kotak aktif adalah kotak kosong pertama yang menunggu input saat fokus
                                            val isCurrentActiveBox = isFocused && (
                                                (i == currentText.length) || (currentText.length == 6 && i == 5)
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(56.dp)
                                                    .background(
                                                        color = when {
                                                            isCodeIncorrect -> Color(0xFFFFF1F2)
                                                            isCurrentActiveBox -> Color.White
                                                            isFilled -> Color.White
                                                            else -> ArenovaInputBg
                                                        },
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .border(
                                                        width = if (isCurrentActiveBox || isCodeIncorrect) 1.8.dp else if (isFilled) 1.2.dp else 1.dp,
                                                        color = when {
                                                            isCodeIncorrect -> Color(0xFFEF4444)
                                                            isCurrentActiveBox -> RevoraGreenPrimary
                                                            isFilled -> RevoraGreenPrimary.copy(alpha = 0.6f)
                                                            else -> Color(0xFFE2E8F0)
                                                        },
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable(
                                                        interactionSource = remember { MutableInteractionSource() },
                                                        indication = null
                                                    ) {
                                                        focusRequester.requestFocus()
                                                        keyboardController?.show()
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isFilled) {
                                                    Text(
                                                        text = char,
                                                        fontSize = 22.sp,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        color = if (isCodeIncorrect) Color(0xFFB91C1C) else ArenovaDarkText,
                                                        textAlign = TextAlign.Center
                                                    )
                                                } else if (isCurrentActiveBox) {
                                                    // Indikator kursor berkedip di kotak aktif
                                                    Box(
                                                        modifier = Modifier
                                                            .width(2.dp)
                                                            .height(24.dp)
                                                            .background(RevoraGreenPrimary.copy(alpha = cursorAlpha))
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Memanggil innerTextField agar sistem input teks Android & clipboard berfungsi sempurna
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .alpha(0f)
                                    ) {
                                        innerTextField()
                                    }
                                }
                            }
                        )
                    }

                    // Tombol Aksi Cepat: "Tempel Kode" & "Hapus"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tombol Tempel dari Clipboard
                        Surface(
                            onClick = { pasteFromClipboard() },
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clipboard),
                                    contentDescription = "Tempel Kode",
                                    tint = RevoraGreenPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Tempel Kode",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = RevoraGreenPrimary
                                )
                            }
                        }

                        // Tombol Hapus jika kotak sudah terisi
                        if (otpValue.text.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                onClick = {
                                    otpValue = TextFieldValue("", selection = TextRange(0))
                                    isCodeIncorrect = false
                                    showTopBanner = false
                                    focusRequester.requestFocus()
                                    keyboardController?.show()
                                },
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_close),
                                        contentDescription = "Hapus",
                                        tint = ArenovaSubtitleGray,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Hapus",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ArenovaSubtitleGray
                                    )
                                }
                            }
                        }
                    }

                    // Keterangan Pesan Error di Bawah Kotak OTP
                    if (isCodeIncorrect) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_circle_info_error),
                                contentDescription = "Peringatan",
                                tint = ArenovaErrorRed,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Kode verifikasi tidak sesuai. Sisa kesempatan ${remainingAttempts}x.",
                                color = ArenovaErrorRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Link Kirim Ulang Kode
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Tidak menerima kode? ",
                            fontSize = 13.sp,
                            color = ArenovaSubtitleGray
                        )
                        Row(
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    otpValue = TextFieldValue("", selection = TextRange(0))
                                    isCodeIncorrect = false
                                    showTopBanner = false
                                    focusRequester.requestFocus()
                                    keyboardController?.show()
                                    onResendCodeClick()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kirim Ulang",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = RevoraGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "Kirim Ulang",
                                tint = RevoraGreenPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ==========================================
                    // TOMBOL "VERIFIKASI ✓"
                    // ==========================================
                    Button(
                        onClick = { handleVerify() },
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
                                    text = "VERIFIKASI",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_check_circle),
                                    contentDescription = "Verifikasi",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Tombol / Link "Ganti Email"
                    Text(
                        text = "Ganti Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArenovaDarkText,
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onChangeEmailClick() }
                            .padding(vertical = 4.dp, horizontal = 12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 3. KARTU "BUTUH LAPANGAN MENDESAK?"
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFEFF5FF)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ikon Raket Oranye dalam Lingkaran Kuning Muda
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = Color(0xFFFEF3C7),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_racket),
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Butuh lapangan mendesak?",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Selesaikan verifikasi untuk amankan jadwal slot favorit Anda hari ini.",
                            fontSize = 12.sp,
                            color = ArenovaSubtitleGray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Kondisi Siap Input (Normal)")
@Composable
fun OtpVerificationScreenPreview() {
    ArenovaTheme {
        OtpVerificationScreen(
            initialOtp = "",
            initialIsCodeIncorrect = false
        )
    }
}

@Preview(showBackground = true, name = "Kondisi Kode Salah (Error)")
@Composable
fun OtpVerificationScreenErrorPreview() {
    ArenovaTheme {
        OtpVerificationScreen(
            initialOtp = "482910",
            initialIsCodeIncorrect = true
        )
    }
}
