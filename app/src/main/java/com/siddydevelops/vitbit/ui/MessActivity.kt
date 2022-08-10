package com.siddydevelops.vitbit.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.siddydevelops.vitbit.R
import com.siddydevelops.vitbit.backend.MenuItems
import com.siddydevelops.vitbit.backend.ReadMenuXls
import com.siddydevelops.vitbit.others.Constants.BLOCK_PREFERENCE
import com.siddydevelops.vitbit.others.Constants.MESS_PREFERENCE
import com.siddydevelops.vitbit.others.Constants.MH_G
import com.siddydevelops.vitbit.others.Constants.MH_K
import com.siddydevelops.vitbit.others.Constants.MH_L
import com.siddydevelops.vitbit.others.Constants.MH_Q
import com.siddydevelops.vitbit.others.Constants.NON_VEG_MESS
import com.siddydevelops.vitbit.others.Constants.SCHEDULE_REGEX
import com.siddydevelops.vitbit.others.Constants.SHARED_PREFERENCES
import com.siddydevelops.vitbit.others.Constants.SPECIAL_MESS
import com.siddydevelops.vitbit.others.Constants.TIME_REGEX
import com.siddydevelops.vitbit.others.Constants.VEG_MESS
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class MessActivity : AppCompatActivity(), ReadMenuXls.DisplayDataInterface {

    private lateinit var menuItem: MenuItems
    private val regex = Regex(SCHEDULE_REGEX)
    private val timeRegex = Regex(TIME_REGEX)
    private var cal: Calendar = Calendar.getInstance()
    private var monthInt: Int = -1
    private lateinit var curDate: String
    private var month: String = ""
    private var year: String = ""
    private lateinit var date: Date
    private var curTime by Delegates.notNull<Int>()

    private lateinit var calenderIV: LinearLayout
    private lateinit var messSelectionLL: LinearLayout
    private lateinit var blockSelectionLL: LinearLayout
    private lateinit var calTV: TextView
    private lateinit var breakfastTV: TextView
    private lateinit var lunchTV: TextView
    private lateinit var snacksTV: TextView
    private lateinit var dinnerTV: TextView
    private lateinit var curDateTV: TextView
    private lateinit var messTV: TextView
    private lateinit var blockTV: TextView
    private lateinit var breakfastCV: CardView
    private lateinit var lunchCV: CardView
    private lateinit var snacksCV: CardView
    private lateinit var dinnerCV: CardView

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var readMenuXls: ReadMenuXls

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mess)

        calenderIV = findViewById(R.id.calenderIV)
        messSelectionLL = findViewById(R.id.messSelectionLL)
        blockSelectionLL = findViewById(R.id.blockSelectionLL)
        calTV = findViewById(R.id.calTV)
        breakfastTV = findViewById(R.id.breakfastTV)
        lunchTV = findViewById(R.id.lunchTV)
        snacksTV = findViewById(R.id.snacksTV)
        dinnerTV = findViewById(R.id.dinnerTV)
        curDateTV = findViewById(R.id.curDateTV)
        messTV = findViewById(R.id.messTV)
        blockTV = findViewById(R.id.blockTV)
        breakfastCV = findViewById(R.id.breakfastCV)
        lunchCV = findViewById(R.id.lunchCV)
        snacksCV = findViewById(R.id.snacksCV)
        dinnerCV = findViewById(R.id.dinnerCV)

        date = Calendar.getInstance().time
        month = SimpleDateFormat("MMMM", Locale.US).format(Date())
        year = SimpleDateFormat("yyyy", Locale.US).format(Date())
        monthInt = Integer.parseInt(SimpleDateFormat("MM", Locale.US).format(cal.time).format(date))
        curDate = SimpleDateFormat("dd", Locale.US).format(Date())
        calTV.text = getString(R.string.date_year,curDate,month,year)
        curTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        sharedPreferences = this.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE)
        if(!sharedPreferences.getString(BLOCK_PREFERENCE,"defaultName").equals("defaultName")) {
            blockTV.text = sharedPreferences.getString(BLOCK_PREFERENCE,"defaultName")
        }
        if(!sharedPreferences.getString(MESS_PREFERENCE,"defaultName").equals("defaultName")) {
            messTV.text = sharedPreferences.getString(MESS_PREFERENCE,"defaultName")
        }

        readMenuXls = ReadMenuXls(this,this)

        calenderIV.setOnClickListener {
            setTVDate()
        }

        messTV.text = SPECIAL_MESS
        blockTV.text = MH_Q

        //registerForContextMenu(messSelectionLL)
        messSelectionLL.setOnClickListener {
            messSelectionLL.showContextMenu()
        }

        //registerForContextMenu(blockSelectionLL)
        blockSelectionLL.setOnClickListener {
            blockSelectionLL.showContextMenu()
        }

        when (curTime) {
            in 2..9 -> breakfastCV.setCardBackgroundColor(getColor(R.color.card_bg_highlight))
            in 10..15 -> lunchCV.setCardBackgroundColor(getColor(R.color.card_bg_highlight))
            in 15..18 -> snacksCV.setCardBackgroundColor(getColor(R.color.card_bg_highlight))
            in 19..22 -> dinnerCV.setCardBackgroundColor(getColor(R.color.card_bg_highlight))
        }

        readMenuXls.readMenuFromExcel()
    }

    private fun getByDate(date: String) {
        var row = 1
        for (dates in menuItem.scheduledDates) {
            val matches = regex.findAll(dates)
            val scheduleTimes = matches.map { it.groupValues[1] }.toList()
            Log.d("TIMES", scheduleTimes.toString())
            for (time in scheduleTimes) {
                if ((timeRegex.find(time)?.value ?: false) == date) {
                    Log.d("ROW::", "DATA EXTRACT HERE:: $row")
                    breakfastTV.text = menuItem.breakFastItems[row-1]
                    lunchTV.text = menuItem.lunchItems[row-1]
                    snacksTV.text = menuItem.snacksItems[row-1]
                    dinnerTV.text = menuItem.dinnerItems[row-1]
                }
            }
            row += 1
        }
    }

    private fun setTVDate() {
        DatePickerDialog(this,
            { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                if(monthOfYear + 1 <= monthInt) {
                    updateDateInView()
                } else {
                    Toast.makeText(this@MessActivity,"Cannot select future dates.", Toast.LENGTH_SHORT).show()
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun updateDateInView() {
        curDate = SimpleDateFormat("dd", Locale.US).format(cal.time)
        month = SimpleDateFormat("MMMM", Locale.US).format(cal.time)
        calTV.text = getString(R.string.date_year,curDate,month,year)
        curDateTV.text = getString(R.string.date_month,curDate,month)
        getByDate(curDate)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        if(v == messSelectionLL) {
            menu?.add(0, v.id,0,VEG_MESS)
            menu?.add(0, v.id,0,NON_VEG_MESS)
            menu?.add(0, v.id,0,SPECIAL_MESS)
        } else if(v == blockSelectionLL) {
            menu?.add(0, v.id,0,MH_Q)
            menu?.add(0, v.id,0,MH_L)
            menu?.add(0, v.id,0,MH_G)
            menu?.add(0, v.id,0,MH_K)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.title) {
            "Veg Mess" -> messTV.text = VEG_MESS
            "Non-Veg Mess" -> messTV.text = NON_VEG_MESS
            "Special Mess" -> messTV.text = SPECIAL_MESS
            "MH-Q Block" -> blockTV.text = MH_Q
            "MH-L Block" -> blockTV.text = MH_L
            "MH-G Block" -> blockTV.text = MH_G
            "MH-K Block" -> blockTV.text = MH_K
        }
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(BLOCK_PREFERENCE,blockTV.text.toString())
        editor.putString(MESS_PREFERENCE,messTV.text.toString())
        editor.apply()
        return true
    }

    override fun displayMenu(menuItems: MenuItems) {
        menuItem = menuItems
        getByDate(curDate)
    }
}