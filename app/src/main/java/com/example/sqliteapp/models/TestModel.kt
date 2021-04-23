package com.example.sqliteapp.models

class TestModel(var idnp: String?, var testResult: Boolean, var testDate: String?, antibodies: Boolean) {
    var isAntibodies = antibodies

    override fun toString(): String {
        return "$idnp $testDate ${if (testResult) "Positive" else "Negative"}" +
                " ${if (isAntibodies) "Antibodies present" else "Antibodies absent"}"
    }
}