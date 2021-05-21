package com.app.greenpass.models

import java.util.*
import kotlin.math.absoluteValue

class PersonModel(var firstName: String, var secondName: String, var iDNP: String){

    override fun toString(): String {
        return this.firstName.toUpperCase(Locale.ROOT) + " " +
                this.secondName.toUpperCase(Locale.ROOT) + " " +
                this.hashCode()
    }

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
        return true
    }

}