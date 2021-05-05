package com.example.sqliteapp.enums

//input vaccine types here
enum class Vaccines(val t : Int) {
    AstroZeneca(1),
    Pfizer(2),
    SputnikV(3),
    None(-1);
    companion object{
        fun fromInt(value: Int) = values().first { it.t == value}
    }

}

