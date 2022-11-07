package com.example.cookroom

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.util.*

//менеджер уведомлений
class NotificationManager() {
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "CookRoom";
            val description = "Ваши продукты заканчиваются! Не забудьте сходить в магазин!";
            val importance = NotificationManager.IMPORTANCE_HIGH;
            val channel = NotificationChannel("CookRoom", name, importance);
            channel.description = description;
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel);
        }
    }
    //отключение уведомлений
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
            notificationManager.deleteNotificationChannel("CookRoom")
        }
        //Toast.makeText(context, "Alarm Cancelled", Toast.LENGTH_SHORT).show()
    }
    //Установка уведомлений
    fun setAlarm(context: Context, time:String) {
        if (time != "null") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val times = time.split(":").toTypedArray()
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, times[0].toInt());
                calendar.set(Calendar.MINUTE,times[1].toInt());
                calendar.set(Calendar.SECOND,times[2].toInt());
                calendar.set(Calendar.MILLISECOND,0);
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
                val intent = Intent(context, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )
                Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
