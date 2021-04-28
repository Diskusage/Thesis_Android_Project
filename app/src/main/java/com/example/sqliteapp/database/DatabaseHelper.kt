package com.example.sqliteapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqliteapp.exceptions.EmptyRecordAfterExistingVaccinationException
import com.example.sqliteapp.exceptions.MatchingVaccinationException
import com.example.sqliteapp.exceptions.VaccineDoesntExist
import com.example.sqliteapp.models.PersonModel
import com.example.sqliteapp.models.TestModel
//helper, has a ton of utility functions to work with db and pull
//data for all possible situations
class DatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context,
        "Person DataBase",
        null,
        1
) {
    //called the first time you access database object
    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = ("CREATE TABLE " + PERSON_TABLE + " (" + COLUMN_PERSON_FIRSTNAME + " TEXT, "
                + COLUMN_PERSON_SECONDNAME + " TEXT, " + COLUMN_PERSON_IDNP + " TEXT, " + COLUMN_PERSON_VACCINE + " TEXT, "
                + COLUMN_VACCINATION_DATE + " TEXT)")
        val createTestTable = ("CREATE TABLE " + PERSON_TESTS + " (" + COLUMN_PERSON_IDNP + " TEXT, "
                + COLUMN_TEST_DATE + " TEXT, " + COLUMN_TEST_RESULT + " TEXT, " + COLUMN_ANTIBODIES + " TEXT)")
        db.execSQL(createTestTable)
        db.execSQL(createTableStatement)
    }

    //called when the version of the database changes (forward/backward compatibility)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

fun checkLogin(personModel: PersonModel): Boolean {
    val fName = personModel.firstName
    val sName = personModel.secondName
    val iDNP = personModel.iDNP
    val queryString = "SELECT * FROM $PERSON_TABLE where $COLUMN_PERSON_FIRSTNAME = '$fName'" +
            " AND $COLUMN_PERSON_SECONDNAME = '$sName' AND $COLUMN_PERSON_IDNP = '$iDNP'"
    val secondQuery = "SELECT * FROM $PERSON_TESTS WHERE $COLUMN_PERSON_IDNP = '$iDNP'"
    val db = this.readableDatabase
    var cursor = db.rawQuery(queryString, null)
    if (cursor.count >= 1) {
        cursor.close()
        db.close()
        return true
    } else {
        cursor = db.rawQuery(secondQuery, null)
        if (cursor.count >= 1){
            cursor.close()
            db.close()
            return true
        }
    }
    cursor.close()
    db.close()
    return false
}

    @Throws(MatchingVaccinationException::class, EmptyRecordAfterExistingVaccinationException::class)
    fun addOne(personModel: PersonModel): Boolean {
        if (checkExistingRecord(personModel.iDNP, personModel.vaccDate)) {
            throw MatchingVaccinationException()
        }
        if (checkIDNP(personModel.iDNP) && personModel.type.toString() == "None") {
            throw EmptyRecordAfterExistingVaccinationException()
        }
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_PERSON_FIRSTNAME, personModel.firstName)
        cv.put(COLUMN_PERSON_SECONDNAME, personModel.secondName)
        cv.put(COLUMN_PERSON_IDNP, personModel.iDNP)
        cv.put(COLUMN_PERSON_VACCINE, personModel.type.toString())
        cv.put(COLUMN_VACCINATION_DATE, personModel.vaccDate)
        val insert = db.insert(PERSON_TABLE, null, cv)
        return insert != -1L
    }

    fun addTest(testModel: TestModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_PERSON_IDNP, testModel.idnp)
        cv.put(COLUMN_TEST_DATE, testModel.testDate)
        cv.put(COLUMN_TEST_RESULT, testModel.testResult)
        cv.put(COLUMN_ANTIBODIES, testModel.isAntibodies)
        val insert = db.insert(PERSON_TESTS, null, cv)
        return insert != -1L
    }

    private fun checkIDNP(idnp: String?): Boolean {
        val queryString = "SELECT * FROM $PERSON_TABLE where $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.count <= 0) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }

    private fun checkExistingRecord(toCheck: String?, checkDate: String?): Boolean {
        val queryString = "SELECT * FROM $PERSON_TABLE where $COLUMN_PERSON_IDNP = '$toCheck'" +
                " AND $COLUMN_VACCINATION_DATE = '$checkDate'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.count <= 0) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }//loop through results and create objects and insert into returnlist

    //get data from the database
    val all: MutableList<PersonModel>
        get() {
            val returnList: MutableList<PersonModel> = ArrayList()
            //get data from the database
            val queryString = "SELECT * FROM $PERSON_TABLE"
            val db = this.readableDatabase
            val cursor = db.rawQuery(queryString, null)
            if (cursor.moveToFirst()) {
                //loop through results and create objects and insert into returnlist
                do {
                    val personFName = cursor.getString(0)
                    val personSName = cursor.getString(1)
                    val personIDNP = cursor.getString(2)
                    val personVaccine = cursor.getString(3)
                    val personDate = cursor.getString(4)
                    try {
                        returnList.add(
                                PersonModel(
                                        personFName,
                                        personSName,
                                        personIDNP,
                                        personVaccine,
                                        personDate
                                )
                        )
                    } catch (e: VaccineDoesntExist) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return returnList
        }
    val allTests: MutableList<TestModel>?
        get() {
            var testModelList: MutableList<TestModel>? = null
            val queryString = "SELECT * FROM $PERSON_TESTS"
            val db = this.readableDatabase
            val cursor = db.rawQuery(queryString, null)
            if (cursor.moveToFirst()) {
                testModelList = ArrayList()
                do {
                    val idnp = cursor.getString(0)
                    val date = cursor.getString(1)
                    val result = cursor.getString(2)
                    val antibodies = cursor.getString(3)

                    testModelList.add(
                            TestModel(
                                    idnp,
                                    result == "1",
                                    date,
                                    antibodies == "1"
                            )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return testModelList
        }

    fun getAll1Person(idnp: String?): MutableList<PersonModel>? {
        var returnList: MutableList<PersonModel> ?= null
        //get data from the database
        val queryString = "SELECT * FROM $PERSON_TABLE WHERE $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToFirst()) {
            returnList = ArrayList()
            //loop through results and create objects and insert into returnlist
            do {

                val personFName = cursor.getString(0)
                val personSName = cursor.getString(1)
                val personIDNP = cursor.getString(2)
                val personVaccine = cursor.getString(3)
                val personDate = cursor.getString(4)
                try {
                    returnList.add(
                            PersonModel(
                                    personFName,
                                    personSName,
                                    personIDNP,
                                    personVaccine,
                                    personDate
                            )
                    )
                } catch (e: VaccineDoesntExist) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return returnList
    }

    fun deleteOne(personModel: PersonModel) {
        //find and delete person, send false if not found
        val db = this.writableDatabase
        val queryString = "DELETE FROM " + PERSON_TABLE + " WHERE " + COLUMN_PERSON_IDNP + " = " +
                personModel.iDNP + " AND " + COLUMN_VACCINATION_DATE +
                " = '" + personModel.vaccDate + "'"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        cursor.close()
        db.close()
    }

    fun deleteTest(testModel: TestModel) {
        val db = this.readableDatabase
        val queryString = "DELETE FROM " + PERSON_TESTS + " WHERE " + COLUMN_PERSON_IDNP + " = " +
                testModel.idnp + " AND " + COLUMN_TEST_DATE + " = '" + testModel.testDate + "'"
        val cursor = db.rawQuery(queryString, null)
        cursor.moveToFirst()
        cursor.close()
        db.close()
    }

    fun getFromDb(idnp: String): PersonModel? {
        val queryString = "SELECT * FROM $PERSON_TABLE where $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToLast()) {
            val toReturn = PersonModel(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_SECONDNAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IDNP)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_VACCINE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_VACCINATION_DATE)),
            )
            cursor.close()
            db.close()
            return toReturn
        }
        cursor.close()
        db.close()
        return null
    }

    fun getTestFromDb(idnp: String): TestModel? {
        val queryString = "SELECT * FROM $PERSON_TESTS where $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToLast()) {
            val toReturn = TestModel(
                    cursor.getString(cursor.getColumnIndex(COLUMN_PERSON_IDNP)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TEST_RESULT)) == "1",
                    cursor.getString(cursor.getColumnIndex(COLUMN_TEST_DATE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ANTIBODIES)) == "1"
            )
            cursor.close()
            db.close()
            return toReturn
        }
        cursor.close()
        db.close()
        return null
    }

    fun getLastTest(idnp: String): Array<String?>?{
        val toReturn = arrayOfNulls<String>(2)
        val queryString = "SELECT * FROM $PERSON_TESTS WHERE $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToLast()){
            toReturn[0] = cursor.getString(cursor.getColumnIndex(COLUMN_ANTIBODIES))
            toReturn[1] = cursor.getString(cursor.getColumnIndex(COLUMN_TEST_DATE))
            cursor.close()
            db.close()
            return toReturn
        }
        cursor.close()
        db.close()
        return null
    }

    fun getLastTestResult(idnp: String): Boolean?{
        val queryString = "SELECT * FROM $PERSON_TESTS WHERE $COLUMN_PERSON_IDNP = $idnp"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToLast()){
            val ret = cursor.getString(cursor.getColumnIndex(COLUMN_TEST_RESULT)).equals("1")
            cursor.close()
            db.close()
            return ret
        }
        cursor.close()
        db.close()
        return null
    }

    companion object {
        const val PERSON_TABLE = "PERSON_TABLE"
        const val PERSON_TESTS = "PERSON_TESTS"
        const val COLUMN_PERSON_FIRSTNAME = "PERSON_FIRSTNAME"
        const val COLUMN_PERSON_SECONDNAME = "PERSON_SECONDNAME"
        const val COLUMN_PERSON_IDNP = "PERSON_IDNP"
        const val COLUMN_PERSON_VACCINE = "PERSON_VACCINE"
        const val COLUMN_VACCINATION_DATE = "PERSON_VACC_DATE"
        const val COLUMN_TEST_DATE = "PERSON_TEST_DATE"
        const val COLUMN_TEST_RESULT = "PERSON_TEST_RESULT"
        const val COLUMN_ANTIBODIES = "PERSON_ANTIBODIES"
    }
}