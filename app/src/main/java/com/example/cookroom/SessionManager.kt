package com.example.cookroom

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

//Пользовательская сессия
class SessionManager(context: Context) {
    var sharedPreferences : SharedPreferences? = null
    var editor : SharedPreferences.Editor? = null
    var context: Context? = null
    var PRIVATE_MOD = 0
    var notificationManager = NotificationManager()
    fun sessionManage(context: Context) {
        this.context = context
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MOD)
        editor = sharedPreferences?.edit()
    }

    var LOGIN =  "IS_LOGIN"
    var EMAIL = "EMAIL"
    var ID = "ID"
    var TIME = "TIME"

    //создание сессии при входе
    fun createSession(context: Context, email : String, id: String, time: String) {
        editor?.putBoolean(LOGIN, true)
        editor?.putString(EMAIL, email)
        editor?.putString(ID, id)
        editor?.putString(TIME, time)
        editor?.apply()
        notificationManager.createNotificationChannel(context)
        notificationManager.setAlarm(context, time)
    }

    fun isLogin(): Boolean? {
        return sharedPreferences?.getBoolean(LOGIN, false)
    }
    //проверка авторизованности
    fun checkLogin() {
        if (!isLogin()!!) {
            var i = Intent(context, LoginActivity::class.java)
            (context as MainActivity).finish()
            context!!.startActivity(i)
        }
    }

    //пользовательская информация
    fun getUserDetails(): HashMap<String, String> {
        var user : HashMap<String, String> = HashMap()
        user.put(EMAIL, sharedPreferences?.getString(EMAIL, null).toString())
        user.put(ID, sharedPreferences?.getString(ID, null).toString())
        user.put(TIME, sharedPreferences?.getString(TIME, null).toString())
        return user
    }
    //выход из аккаунта
    fun logout() {
        editor?.clear()
        editor?.commit()
        var i = Intent(context, LoginActivity::class.java)
        (context as MainActivity).finish()
        context!!.startActivity(i)
        notificationManager.cancelAlarm(context!!)
    }
    //Обновление сессии
    fun updateSession(context: Context, id: String, email: String, time: String?){
        editor?.clear()
        editor?.commit()
        notificationManager.cancelAlarm(context)
        sharedPreferences = context.getSharedPreferences("LOGIN", PRIVATE_MOD)
        editor = sharedPreferences?.edit()
        editor?.putBoolean(LOGIN, true)
        editor?.putString(EMAIL, email)
        editor?.putString(ID, id)
        editor?.putString(TIME, time)
        editor?.apply()
        if (time != null){
            notificationManager.createNotificationChannel(context)
            notificationManager.setAlarm(context, time)
        }
    }
}