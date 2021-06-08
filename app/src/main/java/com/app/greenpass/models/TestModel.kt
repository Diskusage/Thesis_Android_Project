package com.app.greenpass.models

import android.text.Spanned
import androidx.core.text.HtmlCompat

//class with fields describing tests, bound to IDNP
class TestModel(
    var testResult: Boolean,
    var testDate: String,
    var antibodies: Boolean,
    var owner: Int,
) {

    fun display(): Spanned {
        return HtmlCompat.fromHtml(
            "OWNER<br /> <b>$owner</b><br />" +
                    "TEST DATE<br /> <b>$testDate</b><br />" +
                    "ANTIBODIES<br /> <b>${if (antibodies) "Present" else "Absent"}</b>",
            0
        )
    }
}