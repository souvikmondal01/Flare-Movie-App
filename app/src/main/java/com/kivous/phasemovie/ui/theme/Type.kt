package com.kivous.phasemovie.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.kivous.phasemovie.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

fun nunito(weight: FontWeight): FontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Nunito"), fontProvider = provider, weight = weight
    )
)

fun poppins(weight: FontWeight): FontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Poppins"), fontProvider = provider, weight = weight
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = poppins(FontWeight.SemiBold),
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle(
        fontFamily = nunito(FontWeight.SemiBold),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = nunito(FontWeight.Bold),
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)

val NunitoBold = FontFamily(Font(R.font.nunito_bold))
val NunitoSemiBold = FontFamily(Font(R.font.nunito_semi_bold))
val PoppinsBold = FontFamily(Font(R.font.poppins_bold))