package com.app.greenpass.models

import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.app.greenpass.enums.Vaccines
import java.util.*

//class with fields describing a person + vaccination

class VaccinationModel(
    var type: Vaccines,
    var vaccDate: String,
    var lab: String,
    var series: String,
    var owner: Int,
) {
    fun display(): Spanned {
        return HtmlCompat.fromHtml(
            "OWNER:<br /> <b>$owner</b> <br />" +
                    "VACCINE TYPE:<br /> <b>$type</b><br />" +
                    "VACCINATION DATE:<br /> <b>${vaccDate}</b><br />" +
                    "LABORATORY:<br /> <b>$lab</b><br />" +
                    "VACCINE SERIES:<br /> <b>$series</b>",
            0
        )
    }
}