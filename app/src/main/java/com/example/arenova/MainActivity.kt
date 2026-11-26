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
import com.example.arenova.data.model.UserModel
import com.example.arenova.ui.screens.ArenovaSplashScreen
import com.example.arenova.ui.screens.BadmintonListScreen
import com.example.arenova.ui.screens.CourtDetailItem
import com.example.arenova.ui.screens.CourtDetailScreen
import com.example.arenova.ui.screens.DashboardScreen
import com.example.arenova.ui.screens.DashboardTab
import com.example.arenova.ui.screens.EditProfileScreen
import com.example.arenova.ui.screens.ForgotPasswordScreen
import com.example.arenova.ui.screens.FutsalListScreen
import com.example.arenova.ui.screens.LoginScreen
import com.example.arenova.ui.screens.MiniSoccerListScreen
import com.example.arenova.ui.screens.OtpVerificationScreen
import com.example.arenova.ui.screens.PadelListScreen
import com.example.arenova.ui.screens.PasswordChangedSuccessScreen
import com.example.arenova.ui.screens.PaymentMethodScreen
import com.example.arenova.ui.screens.ProfileScreen
import com.example.arenova.ui.screens.RegisterScreen
import com.example.arenova.ui.screens.RegisterSuccessScreen
import com.example.arenova.ui.screens.ResetPasswordScreen
import com.example.arenova.ui.screens.ScheduleSelectionScreen
import com.example.arenova.ui.theme.ArenovaTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

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
    BADMINTON_LIST,
    FUTSAL_LIST,
    MINI_SOCCER_LIST,
    PADEL_LIST,
    COURT_DETAIL,
    SCHEDULE_SELECTION,
    PAYMENT_METHOD,
    PROFILE,
    EDIT_PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArenovaTheme {
                val context = LocalContext.current
                var currentScreen by remember { mutableStateOf(ArenovaScreen.SPLASH) }
                var previousScreen by remember { mutableStateOf(ArenovaScreen.DASHBOARD) }
                var selectedCourtDetail by remember { mutableStateOf(CourtDetailItem()) }
                var bookingDate by remember { mutableStateOf("01 Jan 2026") }
                var bookingTimeSlot by remember { mutableStateOf("15:00 - 16:00") }
                var bookingTotalPrice by remember { mutableStateOf("Rp 100.000") }
                var currentUserName by remember { mutableStateOf("Angga Dwi Saputro") }
                var registeredEmail by remember { mutableStateOf("") }
                var registeredPassword by remember { mutableStateOf("") }
                var resetEmailTarget by remember { mutableStateOf("user@email.com") }
                var isLoginFailed by remember { mutableStateOf(false) }
                var isLoginLoading by remember { mutableStateOf(false) }
                var loginErrorMessage by remember { mutableStateOf("Email atau password salah") }
                var isRegisterFailed by remember { mutableStateOf(false) }
                var isRegisterLoading by remember { mutableStateOf(false) }
                var registerErrorMessage by remember { mutableStateOf("Gagal melakukan registrasi") }

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
                        ArenovaScreen.SPLASH -> {
                            ArenovaSplashScreen(
                                onNavigateNext = {
                                    currentScreen = ArenovaScreen.LOGIN
                                }
                            )
                        }

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
                                                    val uid = firebaseUser?.uid
                                                    val fallbackName = trimmedEmail.substringBefore("@")
                                                        .replace(".", " ")
                                                        .split(" ")
                                                        .joinToString(" ") { word -> word.replaceFirstChar { char -> char.uppercase() } }

                                                    currentUserName = if (!displayName.isNullOrBlank()) displayName else fallbackName

                                                    if (uid != null) {
                                                        try {
                                                            FirebaseFirestore.getInstance().collection("users").document(uid).get()
                                                                .addOnSuccessListener { doc ->
                                                                    val nameInDb = doc.getString("fullName")
                                                                    if (!nameInDb.isNullOrBlank()) {
                                                                        currentUserName = nameInDb
                                                                    }
                                                                }
                                                        } catch (_: Exception) {}
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
                                    isRegisterFailed = false
                                    currentScreen = ArenovaScreen.REGISTER
                                }
                            )
                        }

                        ArenovaScreen.REGISTER -> {
                            RegisterScreen(
                                isError = isRegisterFailed,
                                errorMessage = registerErrorMessage,
                                isLoading = isRegisterLoading,
                                onBackClick = {
                                    isRegisterFailed = false
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onLoginClick = {
                                    isRegisterFailed = false
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onRegisterSuccess = { nama, email, pass ->
                                    val trimmedNama = nama.trim()
                                    val trimmedEmail = email.trim()
                                    val trimmedPass = pass.trim()

                                    isRegisterLoading = true
                                    isRegisterFailed = false

                                    try {
                                        val auth = FirebaseAuth.getInstance()
                                        auth.createUserWithEmailAndPassword(trimmedEmail, trimmedPass)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    val firebaseUser = auth.currentUser
                                                    val uid = firebaseUser?.uid ?: ""

                                                    try {
                                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                                            .setDisplayName(trimmedNama)
                                                            .build()
                                                        firebaseUser?.updateProfile(profileUpdates)
                                                    } catch (_: Exception) {}

                                                    val newUser = UserModel(
                                                        uid = uid,
                                                        fullName = trimmedNama,
                                                        email = trimmedEmail,
                                                        role = "member",
                                                        arenaPoints = 250,
                                                        totalMatches = 0,
                                                        createdAt = System.currentTimeMillis()
                                                    )

                                                    try {
                                                        val db = FirebaseFirestore.getInstance()
                                                        db.collection("users").document(uid).set(newUser.toMap())
                                                            .addOnCompleteListener { _ ->
                                                                isRegisterLoading = false
                                                                isRegisterFailed = false
                                                                isLoginFailed = false
                                                                registeredEmail = trimmedEmail
                                                                registeredPassword = trimmedPass
                                                                currentUserName = trimmedNama

                                                                Toast.makeText(
                                                                    context,
                                                                    "Pendaftaran berhasil! Selamat datang di Arenova.",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()

                                                                currentScreen = ArenovaScreen.REGISTER_SUCCESS
                                                            }
                                                    } catch (_: Exception) {
                                                        isRegisterLoading = false
                                                        isRegisterFailed = false
                                                        isLoginFailed = false
                                                        registeredEmail = trimmedEmail
                                                        registeredPassword = trimmedPass
                                                        currentUserName = trimmedNama
                                                        currentScreen = ArenovaScreen.REGISTER_SUCCESS
                                                    }
                                                } else {
                                                    isRegisterLoading = false
                                                    isRegisterFailed = true
                                                    val ex = task.exception
                                                    val rawMsg = ex?.localizedMessage ?: ""
                                                    registerErrorMessage = when {
                                                        ex is FirebaseAuthUserCollisionException ||
                                                                rawMsg.contains("already in use", ignoreCase = true) ->
                                                            "Email sudah terdaftar. Silakan gunakan email lain atau masuk."
                                                        ex is FirebaseAuthWeakPasswordException ||
                                                                rawMsg.contains("password", ignoreCase = true) ->
                                                            "Kata sandi terlalu lemah. Gunakan minimal 6 karakter."
                                                        rawMsg.contains("network", ignoreCase = true) ->
                                                            "Gagal terhubung: Periksa koneksi internet Anda."
                                                        else -> rawMsg.ifBlank { "Gagal melakukan registrasi" }
                                                    }
                                                }
                                            }
                                    } catch (e: Exception) {
                                        isRegisterLoading = false
                                        isRegisterFailed = true
                                        registerErrorMessage = e.localizedMessage ?: "Terjadi kesalahan saat pendaftaran"
                                    }
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

                        ArenovaScreen.FORGOT_PASSWORD -> {
                            ForgotPasswordScreen(
                                onBackClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onLoginClick = {
                                    currentScreen = ArenovaScreen.LOGIN
                                },
                                onRegisterClick = {
                                    isRegisterFailed = false
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

                        ArenovaScreen.DASHBOARD -> {
                            DashboardScreen(
                                userName = currentUserName,
                                onCategoryClick = { category ->
                                    when {
                                        category.equals("Badminton", ignoreCase = true) -> {
                                            currentScreen = ArenovaScreen.BADMINTON_LIST
                                        }
                                        category.equals("Futsal", ignoreCase = true) -> {
                                            currentScreen = ArenovaScreen.FUTSAL_LIST
                                        }
                                        category.equals("Mini Soccer", ignoreCase = true) -> {
                                            currentScreen = ArenovaScreen.MINI_SOCCER_LIST
                                        }
                                        category.equals("Padel", ignoreCase = true) -> {
                                            currentScreen = ArenovaScreen.PADEL_LIST
                                        }
                                        else -> {
                                            Toast.makeText(
                                                context,
                                                "Membuka daftar lapangan $category",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
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
                                    currentScreen = ArenovaScreen.PROFILE
                                }
                            )
                        }

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
                                    currentScreen = ArenovaScreen.PROFILE
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
                                    selectedCourtDetail = CourtDetailItem(
                                        id = court.id,
                                        name = court.name,
                                        sportType = "Badminton",
                                        courtTag = "INDOOR COURT ${court.id}",
                                        location = court.location,
                                        price = court.price,
                                        rating = court.starRating,
                                        reviewCount = "128 Ulasan",
                                        imageRes = court.imageRes,
                                        description = "Lapangan bulutangkis berstandar PBSI dengan material vinyl interlock berkualitas tinggi yang meredam benturan sendi secara optimal. Dilengkapi dengan tata lampu LED anti-glare 800 lux, sirkulasi udara sejuk, ruang ganti privat, serta kantin dengan koneksi WiFi berkecepatan tinggi.",
                                        courtSize = "13.4 x 6.1 Meter"
                                    )
                                    previousScreen = ArenovaScreen.BADMINTON_LIST
                                    currentScreen = ArenovaScreen.COURT_DETAIL
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.PROFIL) {
                                        currentScreen = ArenovaScreen.PROFILE
                                    }
                                }
                            )
                        }

                        ArenovaScreen.FUTSAL_LIST -> {
                            FutsalListScreen(
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
                                    currentScreen = ArenovaScreen.PROFILE
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
                                    selectedCourtDetail = CourtDetailItem(
                                        id = court.id,
                                        name = court.name,
                                        sportType = "Futsal",
                                        courtTag = "INDOOR COURT A",
                                        location = court.location,
                                        price = court.price,
                                        rating = court.starRating,
                                        reviewCount = "128 Ulasan",
                                        imageRes = court.imageRes,
                                        description = "Lapangan berstandar turnamen nasional dengan material vinyl interlock berkualitas tinggi yang meredam benturan lutut secara optimal. Dilengkapi dengan tata lampu LED anti-glare 800 lux, sirkulasi udara sejuk, ruang ganti privat, serta kantin dengan koneksi WiFi berkecepatan tinggi.",
                                        courtSize = "25 x 15 Meter"
                                    )
                                    previousScreen = ArenovaScreen.FUTSAL_LIST
                                    currentScreen = ArenovaScreen.COURT_DETAIL
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.PROFIL) {
                                        currentScreen = ArenovaScreen.PROFILE
                                    }
                                }
                            )
                        }

                        ArenovaScreen.MINI_SOCCER_LIST -> {
                            MiniSoccerListScreen(
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
                                    currentScreen = ArenovaScreen.PROFILE
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
                                    selectedCourtDetail = CourtDetailItem(
                                        id = court.id,
                                        name = court.name,
                                        sportType = "Mini Soccer",
                                        courtTag = "OUTDOOR TURF A",
                                        location = court.location,
                                        price = court.price,
                                        rating = court.starRating,
                                        reviewCount = "128 Ulasan",
                                        imageRes = court.imageRes,
                                        description = "Lapangan mini soccer berstandar internasional dengan rumput sintetis bersertifikasi yang empuk dan aman untuk pergerakan pemain. Dilengkapi lampu sorot stadion terang, area duduk tribun, dan ruang ganti higienis.",
                                        courtSize = "50 x 30 Meter"
                                    )
                                    previousScreen = ArenovaScreen.MINI_SOCCER_LIST
                                    currentScreen = ArenovaScreen.COURT_DETAIL
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.PROFIL) {
                                        currentScreen = ArenovaScreen.PROFILE
                                    }
                                }
                            )
                        }

                        ArenovaScreen.PADEL_LIST -> {
                            PadelListScreen(
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
                                    currentScreen = ArenovaScreen.PROFILE
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
                                    selectedCourtDetail = CourtDetailItem(
                                        id = court.id,
                                        name = court.name,
                                        sportType = "Padel",
                                        courtTag = "PANORAMIC GLASS",
                                        location = court.location,
                                        price = court.price,
                                        rating = court.starRating,
                                        reviewCount = "128 Ulasan",
                                        imageRes = court.imageRes,
                                        description = "Lapangan padel tipe panoramic court dengan kaca tempered ultra-clear berstandar resmi WPT. Karpet monofilamen berdensitas tinggi memberikan pantulan bola stabil dan cengkeraman optimal.",
                                        courtSize = "20 x 10 Meter"
                                    )
                                    previousScreen = ArenovaScreen.PADEL_LIST
                                    currentScreen = ArenovaScreen.COURT_DETAIL
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.PROFIL) {
                                        currentScreen = ArenovaScreen.PROFILE
                                    }
                                }
                            )
                        }

                        ArenovaScreen.COURT_DETAIL -> {
                            CourtDetailScreen(
                                court = selectedCourtDetail,
                                onBackClick = {
                                    currentScreen = previousScreen
                                },
                                onShareClick = {
                                    // Toast handled inside screen
                                },
                                onProfileClick = {
                                    currentScreen = ArenovaScreen.PROFILE
                                },
                                onSelectScheduleClick = { _ ->
                                    currentScreen = ArenovaScreen.SCHEDULE_SELECTION
                                }
                            )
                        }

                        ArenovaScreen.SCHEDULE_SELECTION -> {
                            ScheduleSelectionScreen(
                                court = selectedCourtDetail,
                                onBackClick = {
                                    currentScreen = ArenovaScreen.COURT_DETAIL
                                },
                                onShareClick = {
                                    // Toast handled inside screen
                                },
                                onProfileClick = {
                                    currentScreen = ArenovaScreen.PROFILE
                                },
                                onProceedClick = { courtItem, date, timeSlot, _, total ->
                                    selectedCourtDetail = courtItem
                                    bookingDate = date
                                    bookingTimeSlot = timeSlot
                                    bookingTotalPrice = total
                                    previousScreen = ArenovaScreen.SCHEDULE_SELECTION
                                    currentScreen = ArenovaScreen.PAYMENT_METHOD
                                }
                            )
                        }

                        ArenovaScreen.PAYMENT_METHOD -> {
                            PaymentMethodScreen(
                                court = selectedCourtDetail,
                                bookingDate = bookingDate,
                                bookingTime = bookingTimeSlot,
                                totalPrice = bookingTotalPrice,
                                onBackClick = {
                                    currentScreen = ArenovaScreen.SCHEDULE_SELECTION
                                },
                                onHelpClick = {
                                    Toast.makeText(
                                        context,
                                        "Pusat Bantuan Pembayaran Arenova",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onProfileClick = {
                                    currentScreen = ArenovaScreen.PROFILE
                                },
                                onPaymentSuccess = { method ->
                                    Toast.makeText(
                                        context,
                                        "Pembayaran via ${method.title} berhasil dikonfirmasi! Booking Anda telah aktif.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    currentScreen = ArenovaScreen.DASHBOARD
                                }
                            )
                        }

                        ArenovaScreen.PROFILE -> {
                            val activeEmail = try {
                                val fbEmail = FirebaseAuth.getInstance().currentUser?.email
                                if (!fbEmail.isNullOrBlank()) fbEmail else registeredEmail.ifBlank { "user@email.com" }
                            } catch (_: Exception) {
                                registeredEmail.ifBlank { "user@email.com" }
                            }

                            ProfileScreen(
                                userName = currentUserName,
                                userEmail = activeEmail,
                                onBackClick = {
                                    currentScreen = ArenovaScreen.DASHBOARD
                                },
                                onEditProfileClick = {
                                    currentScreen = ArenovaScreen.EDIT_PROFILE
                                },
                                onSettingsClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka menu Pengaturan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onChangePhotoClick = {
                                    currentScreen = ArenovaScreen.EDIT_PROFILE
                                },
                                onLogoutClick = {
                                    logoutToLogin()
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.RIWAYAT) {
                                        Toast.makeText(
                                            context,
                                            "Fitur Riwayat dalam pengembangan",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }

                        ArenovaScreen.EDIT_PROFILE -> {
                            val activeEmail = try {
                                val fbEmail = FirebaseAuth.getInstance().currentUser?.email
                                if (!fbEmail.isNullOrBlank()) fbEmail else registeredEmail.ifBlank { "user@email.com" }
                            } catch (_: Exception) {
                                registeredEmail.ifBlank { "user@email.com" }
                            }

                            EditProfileScreen(
                                userName = currentUserName,
                                userEmail = activeEmail,
                                onBackClick = {
                                    currentScreen = ArenovaScreen.PROFILE
                                },
                                onNotificationClick = {
                                    Toast.makeText(
                                        context,
                                        "Tidak ada notifikasi baru",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onSettingsClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka menu Pengaturan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onChangePhotoClick = {
                                    Toast.makeText(
                                        context,
                                        "Fitur ubah foto profil",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onSaveClick = { newName, newEmail, newPhone ->
                                    val trimmedName = newName.trim()
                                    val trimmedEmail = newEmail.trim()
                                    val trimmedPhone = newPhone.trim()

                                    if (trimmedName.isNotBlank()) {
                                        currentUserName = trimmedName
                                    }
                                    if (trimmedEmail.isNotBlank()) {
                                        registeredEmail = trimmedEmail
                                    }

                                    try {
                                        val user = FirebaseAuth.getInstance().currentUser
                                        if (user != null) {
                                            if (trimmedName.isNotBlank()) {
                                                val profileUpdates = UserProfileChangeRequest.Builder()
                                                    .setDisplayName(trimmedName)
                                                    .build()
                                                user.updateProfile(profileUpdates)
                                            }
                                            val updates = mutableMapOf<String, Any>()
                                            if (trimmedName.isNotBlank()) updates["fullName"] = trimmedName
                                            if (trimmedEmail.isNotBlank()) updates["email"] = trimmedEmail
                                            if (trimmedPhone.isNotBlank()) updates["phone"] = trimmedPhone
                                            if (updates.isNotEmpty()) {
                                                FirebaseFirestore.getInstance()
                                                    .collection("users")
                                                    .document(user.uid)
                                                    .update(updates)
                                            }
                                        }
                                    } catch (_: Exception) {}

                                    Toast.makeText(
                                        context,
                                        "Profil berhasil diperbarui",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onLogoutClick = {
                                    logoutToLogin()
                                },
                                onTabSelected = { tab ->
                                    if (tab == DashboardTab.BERANDA) {
                                        currentScreen = ArenovaScreen.DASHBOARD
                                    } else if (tab == DashboardTab.PROFIL) {
                                        currentScreen = ArenovaScreen.PROFILE
                                    } else if (tab == DashboardTab.RIWAYAT) {
                                        Toast.makeText(
                                            context,
                                            "Fitur Riwayat dalam pengembangan",
                                            Toast.LENGTH_SHORT
                                        ).show()
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