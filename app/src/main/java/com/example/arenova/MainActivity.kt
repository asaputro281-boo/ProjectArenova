package com.example.arenova

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.arenova.ui.screens.ArenovaSplashScreen
import com.example.arenova.ui.screens.BadmintonListScreen
import com.example.arenova.ui.screens.DashboardScreen
import com.example.arenova.ui.screens.DashboardTab
import com.example.arenova.ui.screens.ForgotPasswordScreen
import com.example.arenova.ui.screens.LoginScreen
import com.example.arenova.ui.screens.OtpVerificationScreen
import com.example.arenova.ui.screens.PasswordChangedSuccessScreen
import com.example.arenova.ui.screens.RegisterScreen
import com.example.arenova.ui.screens.RegisterSuccessScreen
import com.example.arenova.ui.screens.ResetPasswordScreen
import com.example.arenova.ui.theme.ArenovaTheme

enum class ArenovaScreen {
    SPLASH,
    LOGIN,
    REGISTER,
    REGISTER_SUCCESS,
    FORGOT_PASSWORD,
    OTP_VERIFICATION,
    RESET_PASSWORD,
    PASSWORD_CHANGED_SUCCESS,
    DASHBOARD,
    BADMINTON_LIST
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArenovaTheme {
                val context = LocalContext.current
                var currentScreen by remember { mutableStateOf(ArenovaScreen.SPLASH) }
                var currentUserName by remember { mutableStateOf("Pengguna") }
                var registeredEmail by remember { mutableStateOf("") }
                var registeredPassword by remember { mutableStateOf("") }
                var resetEmailTarget by remember { mutableStateOf("user@email.com") }
                var isLoginFailed by remember { mutableStateOf(false) }

                Crossfade(
                    targetState = currentScreen,
                    animationSpec = tween(durationMillis = 400),
                    label = "AppScreenTransition"
                ) { screen ->
                    when (screen) {
                        // 1. SPLASH SCREEN
                        ArenovaScreen.SPLASH -> {
                            ArenovaSplashScreen(
                                onNavigateNext = {
                                    currentScreen = ArenovaScreen.LOGIN
                                }
                            )
                        }

                        // 2. LOGIN SCREEN
                        ArenovaScreen.LOGIN -> {
                            LoginScreen(
                                isError = isLoginFailed,
                                onDismissError = { isLoginFailed = false },
                                onLoginClick = { email, pass ->
                                    val trimmedEmail = email.trim()
                                    // Validasi: cocok dengan akun yang didaftarkan atau akun demo
                                    val isValidRegistered = registeredEmail.isNotBlank() &&
                                            trimmedEmail.equals(registeredEmail.trim(), ignoreCase = true) &&
                                            pass == registeredPassword
                                    val isDemoAdmin = trimmedEmail.equals("admin@arenova.id", ignoreCase = true) &&
                                            (pass == "123456" || pass == "admin123")
                                    val isDemoUser = trimmedEmail.equals("user@arenova.id", ignoreCase = true) &&
                                            pass == "123456"
                                    val isQuickPass = pass == "123456" && trimmedEmail.contains("@")

                                    if (email.isBlank() || pass.isBlank() ||
                                        (!isValidRegistered && !isDemoAdmin && !isDemoUser && !isQuickPass)
                                    ) {
                                        // LOGIN GAGAL: Aktifkan tampilan banner & penanda error
                                        isLoginFailed = true
                                    } else {
                                        // LOGIN BERHASIL
                                        isLoginFailed = false
                                        if (currentUserName == "Pengguna") {
                                            val extracted = trimmedEmail.substringBefore("@")
                                                .replace(".", " ")
                                                .split(" ")
                                                .joinToString(" ") { word ->
                                                    word.replaceFirstChar { char -> char.uppercase() }
                                                }
                                            if (extracted.isNotBlank()) {
                                                currentUserName = extracted
                                            }
                                        }

                                        Toast.makeText(
                                            context,
                                            "Berhasil masuk! Selamat datang, $currentUserName",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // Navigasi ke Dashboard
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    }
                                },
                                onForgotPasswordClick = {
                                    isLoginFailed = false
                                    // Navigasi ke Tampilan Lupa Sandi
                                    currentScreen = ArenovaScreen.FORGOT_PASSWORD
                                },
                                onRegisterClick = {
                                    isLoginFailed = false
                                    // Navigasi ke Register Screen
                                    currentScreen = ArenovaScreen.REGISTER
                                }
                            )
                        }

                        // 3. REGISTER SCREEN (Pembuatan Akun)
                        ArenovaScreen.REGISTER -> {
                            RegisterScreen(
                                onBackClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onLoginClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onRegisterSuccess = { nama, email, pass ->
                                    registeredEmail = email
                                    registeredPassword = pass
                                    if (nama.isNotBlank()) {
                                        currentUserName = nama
                                    }
                                    isLoginFailed = false
                                    // Berhasil buat akun -> langsung ke Layar Registrasi Sukses
                                    currentScreen = ArenovaScreen.REGISTER_SUCCESS
                                },
                                onSupportClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Bantuan Arenova: Hubungi cs@arenova.id",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 4. REGISTER SUCCESS SCREEN (Pembuatan Sukses)
                        ArenovaScreen.REGISTER_SUCCESS -> {
                            RegisterSuccessScreen(
                                onLoginClick = {
                                    // Dari layar registrasi sukses -> langsung ke Dashboard Menu
                                    Toast.makeText(
                                        context,
                                        "Selamat datang di Arenova, $currentUserName!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    currentScreen = ArenovaScreen.DASHBOARD
                                },
                                onHelpClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Bantuan Arenova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 5. FORGOT PASSWORD SCREEN (Lupa Sandi)
                        ArenovaScreen.FORGOT_PASSWORD -> {
                            ForgotPasswordScreen(
                                onBackClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onLoginClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onRegisterClick = {
                                    currentScreen = ArenovaScreen.REGISTER
                                },
                                onSendCodeClick = { emailSent, onNotRegistered ->
                                    val trimmed = emailSent.trim()
                                    val isRegistered = registeredEmail.isNotBlank() &&
                                            trimmed.equals(registeredEmail.trim(), ignoreCase = true)
                                    val isDemoAdmin = trimmed.equals("admin@arenova.id", ignoreCase = true)
                                    val isDemoUser = trimmed.equals("user@arenova.id", ignoreCase = true)
                                    val isArenovaEmail = trimmed.endsWith("@arenova.id", ignoreCase = true)

                                    if (isRegistered || isDemoAdmin || isDemoUser || isArenovaEmail) {
                                        // Email terdaftar -> lanjut ke Verifikasi OTP
                                        onNotRegistered(false)
                                        resetEmailTarget = trimmed
                                        Toast.makeText(
                                            context,
                                            "Kode 6-digit berhasil dikirim ke $trimmed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = ArenovaScreen.OTP_VERIFICATION
                                    } else {
                                        // Email belum terdaftar di sistem -> aktifkan tampilan error
                                        onNotRegistered(true)
                                    }
                                },
                                onProfileClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Akun Arenova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 6. OTP VERIFICATION SCREEN (Verifikasi Kode)
                        ArenovaScreen.OTP_VERIFICATION -> {
                            OtpVerificationScreen(
                                email = if (resetEmailTarget.isNotBlank()) resetEmailTarget else "user@email.com",
                                onBackClick = {
                                    currentScreen = ArenovaScreen.FORGOT_PASSWORD
                                },
                                onChangeEmailClick = {
                                    currentScreen = ArenovaScreen.FORGOT_PASSWORD
                                },
                                onResendCodeClick = {
                                    Toast.makeText(
                                        context,
                                        "Kode verifikasi baru telah dikirimkan ke email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onVerificationSuccess = { code ->
                                    Toast.makeText(
                                        context,
                                        "Verifikasi Berhasil! Silakan atur sandi baru.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Berpindah ke tampilan Buat Sandi Baru (Reset Password)
                                    currentScreen = ArenovaScreen.RESET_PASSWORD
                                },
                                onProfileClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Akun Arenova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 7. RESET PASSWORD SCREEN (Buat Sandi Baru)
                        ArenovaScreen.RESET_PASSWORD -> {
                            ResetPasswordScreen(
                                onBackClick = {
                                    currentScreen = ArenovaScreen.OTP_VERIFICATION
                                },
                                onPasswordResetSuccess = { newPassword ->
                                    registeredPassword = newPassword
                                    // Navigasi ke Layar Berhasil Ubah Sandi (sesuai instruksi pengguna)
                                    currentScreen = ArenovaScreen.PASSWORD_CHANGED_SUCCESS
                                },
                                onProfileClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Akun Arenova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 8. PASSWORD CHANGED SUCCESS SCREEN (Sandi Berhasil Diubah)
                        ArenovaScreen.PASSWORD_CHANGED_SUCCESS -> {
                            PasswordChangedSuccessScreen(
                                onLoginClick = {
                                    Toast.makeText(
                                        context,
                                        "Silakan masuk dengan sandi baru Anda",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Dari layar sukses -> langsung menuju ke Login Screen
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onHelpClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Bantuan Arenova: Hubungi cs@arenova.id",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }

                        // 6. DASHBOARD SCREEN (Menu Utama Lapangan & Komunitas)
                        ArenovaScreen.DASHBOARD -> {
                            DashboardScreen(
                                userName = currentUserName,
                                onCategoryClick = { category ->
                                    if (category.equals("Badminton", ignoreCase = true)) {
                                        // Navigasi ke Layar Daftar Lapangan Badminton
                                        currentScreen = ArenovaScreen.BADMINTON_LIST
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Membuka daftar lapangan $category",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onCommunityClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka sesi main bareng komunitas terdekat",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onNotificationClick = {
                                    Toast.makeText(
                                        context,
                                        "Belum ada notifikasi baru",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onLogoutClick = {
                                    Toast.makeText(
                                        context,
                                        "Berhasil keluar dari akun $currentUserName",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    currentScreen = ArenovaScreen.LOGIN
                                }
                            )
                        }

                        // 7. BADMINTON COURT LIST SCREEN (Daftar Lapangan Badminton)
                        ArenovaScreen.BADMINTON_LIST -> {
                            BadmintonListScreen(
                                onBackClick = {
                                    currentScreen = ArenovaScreen.DASHBOARD
                                },
                                onNotificationClick = {
                                    Toast.makeText(
                                        context,
                                        "Belum ada notifikasi baru",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onProfileClick = {
                                    Toast.makeText(
                                        context,
                                        "Profil $currentUserName",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onFilterClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka opsi filter lapangan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onSortClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka urutan harga & rating",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onBookCourtClick = { court ->
                                    Toast.makeText(
                                        context,
                                        "Pemesanan ${court.name} (${court.price}/jam) berhasil dipilih!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onTabSelected = { tab ->
                                    when (tab) {
                                        DashboardTab.BERANDA -> {
                                            currentScreen = ArenovaScreen.DASHBOARD
                                        }
                                        DashboardTab.RIWAYAT, DashboardTab.PROFIL -> {
                                            currentScreen = ArenovaScreen.DASHBOARD
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    ArenovaTheme {
        DashboardScreen()
    }
}
