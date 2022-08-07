package com.siddydevelops.vitbit.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.siddydevelops.vitbit.R
import com.siddydevelops.vitbit.backend.WidgetService
import com.siddydevelops.vitbit.others.Constants
import jxl.Workbook
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


/**
 * Implementation of App Widget functionality.
 */
class MessWidget : AppWidgetProvider() {

    private var curTime by Delegates.notNull<Int>()
    private var service: PendingIntent? = null

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        curTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val m = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        val TIME = Calendar.getInstance()
//        TIME[Calendar.MINUTE] = 0
//        TIME[Calendar.SECOND] = 0
//        TIME[Calendar.MILLISECOND] = 0
//
//        val i = Intent(context, WidgetService::class.java)
//
//        if (service == null) {
//            service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_IMMUTABLE)
//        }
//
//        m.setRepeating(AlarmManager.RTC, TIME.time.time, (1000 * 1).toLong(), service)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        val m = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (service != null) {
            m.cancel(service)
        }
    }

//    override fun onReceive(context: Context?, intent: Intent?) {
//        super.onReceive(context, intent)
//    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val scheduledDates: ArrayList<String> = ArrayList()
    val breakFastItems: ArrayList<String> = ArrayList()
    val lunchItems: ArrayList<String> = ArrayList()
    val snacksItems: ArrayList<String> = ArrayList()
    val dinnerItems: ArrayList<String> = ArrayList()
    var dataRow = 0
    val curTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val regex = Regex(Constants.SCHEDULE_REGEX)
    val timeRegex = Regex(Constants.TIME_REGEX)

    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.mess_widget)
    //views.setTextViewText(R.id.itemsTV, widgetText)
    val intent = Intent(context, MessActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    views.setOnClickPendingIntent(R.id.layoutWidget, pendingIntent)
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
        val date = SimpleDateFormat("dd", Locale.US).format(Date())
        var row = 1
        for (dates in scheduledDates) {
            val matches = regex.findAll(dates)
            val scheduleTimes = matches.map { it.groupValues[1] }.toList()
            for (time in scheduleTimes) {
                if ((timeRegex.find(time)?.value ?: false) == date) {
                    dataRow = row-1
                }
            }
            row += 1
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    when (curTime) {
        in 2..9 -> {
            views.setTextViewText(R.id.headerTV, "Breakfast")
            views.setTextViewText(R.id.itemsTV, breakFastItems[dataRow])
        }
        in 10..15 -> {
            views.setTextViewText(R.id.headerTV, "Lunch")
            views.setTextViewText(R.id.itemsTV, lunchItems[dataRow])
        }
        in 15..18 -> {
            views.setTextViewText(R.id.headerTV, "Snacks")
            views.setTextViewText(R.id.itemsTV, snacksItems[dataRow])
        }
        in 19..22 -> {
            views.setTextViewText(R.id.headerTV, "Dinner")
            views.setTextViewText(R.id.itemsTV, dinnerItems[dataRow])
        } else -> {
            views.setTextViewText(R.id.headerTV, "Breakfast")
            views.setTextViewText(R.id.itemsTV, breakFastItems[dataRow])
        }
    }
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}