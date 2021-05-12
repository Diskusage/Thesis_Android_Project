package com.app.greenpass.models

//class with fields describing tests, bound to IDNP
class TestModel(
        var testResult: Boolean,
        var testDate: String,
        var antibodies: Boolean,
        var owner: Int,
)
{

    override fun toString(): String {
        return  "OWNER:\n $owner\n" +
                "TEST DATE:\n $testDate\n" +
                "ANTIBODIES:\n ${if (antibodies) "PRESENT" else "ABSENT"}"
    }

}