package com.app.greenpass.models

import java.util.*
import kotlin.math.absoluteValue

class PersonModel(var firstName: String, var secondName: String, var iDNP: String) {
//    var tests: ArrayList<TestModel> = ArrayList()
//    var vaccinations: ArrayList<VaccinationModel> = ArrayList()
//
//    constructor(
//            var firstName: String,
//            var secondName: String,
//            var iDNP: String,
//            var tests: ArrayList<Tests>,
//            var vaccinations: ArrayList<Vaccinations>,
//    ){
//        firstName = this.firstName
//    }

    override fun toString(): String {
        return this.firstName.toUpperCase(Locale.ROOT) + " " +
                this.secondName.toUpperCase(Locale.ROOT) + " " +
                this.hashCode()
    }

//    fun addTest(test: TestModel){
//        tests.add(test)
//    }
//
//    fun addVaccination(vaccination: VaccinationModel){
//        vaccinations.add(vaccination)
//    }
//
//    fun getTests(): List<TestModel>{
//        return tests
//    }
//
//    fun deleteTest(test: TestModel){
//        tests.remove(test)
//    }
//
//    fun deleteVaccination(test: VaccinationModel){
//        vaccinations.remove(test)
//    }
//
//    fun getVaccinations(): List<VaccinationModel>{
//        return vaccinations
//    }



    override fun hashCode(): Int {
        return (firstName + secondName + iDNP).hashCode().absoluteValue
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonModel

        if (firstName != other.firstName) return false
        if (secondName != other.secondName) return false
        if (iDNP != other.iDNP) return false
//        if (tests != other.tests) return false
//        if (vaccinations != other.vaccinations) return false

        return true
    }

}