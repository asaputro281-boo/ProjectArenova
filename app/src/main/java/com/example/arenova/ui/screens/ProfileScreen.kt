package com.example.arenova.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorLightBg
import com.example.arenova.ui.theme.ArenovaErrorRed
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
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userName: String = "Angga Dwi Saputro",
    userEmail: String = "user@email.com",
    onBackClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onChangePhotoClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onBookingHistoryClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onTabSelected: (DashboardTab) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ArenovaScreenBg)
            .statusBarsPadding(),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                            contentDescription = "Akun Pengguna",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==========================================
            // 2. FOTO PROFIL DENGAN TOMBOL KAMERA
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
                        ) { onEditProfileClick() }
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
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = "Ubah Foto Profil",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = userName.ifBlank { "Angga Dwi Saputro" },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = ArenovaDarkText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = userEmail.ifBlank { "user@email.com" },
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ==========================================
            // 3. JUDUL SECTION "AKUN & PREFERENSI"
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "AKUN & PREFERENSI",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ArenovaSubtitleGray,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ==========================================
            // 4. DAFTAR MENU KARTU
            // ==========================================
            ProfileMenuCard(
                iconRes = R.drawable.ic_badge_id,
                iconTint = RevoraGreenPrimary,
                iconBgColor = Color(0xFFE0E7FF),
                title = "Edit Profil",
                subtitle = "Ubah nama, kontak, & preferensi cabang",
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuCard(
                iconRes = R.drawable.ic_receipt,
                iconTint = Color(0xFFF59E0B),
                iconBgColor = Color(0xFFE0E7FF),
                title = "Riwayat Booking",
                subtitle = "Jadwal lapangan & bukti pembayaran",
                badgeText = "1 Aktif",
                onClick = onBookingHistoryClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuCard(
                iconRes = R.drawable.ic_settings,
                iconTint = Color(0xFF475569),
                iconBgColor = Color(0xFFE2E8F0),
                title = "Pengaturan",
                subtitle = "Notifikasi, bahasa, keamanan, & bantuan",
                onClick = onSettingsClick
            )

            Spacer(modifier = Modifier.height(26.dp))

            // ==========================================
            // 5. TOMBOL LOGOUT
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

            Spacer(modifier = Modifier.height(18.dp))

            // ==========================================
            // 6. FOOTER VERSI APLIKASI
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

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onHelpClick: () -> Unit = {}
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
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
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
                    text = "Pengaturan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "DUKUNGAN",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = ArenovaSubtitleGray,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileMenuCard(
                iconRes = R.drawable.ic_question_circle,
                iconTint = Color(0xFF00A86B),
                iconBgColor = Color(0xFFE2EFE7),
                title = "Bantuan",
                subtitle = "Pusat tanya jawab & hubungi kami",
                onClick = onHelpClick
            )
        }
    }
}

@Composable
private fun ProfileMenuCard(
    iconRes: Int,
    iconTint: Color,
    iconBgColor: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.02f),
                spotColor = Color.Black.copy(alpha = 0.04f)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = iconBgColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = ArenovaSubtitleGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RevoraGreenPrimary,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = "Buka $title",
                tint = ArenovaSubtitleGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ArenovaTheme {
        ProfileScreen(
            userName = "Angga Dwi Saputro",
            userEmail = "user@email.com"
        )
    }
}