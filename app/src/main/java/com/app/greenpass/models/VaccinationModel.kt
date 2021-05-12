package com.app.greenpass.models

import com.app.greenpass.enums.Vaccines
import java.util.*

//class with fields describing a person + vaccination

class VaccinationModel(
        var type: Vaccines,
        var vaccDate: String,
        var lab: String,
        var series: String,
        var owner: Int,
        )

{

    override fun toString(): String {
        return "OWNER:\n $owner\n" +
                "VACCINE TYPE:\n $type\n" +
                "VACCINATION DATE:\n ${vaccDate.toUpperCase(Locale.ROOT)}\n" +
                "LABORATORY:\n $lab\n" +
                "VACCINE SERIES:\n $series"
    }

}