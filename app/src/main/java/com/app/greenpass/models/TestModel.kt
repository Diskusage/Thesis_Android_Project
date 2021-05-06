package com.app.greenpass.models

//class with fields describing tests, bound to IDNP
class TestModel(var idnp: String, var testResult: Boolean, var testDate: String, antibodies: Boolean) {
    var isAntibodies = antibodies

    override fun toString(): String {
        return  "Test date: $testDate\n" +
                "Antibodies: ${if (isAntibodies) "Present" else "Absent"}"
    }
    fun toCode(): String {
        return idnp+testDate+testResult+isAntibodies
    }
}