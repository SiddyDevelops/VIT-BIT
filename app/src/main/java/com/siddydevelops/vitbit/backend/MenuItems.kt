package com.siddydevelops.vitbit.backend

data class MenuItems(
    var scheduledDates: ArrayList<String>,
    var breakFastItems: ArrayList<String>,
    var lunchItems: ArrayList<String>,
    var snacksItems: ArrayList<String>,
    var dinnerItems: ArrayList<String>
)
