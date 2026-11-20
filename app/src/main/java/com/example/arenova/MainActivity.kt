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
import com.example.arenova.ui.screens.DashboardScreen
import com.example.arenova.ui.screens.LoginScreen
import com.example.arenova.ui.screens.RegisterScreen
import com.example.arenova.ui.screens.RegisterSuccessScreen
import com.example.arenova.ui.theme.ArenovaTheme

enum class ArenovaScreen {
    SPLASH,
    LOGIN,
    REGISTER,
    REGISTER_SUCCESS,
    DASHBOARD
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
                                onLoginClick = { email, pass ->
                                    if (email.isBlank() || pass.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Silakan masukkan email dan kata sandi",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        // Ekstrak nama panggilan sederhana jika belum diubah
                                        if (currentUserName == "Pengguna") {
                                            val extracted = email.substringBefore("@")
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
                                    Toast.makeText(
                                        context,
                                        "Fitur Lupa Sandi: Tautan reset telah dikirim ke email",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onRegisterClick = {
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
                                onRegisterSuccess = { nama, _, _ ->
                                    if (nama.isNotBlank()) {
                                        currentUserName = nama
                                    }
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

                        // 5. DASHBOARD SCREEN (Menu Utama Lapangan & Komunitas)
                        ArenovaScreen.DASHBOARD -> {
                            DashboardScreen(
                                userName = currentUserName,
                                onCategoryClick = { category ->
                                    Toast.makeText(
                                        context,
                                        "Membuka daftar lapangan $category",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onCommunityClick = {
                                    Toast.makeText(
                                        context,
                                        "Membuka sesi main bareng komunitas terdekat",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                onProfileClick = {
                                    // Profil / Opsi Keluar
                                    Toast.makeText(
                                        context,
                                        "Keluar dari akun $currentUserName",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    currentScreen = ArenovaScreen.LOGIN
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
