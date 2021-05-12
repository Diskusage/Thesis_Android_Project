package com.app.greenpass.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.enums.Vaccines
import com.app.greenpass.models.VaccinationModel

@Entity
data class Vaccinations(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "PERSON_CODE") val personCode: Int,
        @ColumnInfo(name = "VACCINATION_SERIES") val series: String,
        @ColumnInfo(name = "VACCINATION_LAB") val labName: String,
        @ColumnInfo(name = "VACCINATION_VACCINE") val vaccineType: Int,
        @ColumnInfo(name = "VACCINATION_DATE") val vaccinationDate: String,
)

fun VaccinationModel.toMap() = Vaccinations(
        series = this.series,
        labName = this.lab,
        vaccineType = this.type.t,
        vaccinationDate = this.vaccDate,
        personCode = this.owner,
)
fun Vaccinations.toMap() = VaccinationModel(
        series = this.series,
        lab = this.labName,
        type = Vaccines.fromInt(vaccineType),
        vaccDate = this.vaccinationDate,
        owner = this.personCode,
)

