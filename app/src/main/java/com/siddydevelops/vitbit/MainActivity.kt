package com.siddydevelops.vitbit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jxl.Workbook

class MainActivity : AppCompatActivity() {

    private var scheduledDates: ArrayList<String> = ArrayList()
    private var breakFastItems: ArrayList<String> = ArrayList()
    private var lunchItems: ArrayList<String> = ArrayList()
    private var snacksItems: ArrayList<String> = ArrayList()
    private var dinnerItems: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val assetManager = assets
            val inputStream = assetManager.open("mess_schedule.xls")
            val workbook = Workbook.getWorkbook(inputStream)
            val sheet = workbook.getSheet(0)
            val rows = sheet.rows
            val col = sheet.columns
            for (i in 0 until rows) {
                for(j in 0 until col) {
                    val cell = sheet.getCell(0,j+1)
                    scheduledDates.add(cell.contents)
                }
            }
            for (i in 0 until rows) {
                for(j in 0 until col) {
                    val cell = sheet.getCell(1,j+1)
                    breakFastItems.add(cell.contents)
                }
            }
            for (i in 0 until rows) {
                for(j in 0 until col) {
                    val cell = sheet.getCell(2,j+1)
                    lunchItems.add(cell.contents)
                }
            }
            for (i in 0 until rows) {
                for(j in 0 until col) {
                    val cell = sheet.getCell(3,j+1)
                    snacksItems.add(cell.contents)
                }
            }
            for (i in 0 until rows) {
                for(j in 0 until col) {
                    val cell = sheet.getCell(4,j+1)
                    dinnerItems.add(cell.contents)
                }
            }
            Log.d("ExcelData:: ", scheduledDates.toString())
            Log.d("ExcelData:: ", breakFastItems.toString())
            Log.d("ExcelData:: ", lunchItems.toString())
            Log.d("ExcelData:: ", snacksItems.toString())
            Log.d("ExcelData:: ", dinnerItems.toString())
        }catch(e: Exception) {
            e.printStackTrace()
        }
    }
}