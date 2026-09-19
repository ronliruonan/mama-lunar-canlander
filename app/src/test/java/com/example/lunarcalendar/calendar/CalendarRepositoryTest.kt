package com.example.lunarcalendar.calendar

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class CalendarRepositoryTest {
    // Civil-date fixtures checked against Hong Kong Observatory conversion tables:
    // https://www.hko.gov.hk/tc/gts/time/calendar/pdf/files/2024.pdf
    // https://www.hko.gov.hk/tc/gts/time/calendar/pdf/files/2025.pdf
    // https://www.hko.gov.hk/tc/gts/time/calendar/pdf/files/2026.pdf

    @Test
    fun springFestivalStartsTheCivilLunarYear() {
        val day = date("2026-02-17")
        assertEquals("正月初一", day.lunarText)
        assertEquals("春节", day.festivalText)
        assertEquals("丙午马年", day.yearText)
        assertEquals("2026年2月17日 星期二", day.solarText)
        assertEquals("乙巳蛇年", date("2026-02-16").yearText)
    }

    @Test
    fun newYearsEveCanFallOnDayThirtyOrTwentyNine() {
        val thirty = date("2024-02-09")
        assertEquals("腊月三十", thirty.lunarText)
        assertEquals("除夕", thirty.festivalText)

        val twentyNine = date("2025-01-28")
        assertEquals("腊月廿九", twentyNine.lunarText)
        assertEquals("除夕", twentyNine.festivalText)
        assertEquals("正月初一", date("2025-01-29").lunarText)
    }

    @Test
    fun leapMonthIsClearlyDistinguishedAndRollsOverCorrectly() {
        assertEquals("六月三十", date("2025-07-24").lunarText)
        assertEquals("闰六月初一", date("2025-07-25").lunarText)
        assertEquals("闰六月廿九", date("2025-08-22").lunarText)
        assertEquals("七月初一", date("2025-08-23").lunarText)
    }

    @Test
    fun qingmingIsShownForItsWholeCalendarDay() {
        assertEquals("距清明1天", date("2024-04-03").solarTermText)
        assertEquals("今日清明", date("2024-04-04").solarTermText)
        assertEquals("距谷雨14天", date("2024-04-05").solarTermText)
    }

    @Test
    fun midAutumnAndWinterSolsticeMatchThe2026Almanac() {
        val midAutumn = date("2026-09-25")
        assertEquals("八月十五", midAutumn.lunarText)
        assertEquals("中秋节", midAutumn.festivalText)
        assertEquals("距冬至1天", date("2026-12-21").solarTermText)
        assertEquals("今日冬至", date("2026-12-22").solarTermText)
    }

    @Test
    fun ordinaryDatesHaveNoFestivalAndSmallNewYearIsRegionSpecific() {
        assertEquals("", date("2026-09-19").festivalText)
        assertEquals("小年（北方）", date("2025-01-22").festivalText)
        assertEquals("小年（南方）", date("2025-01-23").festivalText)
    }

    @Test
    fun todayChangesAtBeijingMidnightEvenWhenClockIsInAnotherZone() {
        val before = clock("2026-02-16T15:59:59Z", "America/Los_Angeles")
        val after = clock("2026-02-16T16:00:00Z", "America/Los_Angeles")
        assertEquals(LocalDate.of(2026, 2, 16), CalendarRepository.today(before).date)
        assertEquals("除夕", CalendarRepository.today(before).festivalText)
        assertEquals(LocalDate.of(2026, 2, 17), CalendarRepository.today(after).date)
        assertEquals("春节", CalendarRepository.today(after).festivalText)
    }

    @Test
    fun nextMidnightMeansTheNextDateBoundaryRatherThanTwentyFourHoursLater() {
        val afternoon = clock("2026-09-19T06:30:00Z")
        assertEquals(
            Instant.parse("2026-09-19T16:00:00Z").toEpochMilli(),
            CalendarRepository.nextMidnightMillis(afternoon),
        )
        val midnight = clock("2026-09-19T16:00:00Z")
        assertEquals(
            Instant.parse("2026-09-20T16:00:00Z").toEpochMilli(),
            CalendarRepository.nextMidnightMillis(midnight),
        )
    }

    @Test
    fun nextMidnightHandlesYearRollover() {
        assertEquals(
            Instant.parse("2026-12-31T16:00:00Z").toEpochMilli(),
            CalendarRepository.nextMidnightMillis(clock("2026-12-31T15:59:59Z")),
        )
        assertEquals(
            LocalDate.of(2027, 1, 1),
            CalendarRepository.today(clock("2026-12-31T16:00:00Z")).date,
        )
    }

    private fun date(value: String): CalendarDay = CalendarRepository.forDate(LocalDate.parse(value))

    private fun clock(value: String, zone: String = "UTC"): Clock =
        Clock.fixed(Instant.parse(value), if (zone == "UTC") ZoneOffset.UTC else ZoneId.of(zone))
}
