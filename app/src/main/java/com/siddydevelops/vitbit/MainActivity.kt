package com.siddydevelops.vitbit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.FileAsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import jxl.Workbook
import jxl.WorkbookSettings
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val EXCEL_URL =
        "https://github.com/SiddyDevelops/VIT-BIT/blob/master/app/src/main/assets/mess_schedule.xlsx?raw=true"
    private lateinit var client: AsyncHttpClient
    private lateinit var workBook: Workbook
    private var days: ArrayList<String> = ArrayList()

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
            var xx = ""
            for (i in 0 until rows) {
                for(i in 0 until col) {
                    val cell = sheet.getCell(0,i)
                    xx += cell.contents
                }
            }
            Log.d("ExcelData:: ", xx)
        }catch(e: Exception) {
            e.printStackTrace()
        }

        client = AsyncHttpClient()
//        client[EXCEL_URL, object : FileAsyncHttpResponseHandler(this) {
//            override fun onSuccess(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                file: java.io.File?
//            ) {
//                Toast.makeText(applicationContext,"Download has been Successful!", Toast.LENGTH_SHORT).show()
//                val workbookSettings = WorkbookSettings()
//                workbookSettings.gcDisabled = true
//                if(file != null) {
//                    try {
//                        workBook = Workbook.getWorkbook(file)
//                        val sheet = workBook.getSheet(0)
//                        for (i in 0 until sheet.rows) {
//                            val rows = sheet.getRow(i)
//                            days.add(rows[0].contents)
//                        }
//                        Log.d("ExcelData:: ",days.toString())
//                    } catch(e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun onFailure(
//                statusCode: Int,
//                headers: Array<out Header>?,
//                throwable: Throwable?,
//                file: java.io.File?
//            ) {
//                Log.d("IOError:: ",throwable.toString())
//                Toast.makeText(applicationContext,"Download has been Failed!",Toast.LENGTH_SHORT).show()
//            }
//        }]
    }
}