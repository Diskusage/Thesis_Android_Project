package com.app.greenpass.models

import com.app.greenpass.enums.Vaccines

//class with fields describing a person + vaccination
class PersonModel(//getters/setters
        var firstName: String, var secondName: String, var iDNP: String, var type: Vaccines, date: String) {
    var vaccDate: String = date

    //toString method
    override fun toString(): String {
        return "First name: $firstName\n" +
                "Second name: $secondName\n" +
                "Vaccine type: $type\n" +
                "Vaccination date: $vaccDate"
    }
    fun toCode(): String {
        return firstName + secondName + iDNP + vaccDate + type.toString()
    }
}