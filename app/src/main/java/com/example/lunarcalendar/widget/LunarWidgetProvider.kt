package com.example.lunarcalendar.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews
import com.example.lunarcalendar.MainActivity
import com.example.lunarcalendar.R
import com.example.lunarcalendar.calendar.CalendarRepository

class LunarWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        render(context, manager, ids)
        WidgetRefreshScheduler.schedule(context)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context, manager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle
    ) {
        render(context, manager, intArrayOf(appWidgetId))
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) refreshAll(context)
    }

    override fun onDisabled(context: Context) {
        WidgetRefreshScheduler.cancel(context)
    }

    companion object {
        const val ACTION_REFRESH = "com.example.lunarcalendar.REFRESH_WIDGETS"

        fun refreshAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, LunarWidgetProvider::class.java))
            if (ids.isEmpty()) {
                WidgetRefreshScheduler.cancel(context)
                return
            }
            render(context, manager, ids)
            WidgetRefreshScheduler.schedule(context)
        }

        private fun render(context: Context, manager: AppWidgetManager, ids: IntArray) {
            val day = CalendarRepository.today()
            val weekday = SpannableString(day.weekdayText).apply {
                if (day.isWeekend) {
                    setSpan(ForegroundColorSpan(context.getColor(R.color.widget_weekend)),
                        length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            val openApp = PendingIntent.getActivity(
                context, 0, Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            val refresh = PendingIntent.getBroadcast(
                context, 1, Intent(context, LunarWidgetProvider::class.java).setAction(ACTION_REFRESH),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            ids.forEach { id ->
                val views = RemoteViews(context.packageName, R.layout.lunar_widget).apply {
                    setTextViewText(R.id.widget_lunar, day.lunarText)
                    setTextViewText(R.id.widget_weekday, weekday)
                    setTextViewText(R.id.widget_solar, day.fullSolarText)
                    // Keep the footer to one topic; full calendar details remain in the app.
                    setTextViewText(R.id.widget_term,
                        if (day.festivalText.isNotBlank()) "今日${day.festivalText}" else day.solarTermText)
                    setTextColor(R.id.widget_term, context.getColor(
                        if (day.isSolarTermToday || day.festivalText.isNotBlank()) R.color.widget_accent
                        else R.color.widget_ink))
                    setOnClickPendingIntent(R.id.widget_root, openApp)
                    setOnClickPendingIntent(R.id.widget_refresh, refresh)
                }
                manager.updateAppWidget(id, views)
            }
        }
    }
}
