package ipn.escom.meteora.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ipn.escom.meteora.R

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter)),
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontSize = 14.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.inter_medium)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.clash_display)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.space_grotesk_bold)),
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
)