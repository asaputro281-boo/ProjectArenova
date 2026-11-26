package com.example.arenova.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaBorderGray
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaInputBg
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

private val RevoraGreenStart = Color(0xFF43A047)
private val RevoraGreenEnd = Color(0xFF2E7D32)
private val RevoraGreenPrimary = Color(0xFF388E3C)

private val RevoraGreenGradient = Brush.horizontalGradient(
    colors = listOf(RevoraGreenStart, RevoraGreenEnd)
)

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    userName: String = "Nama Pengguna",
    userEmail: String = "user@email.com",
    initialShowSuccess: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    onSaveClick: (name: String, email: String) -> Unit = { _, _ -> },
    onLogoutClick: () -> Unit = {},
    onTabSelected: (DashboardTab) -> Unit = {}
) {
    var nameState by remember { mutableStateOf(userName) }
    var emailState by remember { mutableStateOf(userEmail) }
    var showSuccessBanner by remember { mutableStateOf(initialShowSuccess) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isNameError = errorMessage == "Nama tidak boleh kosong" && nameState.isBlank()
    val isEmailError = errorMessage == "Email tidak boleh kosong" && emailState.isBlank()

    val scrollState = rememberScrollState()

    LaunchedEffect(showSuccessBanner, errorMessage) {
        if (showSuccessBanner || errorMessage != null) {
            scrollState.animateScrollTo(0)
        }
    }

    BackHandler {
        onBackClick()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding()
            .imePadding(),
        containerColor = ArenovaScreenBg,
        bottomBar = {
            ArenovaBottomNavigation(
                selectedTab = DashboardTab.PROFIL,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR: LOGO PROFIL & AKSI KANAN
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onBackClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                brush = RevoraGreenGradient,
                                shape = RoundedCornerShape(11.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile_tab),
                            contentDescription = "Logo Profil",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Profil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaDarkText
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { onNotificationClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bell),
                            contentDescription = "Notifikasi",
                            tint = ArenovaDarkText,
                            modifier = Modifier.size(22.dp)
                        )
                    }

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
                            ) { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_user),
                            contentDescription = "Kembali ke Profil",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // 2. BANNER NOTIFIKASI ERROR VALIDASI
            // ==========================================
            AnimatedVisibility(
                visible = errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = ArenovaErrorLightBg,
                    border = BorderStroke(1.dp, ArenovaErrorRed.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(ArenovaErrorRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = errorMessage.orEmpty(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = ArenovaErrorRed
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Tutup Peringatan",
                            tint = ArenovaErrorRed,
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .clickable { errorMessage = null }
                        )
                    }
                }
            }

            // ==========================================
            // 3. BANNER NOTIFIKASI SUKSES
            // ==========================================
            AnimatedVisibility(
                visible = showSuccessBanner && errorMessage == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_check_circle),
                                contentDescription = "Sukses",
                                tint = RevoraGreenPrimary,
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "Profil berhasil diperbarui",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RevoraGreenEnd
                            )
                        }

                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = "Tutup Notifikasi",
                            tint = RevoraGreenEnd,
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .clickable { showSuccessBanner = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 4. FOTO PROFIL DENGAN BADGE KAMERA
            // ==========================================
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.size(100.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_badminton1),
                    contentDescription = "Foto Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color.White, CircleShape)
                        .shadow(elevation = 3.dp, shape = CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onChangePhotoClick() }
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            brush = RevoraGreenGradient,
                            shape = CircleShape
                        )
                        .border(2.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onChangePhotoClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "Ubah Foto Profil",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (nameState.isNotBlank()) nameState else "Nama Pengguna",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 5. KARTU FORM EDIT PROFIL
            // ==========================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                shadowElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // --- 5.1 Nama Lengkap ---
                    Text(
                        text = "Nama Lengkap",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ArenovaSubtitleGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    BasicTextField(
                        value = nameState,
                        onValueChange = {
                            nameState = it
                            if (isNameError && it.isNotBlank()) errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(ArenovaInputBg, RoundedCornerShape(12.dp))
                            .border(
                                BorderStroke(1.dp, if (isNameError) ArenovaErrorRed else ArenovaBorderGray),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = ArenovaDarkText
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(RevoraGreenPrimary),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (nameState.isEmpty()) {
                                    Text(
                                        text = "Nama Pengguna",
                                        fontSize = 14.5.sp,
                                        color = ArenovaSubtitleGray
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- 5.2 Email ---
                    Text(
                        text = "Email",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ArenovaSubtitleGray
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    BasicTextField(
                        value = emailState,
                        onValueChange = {
                            emailState = it
                            if (isEmailError && it.isNotBlank()) errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .background(ArenovaInputBg, RoundedCornerShape(12.dp))
                            .border(
                                BorderStroke(1.dp, if (isEmailError) ArenovaErrorRed else ArenovaBorderGray),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = ArenovaDarkText
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(RevoraGreenPrimary),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (emailState.isEmpty()) {
                                    Text(
                                        text = "user@email.com",
                                        fontSize = 14.5.sp,
                                        color = ArenovaSubtitleGray
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 6. TOMBOL SIMPAN PERUBAHAN
            // ==========================================
            Button(
                onClick = {
                    val trimmedName = nameState.trim()
                    val trimmedEmail = emailState.trim()

                    when {
                        trimmedName.isBlank() -> {
                            errorMessage = "Nama tidak boleh kosong"
                            showSuccessBanner = false
                        }
                        trimmedEmail.isBlank() -> {
                            errorMessage = "Email tidak boleh kosong"
                            showSuccessBanner = false
                        }
                        else -> {
                            errorMessage = null
                            showSuccessBanner = true
                            onSaveClick(trimmedName, trimmedEmail)
                        }
                    }
                },
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp)
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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_save),
                            contentDescription = "Simpan",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SIMPAN PERUBAHAN",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 0.6.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==========================================
            // 7. TOMBOL LOGOUT
            // ==========================================
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ArenovaErrorLightBg,
                    contentColor = ArenovaErrorRed
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Keluar",
                        tint = ArenovaErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LOGOUT",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaErrorRed,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 8. FOOTER VERSI APLIKASI
            // ==========================================
            Text(
                text = "Versi 2.4.0 • Arenova Sports Hub",
                fontSize = 12.sp,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileScreenPreview() {
    ArenovaTheme {
        EditProfileScreen(
            userName = "Nama Pengguna",
            userEmail = "user@email.com",
            initialShowSuccess = false
        )
    }
}