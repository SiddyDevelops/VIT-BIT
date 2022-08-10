package com.siddydevelops.vitbit.backend

import android.content.Context
import jxl.Workbook
import kotlin.collections.ArrayList

class ReadMenuXls(private val context: Context, private val displayDataInterface: DisplayDataInterface) {

    private var menuItems: MenuItems = MenuItems(ArrayList(),ArrayList(),ArrayList(),ArrayList(),ArrayList())

    fun readMenuFromExcel() {
        try {
            val assetManager = context.assets
            val inputStream = assetManager.open("mess_schedule.xls")
            val workbook = Workbook.getWorkbook(inputStream)
            val sheet = workbook.getSheet(0)                        //Two sheets per tt
            val rows = sheet.rows
            //val col = sheet.columns
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(0, j + 1)       //Date and Days
                menuItems.scheduledDates.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(1, j + 1)      //Breakfast
                menuItems.breakFastItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(2, j + 1)      //Lunch
                menuItems.lunchItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(3, j + 1)      //Snacks
                menuItems.snacksItems.add(cell.contents)
            }
            for (j in 0 until rows - 1) {
                val cell = sheet.getCell(4, j + 1)     //Dinner
                menuItems.dinnerItems.add(cell.contents)
            }
            displayDataInterface.displayMenu(menuItems)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface DisplayDataInterface {
        fun displayMenu(menuItems: MenuItems)
    }
}