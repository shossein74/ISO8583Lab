package com.hossein.dev.iso8583lab.util

import java.time.LocalDateTime

object CalendarUtil {
    fun gregorianToJalali(gYear: Int, gMonth: Int, gDay: Int): Triple<Int, Int, Int> {
        val gDaysInMonth = intArrayOf(31,28,31,30,31,30,31,31,30,31,30,31)
        val gy = gYear - 1600
        val gm = gMonth - 1   // 0-based
        val gd = gDay - 1     // 0-based

        var gDayNo = 365 * gy + (gy + 3) / 4 - (gy + 99) / 100 + (gy + 399) / 400
        for (i in 0 until gm) gDayNo += gDaysInMonth[i]
        // leap year in Gregorian
        val isLeapG = (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)
        if (gm > 1 && isLeapG) gDayNo += 1
        gDayNo += gd

        var jDayNo = gDayNo - 79
        val jNp = jDayNo / 12053 // 33-year cycles (365*33+8)
        jDayNo %= 12053

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461) // 4-year cycles
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        val jMonthDays = intArrayOf(31,31,31,31,31,31,30,30,30,30,30,29)
        var jm = 0
        while (jm < 11 && jDayNo >= jMonthDays[jm]) {
            jDayNo -= jMonthDays[jm]
            jm++
        }
        val jd = jDayNo + 1
        return Triple(jy, jm + 1, jd)
    }

    fun getJalaliDateTimeForIso(dt: LocalDateTime): String {
        val (_, jm, jd) = gregorianToJalali(dt.year, dt.monthValue, dt.dayOfMonth)
        return "%02d%02d%02d%02d%02d".format( jm, jd, dt.hour, dt.minute, dt.second)
    }
}