package com.app.greenpass.enums

import java.util.*

//input vaccine types here
@Suppress("unused")
enum class Vaccines(val t : Int) {
    ASTRAZENECA(1),
    PFIZER(2),
    SPUTNIKV(3),
    NONE(-1);
    companion object{
        fun fromInt(value: Int) = values().first { it.t == value }
        fun getValue(incoming: String): Vaccines {
            return try {
                valueOf(incoming.toUpperCase(Locale.ROOT))
            } catch (e: IllegalArgumentException){
                NONE
            }
        }
    }
}

