package com.example.sqliteapp.models

import com.example.sqliteapp.enums.Vaccines
import com.example.sqliteapp.exceptions.VaccineDoesntExist
import java.util.*

class PersonModel {
    //getters/setters
    var firstName: String? = null
    var secondName: String? = null
    var iDNP: String? = null
    private var isVaccinated = false
    var type: Vaccines? = null
    var vaccDate: String? = null

    //constructor
    constructor(firstName: String?, secondName: String?, IDNP: String?) {
        this.firstName = firstName
        this.secondName = secondName
        iDNP = IDNP
        isVaccinated = false
        type = Vaccines.None
        vaccDate = "None"
    }
    @Throws(VaccineDoesntExist::class)
    constructor(firstName: String?, secondName: String?, IDNP: String?, type: String, date: String?) {
        this.firstName = firstName
        this.secondName = secondName
        iDNP = IDNP
        isVaccinated = true
        this.type = sToEn(type)
        vaccDate = date
    }

    //toString method
    override fun toString(): String {
        return "$firstName $secondName $type $vaccDate"
    }

    @Throws(VaccineDoesntExist::class)
    fun sToEn(s: String): Vaccines {
        for (c in Vaccines.values()) {
            if (c.name.equals(s, ignoreCase = true)) return c
        }
        throw VaccineDoesntExist()
    }


}