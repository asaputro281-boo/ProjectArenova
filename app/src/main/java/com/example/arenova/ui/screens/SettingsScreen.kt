package com.example.arenova.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arenova.R
import com.example.arenova.ui.theme.ArenovaDarkText
import com.example.arenova.ui.theme.ArenovaErrorRed
import com.example.arenova.ui.theme.ArenovaPrimaryGreen
import com.example.arenova.ui.theme.ArenovaScreenBg
import com.example.arenova.ui.theme.ArenovaSubtitleGray
import com.example.arenova.ui.theme.ArenovaTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isBookingReminderEnabled by remember { mutableStateOf(true) }
    var isPromoInfoEnabled by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showVerifiedDialog by remember { mutableStateOf(false) }

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // 1. TOP APP BAR (Back Button, Title, Profile Avatar)
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
                    text = "Pengaturan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ArenovaDarkText,
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFF006941),
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
            // 2. BANNER KARTU: PROFIL OLAHRAGA TERVERIFIKASI
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showVerifiedDialog = true },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFEEF2FF)
            ) {
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
                                color = Color(0xFFDCFCE7),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_shield_check),
                            contentDescription = "Terverifikasi",
                            tint = Color(0xFF00B368),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Profil Olahraga Terverifikasi",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = ArenovaDarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Preferensi & keamanan Arenova ID kamu",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = ArenovaSubtitleGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF00B368)
                    ) {
                        Text(
                            text = "Aktif",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 3. SECTION: AKUN
            // ==========================================
            SectionHeader(title = "AKUN")

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
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
                    ) { onChangePasswordClick() },
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
                            .size(42.dp)
                            .background(
                                color = Color(0xFFEFF6FF),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_lock_repeat),
                            contentDescription = "Ubah Sandi",
                            tint = Color(0xFF0D9488),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = "Ubah Sandi",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ArenovaDarkText,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_right),
                        contentDescription = "Buka Ubah Sandi",
                        tint = ArenovaSubtitleGray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 4. SECTION: NOTIFIKASI
            // ==========================================
            SectionHeader(title = "NOTIFIKASI")

            Spacer(modifier = Modifier.height(8.dp))

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
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Item 1: Pengingat Booking
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
                                    color = Color(0xFFEFF6FF),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_alarm_clock),
                                contentDescription = "Pengingat Booking",
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Pengingat Booking",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Alarm 2 jam sebelum kick-off/sparring",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = ArenovaSubtitleGray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Switch(
                            checked = isBookingReminderEnabled,
                            onCheckedChange = { isChecked ->
                                isBookingReminderEnabled = isChecked
                                Toast.makeText(
                                    context,
                                    if (isChecked) "Pengingat booking diaktifkan" else "Pengingat booking dinonaktifkan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF00B368),
                                checkedBorderColor = Color.Transparent,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFE2E8F0),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }

                    // Divider horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFFF1F5F9))
                    )

                    // Item 2: Info dan Promo
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
                                    color = Color(0xFFEFF6FF),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_megaphone),
                                contentDescription = "Info dan Promo",
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Info dan Promo",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = ArenovaDarkText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Diskon sewa lapangan & turnamen",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = ArenovaSubtitleGray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Switch(
                            checked = isPromoInfoEnabled,
                            onCheckedChange = { isChecked ->
                                isPromoInfoEnabled = isChecked
                                Toast.makeText(
                                    context,
                                    if (isChecked) "Notifikasi info dan promo diaktifkan" else "Notifikasi info dan promo dinonaktifkan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF00B368),
                                checkedBorderColor = Color.Transparent,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFFCBD5E1),
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==========================================
            // 5. SECTION: LAINNYA
            // ==========================================
            SectionHeader(title = "LAINNYA")

            Spacer(modifier = Modifier.height(8.dp))

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
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Item 1: Bantuan
                    SettingsNavRow(
                        iconRes = R.drawable.ic_help_square,
                        title = "Bantuan",
                        onClick = { showHelpDialog = true }
                    )

                    // Divider horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFFF1F5F9))
                    )

                    // Item 2: Syarat dan Ketentuan
                    SettingsNavRow(
                        iconRes = R.drawable.ic_document_terms,
                        title = "Syarat dan Ketentuan",
                        onClick = { showTermsDialog = true }
                    )

                    // Divider horizontal
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 16.dp)
                            .background(Color(0xFFF1F5F9))
                    )

                    // Item 3: Kebijakan Privasi
                    SettingsNavRow(
                        iconRes = R.drawable.ic_shield_privacy,
                        title = "Kebijakan Privasi",
                        onClick = { showPrivacyDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==========================================
            // 6. TOMBOL KELUAR AKUN
            // ==========================================
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showLogoutDialog = true },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF1F5FD)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Keluar Akun",
                        tint = ArenovaErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Keluar Akun",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ArenovaErrorRed
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ==========================================
            // 7. FOOTER APLIKASI
            // ==========================================
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(ArenovaPrimaryGreen, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Arenova v1.0.0",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF334155)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Sports Venue Hub • Jakarta Selatan",
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = ArenovaSubtitleGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // ==========================================
    // DIALOGS & MODALS
    // ==========================================

    // 1. Dialog Konfirmasi Keluar Akun
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Keluar Akun",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ArenovaDarkText
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin keluar dari akun Arenova Anda?",
                    fontSize = 14.sp,
                    color = ArenovaSubtitleGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenovaErrorRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Keluar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Batal", color = ArenovaDarkText)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 2. Dialog Bantuan
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_help_square),
                        contentDescription = null,
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Pusat Bantuan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ArenovaDarkText
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Butuh bantuan terkait pemesanan atau akun Arenova Anda?",
                        fontSize = 13.5.sp,
                        color = ArenovaDarkText
                    )
                    Text(
                        text = "• Email: cs@arenova.id\n• WhatsApp: +62 812-3456-7890\n• Jam Operasional: 07:00 - 23:00 WIB",
                        fontSize = 13.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 20.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showHelpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenovaPrimaryGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tutup", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 3. Dialog Syarat dan Ketentuan
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_document_terms),
                        contentDescription = null,
                        tint = Color(0xFF00B368),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Syarat dan Ketentuan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ArenovaDarkText
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "1. Pemesanan Lapangan\nSetiap reservasi lapangan yang telah dibayar lunas dinyatakan sah dan tidak dapat dipindahtangankan tanpa konfirmasi pihak Arenova.",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "2. Pembatalan & Jadwal Ulang\nPembatalan dapat dilakukan maksimal 6 jam sebelum jadwal kick-off/main. Refund akan diproses sesuai ketentuan sistem Arenova.",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3. Tata Tertib Fasilitas\nPengguna wajib menggunakan sepatu dan perlengkapan olahraga yang sesuai serta menjaga kebersihan fasilitas venue.",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTermsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenovaPrimaryGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Mengerti", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 4. Dialog Kebijakan Privasi
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_privacy),
                        contentDescription = null,
                        tint = Color(0xFF00B368),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kebijakan Privasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ArenovaDarkText
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Kami di Arenova menghargai dan melindungi privasi data pribadi Anda. Data yang kami kumpulkan meliputi informasi profil, riwayat booking, dan preferensi olahraga.",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Data Anda dienkripsi secara aman dan hanya digunakan untuk meningkatkan pengalaman serta keamanan layanan Arenova.",
                        fontSize = 12.5.sp,
                        color = ArenovaSubtitleGray,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenovaPrimaryGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Tutup", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // 5. Dialog Profil Olahraga Terverifikasi
    if (showVerifiedDialog) {
        AlertDialog(
            onDismissRequest = { showVerifiedDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shield_check),
                        contentDescription = null,
                        tint = Color(0xFF00B368),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Arenova ID Terverifikasi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ArenovaDarkText
                    )
                }
            },
            text = {
                Text(
                    text = "Akun Anda berstatus Aktif dan Terverifikasi. Nikmati kemudahan booking lapangan prioritas, perlindungan transaksi, dan notifikasi jadwal olahraga tepat waktu.",
                    fontSize = 13.5.sp,
                    color = ArenovaSubtitleGray,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showVerifiedDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ArenovaPrimaryGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Oke", color = Color.White)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ArenovaSubtitleGray,
            letterSpacing = 0.6.sp
        )
    }
}

@Composable
private fun SettingsNavRow(
    iconRes: Int,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = Color(0xFFEFF6FF),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = ArenovaDarkText,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = "Buka $title",
            tint = ArenovaSubtitleGray,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    ArenovaTheme {
        SettingsScreen()
    }
}
