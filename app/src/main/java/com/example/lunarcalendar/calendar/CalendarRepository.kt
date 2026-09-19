package com.example.lunarcalendar.calendar

import com.nlf.calendar.Solar
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.format.TextStyle
import java.util.Locale

data class CalendarDay(
    val date: LocalDate,
    val solarText: String,
    val lunarText: String,
    val yearText: String,
    val solarTermText: String,
    val festivalText: String,
    val isSolarTermToday: Boolean,
) {
    val isWeekend: Boolean get() = date.dayOfWeek.value >= 6
    val weekdayText: String get() = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.SIMPLIFIED_CHINESE)
    val shortSolarText: String get() = "${date.monthValue}月${date.dayOfMonth}日"
    val fullSolarText: String get() = "${date.year}年${date.monthValue}月${date.dayOfMonth}日"
}

/** Offline civil-calendar information. Every date is interpreted in Beijing time. */
object CalendarRepository {
    val zone: ZoneId = ZoneId.of("Asia/Shanghai")

    private val weekdays = listOf("一", "二", "三", "四", "五", "六", "日")
    private val traditionalFestivals = setOf(
        "春节", "元宵节", "端午节", "七夕节", "中秋节", "重阳节", "腊八节", "除夕",
    )

    fun today(clock: Clock = Clock.systemUTC()): CalendarDay =
        forDate(LocalDate.now(clock.withZone(zone)))

    fun forDate(date: LocalDate): CalendarDay {
        // Pass calendar components rather than java.util.Date so the device timezone
        // cannot alter the day supplied to the lunar library.
        val lunar = Solar.fromYmd(date.year, date.monthValue, date.dayOfMonth).lunar
        val currentTerm = lunar.jieQi
        val solarTermText = if (currentTerm.isNotEmpty()) {
            "今日$currentTerm"
        } else {
            val nextTerm = lunar.getNextJieQi(true)
            val nextSolar = nextTerm.solar
            val nextDate = LocalDate.of(nextSolar.year, nextSolar.month, nextSolar.day)
            val days = ChronoUnit.DAYS.between(date, nextDate)
            "距${nextTerm.name}${days}天"
        }

        val festivals = lunar.festivals.filter { it in traditionalFestivals }.toMutableList()
        // Regional customs differ; label both common dates rather than silently
        // presenting one regional convention as universal. Negative months are leap months.
        if (lunar.month == 12) {
            when (lunar.day) {
                23 -> festivals.add("小年（北方）")
                24 -> festivals.add("小年（南方）")
            }
        }

        return CalendarDay(
            date = date,
            solarText = "${date.year}年${date.monthValue}月${date.dayOfMonth}日 星期${weekdays[date.dayOfWeek.value - 1]}",
            lunarText = "${lunar.monthInChinese}月${lunar.dayInChinese}",
            // Use the civil lunar year beginning on Spring Festival, not the
            // alternative astrology convention that changes the year at Start of Spring.
            yearText = "${lunar.yearInGanZhi}${lunar.yearShengXiao}年",
            solarTermText = solarTermText,
            festivalText = festivals.joinToString(" · "),
            isSolarTermToday = currentTerm.isNotEmpty(),
        )
    }

    fun nextMidnightMillis(clock: Clock = Clock.systemUTC()): Long =
        LocalDate.now(clock.withZone(zone))
            .plusDays(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
}
