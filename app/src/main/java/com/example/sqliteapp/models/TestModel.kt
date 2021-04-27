package com.example.sqliteapp.models

class TestModel(var idnp: String?, var testResult: Boolean, var testDate: String?, antibodies: Boolean) {
    var isAntibodies = antibodies

    override fun toString(): String {
        return "IDNP: $idnp\n" +
                "Test date: $testDate\n" +
                "Test result: ${if (testResult) "Positive" else "Negative"}\n" +
                "Antibodies: ${if (isAntibodies) "Present" else "Absent"}"
    }
}