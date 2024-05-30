package ipn.escom.meteora.data.localities

val availableLocalities = listOf(
    Locality(
        key = "ALVARO OBREGON",
        name = "Álvaro Obregón",
        latitude = 19.3644,
        longitude = -99.1978
    ),
    Locality(
        key = "AZCAPOTZALCO",
        name = "Azcapotzalco",
        latitude = 19.4854,
        longitude = -99.1843
    ),
    Locality(
        key = "BENITO JUAREZ",
        name = "Benito Juárez",
        latitude = 19.3809,
        longitude = -99.1726
    ),
    Locality(
        key = "COYOACAN",
        name = "Coyoacán",
        latitude = 19.328,
        longitude = -99.1405
    ),
    Locality(
        key = "CUAJIMALPA DE MORELOS",
        name = "Cuajimalpa de Morelos",
        latitude = 19.3588,
        longitude = -99.3224
    ),
    Locality(
        key = "CUAUHTEMOC",
        name = "Cuauhtémoc",
        latitude = 19.4337,
        longitude = -99.1562
    ),
    Locality(
        key = "GUSTAVO A MADERO",
        name = "Gustavo A. Madero",
        latitude = 19.4926,
        longitude = -99.1183
    ),
    Locality(
        key = "IZTACALCO",
        name = "Iztacalco",
        latitude = 19.3956,
        longitude = -99.0973
    ),
    Locality(
        key = "IZTAPALAPA",
        name = "Iztapalapa",
        latitude = 19.3429,
        longitude = -99.0616
    ),
    Locality(
        key = "MAGDALENA CONTRERAS",
        name = "Magdalena Contreras",
        latitude = 19.3139,
        longitude = -99.2415
    ),
    Locality(
        key = "MIGUEL HIDALGO",
        name = "Miguel Hidalgo",
        latitude = 19.4297,
        longitude = -99.1999
    ),
    Locality(
        key = "MILPA ALTA",
        name = "Milpa Alta",
        latitude = 19.1249,
        longitude = -99.0054
    ),
    Locality(
        key = "TLAHUAC",
        name = "Tláhuac",
        latitude = 19.2692,
        longitude = -99.0046
    ),
    Locality(
        key = "TLALPAN",
        name = "Tlalpan",
        latitude = 19.1967,
        longitude = -99.2109
    ),
    Locality(
        key = "VENUSTIANO CARRANZA",
        name = "Venustiano Carranza",
        latitude = 19.4306,
        longitude = -99.072
    ),
    Locality(
        key = "XOCHIMILCO",
        name = "Xochimilco",
        latitude = 19.2565,
        longitude = -99.1033
    )
)

fun getLocalityKeyFromName(name: String): String {
    return availableLocalities.find { it.name == name }?.key ?: ""
}

fun getLocalityNameFromKey(key: String): String {
    return availableLocalities.find { it.key == key }?.name ?: ""
}
