package com.example.cookroom

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.sql.Time
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class NotificationManager() {
    fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var name = "foxandroidReminderChannel";
            var description = "Channel For Alarm Manager";
            var importance = NotificationManager.IMPORTANCE_HIGH;
            var channel = NotificationChannel("cookroom", name, importance);
            channel.setDescription(description);

            var notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel);


        }
    }
    fun cancelAlarm(context: Context) {
        var alarmManager : AlarmManager?=null

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        }
        alarmManager?.cancel(pendingIntent)
        var notificationManager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel("cookroom")
        }
        Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }
    fun setAlarm(context: Context, time:String) {
        if (time != "null") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val times = time.split(":").toTypedArray()
                var calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, times[0].toInt());
                calendar.set(Calendar.MINUTE,times[0].toInt());
                calendar.set(Calendar.SECOND,times[0].toInt());
                calendar.set(Calendar.MILLISECOND,0);
                var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )
                Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()

            }
            /*
            var calendar = Calendar.getInstance()
            calendar.time = Date.from(dateTime!!.atStartOfDay(ZoneId.systemDefault()).toInstant())
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE,35);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent
            )*/
        }

        //Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

}