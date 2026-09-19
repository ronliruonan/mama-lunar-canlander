package com.example.lunarcalendar.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lunarcalendar.calendar.CalendarRepository

object WidgetRefreshScheduler {
    private fun pendingIntent(context: Context) = PendingIntent.getBroadcast(
        context, 2,
        Intent(context, LunarWidgetProvider::class.java).setAction(LunarWidgetProvider.ACTION_REFRESH),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    fun schedule(context: Context) {
        // Daily local calculation; no exact-alarm permission or persistent service.
        // The system's 30-minute widget updates also recover missed refreshes.
        val alarms = context.getSystemService(AlarmManager::class.java)
        alarms.setAndAllowWhileIdle(
            AlarmManager.RTC, CalendarRepository.nextMidnightMillis(), pendingIntent(context)
        )
    }

    fun cancel(context: Context) {
        context.getSystemService(AlarmManager::class.java).cancel(pendingIntent(context))
    }
}

class CalendarChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action in setOf(
                Intent.ACTION_BOOT_COMPLETED, Intent.ACTION_TIME_CHANGED,
                Intent.ACTION_TIMEZONE_CHANGED, Intent.ACTION_MY_PACKAGE_REPLACED
            )) LunarWidgetProvider.refreshAll(context)
    }
}
