package com.app.greenpass.database.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.greenpass.models.TestModel
import java.util.regex.Matcher
import java.util.regex.Pattern

@Entity
data class Tests(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "PERSON_CODE") val personCode: Int,
    @ColumnInfo(name = "TEST_DATE") val testDate: String,
    @ColumnInfo(name = "TEST_RESULT") val testResult: Boolean,
    @ColumnInfo(name = "ANTIBODIES") val antibodies: Boolean,
) : Comparable<Tests> {
    override fun compareTo(other: Tests): Int {
        val p = Pattern.compile("\\d*\$")
        val m1 = p.matcher(testDate)
        val m2 = p.matcher(other.testDate)
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

fun TestModel.toMap() = Tests(
    personCode = this.owner,
    testDate = this.testDate,
    testResult = this.testResult,
    antibodies = this.antibodies,
)

fun Tests.toMap() = TestModel(
    owner = this.personCode,
    testDate = this.testDate,
    testResult = this.testResult,
    antibodies = this.antibodies,
)
