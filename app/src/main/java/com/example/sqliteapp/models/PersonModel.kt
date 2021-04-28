package com.example.sqliteapp.models

import com.example.sqliteapp.enums.Vaccines
import com.example.sqliteapp.exceptions.VaccineDoesntExist
//class with fields describing a person + vaccination
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
    constructor(firstName: String?, secondName: String?, iDNP: String?, type: String, date: String?) {
        this.firstName = firstName
        this.secondName = secondName
        this.iDNP = iDNP
        isVaccinated = true
        this.type = sToEn(type)
        vaccDate = date
    }

    //toString method
    override fun toString(): String {
        return "First name: $firstName\n" +
                "Second name: $secondName\n" +
                "Vaccine type: $type\n" +
                "Vaccination date: $vaccDate"
    }

    @Throws(VaccineDoesntExist::class)
    fun sToEn(s: String): Vaccines {
        for (c in Vaccines.values()) {
            if (c.name.equals(s, ignoreCase = true)) return c
        }
        throw VaccineDoesntExist()
    }


}