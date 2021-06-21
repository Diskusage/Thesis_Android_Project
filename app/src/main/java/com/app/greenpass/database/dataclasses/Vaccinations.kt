package com.app.greenpass.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.enums.Vaccines
import com.app.greenpass.models.VaccinationModel
import java.util.regex.Pattern

@Entity
data class Vaccinations(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "PERSON_CODE") val personCode: Int,
    @ColumnInfo(name = "VACCINATION_SERIES") val series: String,
    @ColumnInfo(name = "VACCINATION_LAB") val labName: String,
    @ColumnInfo(name = "VACCINATION_VACCINE") val vaccineType: Int,
    @ColumnInfo(name = "VACCINATION_DATE") val vaccinationDate: String,
) : Comparable<Vaccinations>, Subject {
    override fun compareTo(other: Vaccinations): Int {
        val p = Pattern.compile("\\d*\$")
        val m1 = p.matcher(vaccinationDate)
        val m2 = p.matcher(other.vaccinationDate)
        if (m1.find() && m2.find()) {
            if (m1.group(0).toInt() > m2.group(0).toInt()) {
                return 1
            }
            return if (m1.group(0).toInt() < m2.group(0).toInt()) {
                -1
            } else 0
        }
        return 0
    }

}

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

