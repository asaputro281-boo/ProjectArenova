package com.example.arenova

import android.os.Bundle
import android.util.Patterns
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
import com.google.firebase.auth.FirebaseAuth

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
                // Mulai dari Splash Screen
                var currentScreen by remember { mutableStateOf(ArenovaScreen.SPLASH) }
                var currentUserName by remember { mutableStateOf("Angga Dwi Saputro") }
                var registeredEmail by remember { mutableStateOf("") }
                var registeredPassword by remember { mutableStateOf("") }
                var resetEmailTarget by remember { mutableStateOf("user@email.com") }
                var isLoginFailed by remember { mutableStateOf(false) }
                var isLoginLoading by remember { mutableStateOf(false) }
                var loginErrorMessage by remember { mutableStateOf("Email atau password salah") }

                fun logoutToLogin() {
                    try {
                        FirebaseAuth.getInstance().signOut()
                    } catch (_: Exception) {
                    }
                    isLoginFailed = false
                    isLoginLoading = false
                    currentScreen = ArenovaScreen.LOGIN
                    Toast.makeText(
                        context,
                        "Berhasil keluar dari akun",
                        Toast.LENGTH_SHORT
                    ).show()
                }

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
                                errorMessage = loginErrorMessage,
                                isLoading = isLoginLoading,
                                onDismissError = { isLoginFailed = false },
                                onLoginClick = { email, pass ->
                                    val trimmedEmail = email.trim()
                                    val trimmedPass = pass.trim()

                                    if (trimmedEmail.isBlank() || trimmedPass.isBlank()) {
                                        isLoginFailed = true
                                        loginErrorMessage = "Email dan kata sandi tidak boleh kosong"
                                        return@LoginScreen
                                    }

                                    if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
                                        isLoginFailed = true
                                        loginErrorMessage = "Format email tidak valid. Gunakan format seperti nama@email.com"
                                        return@LoginScreen
                                    }

                                    // Fallback akun lokal / demo untuk pengetesan offline
                                    val isValidRegistered = registeredEmail.isNotBlank() &&
                                            trimmedEmail.equals(registeredEmail.trim(), ignoreCase = true) &&
                                            trimmedPass == registeredPassword
                                    val isDemoAdmin = trimmedEmail.equals("admin@arenova.id", ignoreCase = true) &&
                                            (trimmedPass == "123456" || trimmedPass == "admin123")
                                    val isDemoUser = trimmedEmail.equals("user@arenova.id", ignoreCase = true) &&
                                            trimmedPass == "123456"

                                    if (isValidRegistered || isDemoAdmin || isDemoUser) {
                                        isLoginFailed = false
                                        currentUserName = when {
                                            isDemoAdmin -> "Admin Arenova"
                                            isDemoUser -> "User Demo"
                                            currentUserName.isNotBlank() -> currentUserName
                                            else -> trimmedEmail.substringBefore("@")
                                                .replace(".", " ")
                                                .split(" ")
                                                .joinToString(" ") { word ->
                                                    word.replaceFirstChar { char -> char.uppercase() }
                                                }
                                        }

                                        Toast.makeText(
                                            context,
                                            "Berhasil masuk! Selamat datang, $currentUserName",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = ArenovaScreen.DASHBOARD
                                        return@LoginScreen
                                    }

                                    // Login melalui Firebase Authentication
                                    isLoginLoading = true
                                    isLoginFailed = false

                                    try {
                                        val auth = FirebaseAuth.getInstance()
                                        auth.signInWithEmailAndPassword(trimmedEmail, trimmedPass)
                                            .addOnCompleteListener { task ->
                                                isLoginLoading = false
                                                if (task.isSuccessful) {
                                                    isLoginFailed = false
                                                    val firebaseUser = auth.currentUser
                                                    val displayName = firebaseUser?.displayName
                                                    currentUserName = if (!displayName.isNullOrBlank()) {
                                                        displayName
                                                    } else {
                                                        trimmedEmail.substringBefore("@")
                                                            .replace(".", " ")
                                                            .split(" ")
                                                            .joinToString(" ") { word ->
                                                                word.replaceFirstChar { char -> char.uppercase() }
                                                            }
                                                    }

                                                    Toast.makeText(
                                                        context,
                                                        "Berhasil masuk! Selamat datang, $currentUserName",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    currentScreen = ArenovaScreen.DASHBOARD
                                                } else {
                                                    isLoginFailed = true
                                                    val rawMsg = task.exception?.localizedMessage ?: ""
                                                    loginErrorMessage = when {
                                                        rawMsg.contains("badly formatted", ignoreCase = true) ->
                                                            "Format email tidak valid. Gunakan format seperti nama@email.com"
                                                        rawMsg.contains("no user record", ignoreCase = true) ||
                                                                rawMsg.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) ||
                                                                rawMsg.contains("password is invalid", ignoreCase = true) ->
                                                            "Email atau password salah / belum terdaftar"
                                                        rawMsg.contains("network", ignoreCase = true) ->
                                                            "Gagal terhubung: Periksa koneksi internet Anda"
                                                        else -> "Email atau password salah"
                                                    }
                                                }
                                            }
                                    } catch (e: Exception) {
                                        isLoginLoading = false
                                        isLoginFailed = true
                                        val rawErr = e.localizedMessage ?: ""
                                        loginErrorMessage = if (rawErr.contains("badly formatted", ignoreCase = true)) {
                                            "Format email tidak valid. Gunakan format seperti nama@email.com"
                                        } else {
                                            "Terjadi kesalahan saat masuk"
                                        }
                                    }
                                },
                                onForgotPasswordClick = {
                                    isLoginFailed = false
                                    currentScreen = ArenovaScreen.FORGOT_PASSWORD
                                },
                                onRegisterClick = {
                                    isLoginFailed = false
                                    currentScreen = ArenovaScreen.REGISTER
                                }
                            )
                        }

                        // 3. REGISTER SCREEN (Pembuatan Akun)
                        ArenovaScreen.REGISTER -> {
                            RegisterScreen(
                                initialShowError = false,
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
                                        onNotRegistered(false)
                                        resetEmailTarget = trimmed
                                        Toast.makeText(
                                            context,
                                            "Kode 6-digit berhasil dikirim ke $trimmed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = ArenovaScreen.OTP_VERIFICATION
                                    } else {
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
                                onVerificationSuccess = { _ ->
                                    Toast.makeText(
                                        context,
                                        "Verifikasi Berhasil! Silakan atur sandi baru.",
                                        Toast.LENGTH_SHORT
                                    ).show()
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

                        // 9. DASHBOARD SCREEN (Menu Utama Lapangan & Komunitas)
                        ArenovaScreen.DASHBOARD -> {
                            DashboardScreen(
                                userName = currentUserName,
                                onCategoryClick = { category ->
                                    if (category.equals("Badminton", ignoreCase = true)) {
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
                                onProfileClick = {
                                    logoutToLogin()
                                }
                            )
                        }

                        // 10. BADMINTON COURT LIST SCREEN (Daftar Lapangan Badminton)
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
                                    logoutToLogin()
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
                                        DashboardTab.RIWAYAT -> {
                                            Toast.makeText(
                                                context,
                                                "Membuka riwayat pemesanan",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        DashboardTab.PROFIL -> {
                                            logoutToLogin()
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
        ArenovaSplashScreen(onNavigateNext = {})
    }
}