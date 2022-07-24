package com.siddydevelops.vitbit.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.siddydevelops.vitbit.R
import com.siddydevelops.vitbit.others.Constants.SCHEDULE_REGEX
import com.siddydevelops.vitbit.others.Constants.TIME_REGEX
import jxl.Workbook

class MessActivity : AppCompatActivity() {

    private var scheduledDates: ArrayList<String> = ArrayList()
    private var breakFastItems: ArrayList<String> = ArrayList()
    private var lunchItems: ArrayList<String> = ArrayList()
    private var snacksItems: ArrayList<String> = ArrayList()
    private var dinnerItems: ArrayList<String> = ArrayList()

    private val regex = Regex(SCHEDULE_REGEX)
    private val timeRegex = Regex(TIME_REGEX)

    private lateinit var breakFastTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess)

        breakFastTV = findViewById(R.id.breakFastTV)

        try {
            val assetManager = assets
            val inputStream = assetManager.open("mess_schedule.xls")
            val workbook = Workbook.getWorkbook(inputStream)
            val sheet = workbook.getSheet(0)                        //Two sheets per tt
            val rows = sheet.rows
            val col = sheet.columns
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(0, j + 1)       //Date and Days
                scheduledDates.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(1, j + 1)      //Breakfast
                breakFastItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(2, j + 1)      //Lunch
                lunchItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(3, j + 1)      //Snacks
                snacksItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(4, j + 1)     //Dinner
                dinnerItems.add(cell.contents)
            }
            Log.d("ExcelData:: ", scheduledDates.toString())
            Log.d("ExcelData:: ", breakFastItems.toString())
            Log.d("ExcelData:: ", lunchItems.toString())
            Log.d("ExcelData:: ", snacksItems.toString())
            Log.d("ExcelData:: ", dinnerItems.toString())
            getByDate("07")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getByDate(date: String) {
        var row = 1
        for (dates in scheduledDates) {
            val matches = regex.findAll(dates)
            val scheduleTimes = matches.map { it.groupValues[1] }.toList()
            for (time in scheduleTimes) {
                if (timeRegex.find(time)!!.value == date) {
                    Log.d("ROW::", "DATA EXTRACT HERE:: $row")
                    // We require row + 1 to access data items
                }
            }
            row += 1
        }
    }

}