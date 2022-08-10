package com.siddydevelops.vitbit.backend

import android.content.Context
import android.util.Log
import jxl.Workbook
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReadMenuXls(val context: Context, val displayDataInterface: DisplayDataInterface) {

    var scheduledDates: ArrayList<String> = ArrayList()
    var breakFastItems: ArrayList<String> = ArrayList()
    var lunchItems: ArrayList<String> = ArrayList()
    var snacksItems: ArrayList<String> = ArrayList()
    var dinnerItems: ArrayList<String> = ArrayList()

    public fun readMenuFromExcel() {
        try {
            val assetManager = context.assets
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
            displayDataInterface.displayMenu()
            //getByDate(curDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface DisplayDataInterface {
        fun displayMenu()
    }

}