package com.example.cookroom

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

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

    var PREF_NAME = "LOGIN"
    var LOGIN =  "IS_LOGIN"
    var EMAIL = "EMAIL"
    var ID = "ID"
    var TIME = "TIME"

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
    fun checkLogin() {
        if (!isLogin()!!) {
            var i = Intent(context, LoginActivity::class.java)
            (context as MainActivity).finish()
            context!!.startActivity(i)

        }
    }
    fun getUserDetails(): HashMap<String, String> {
        var user : HashMap<String, String> = HashMap()
        user.put(EMAIL, sharedPreferences?.getString(EMAIL, null).toString())
        user.put(ID, sharedPreferences?.getString(ID, null).toString())
        user.put(TIME, sharedPreferences?.getString(TIME, null).toString())
        return user
    }
    fun logout() {
        editor?.clear()
        editor?.commit()
        var i = Intent(context, LoginActivity::class.java)
        (context as MainActivity).finish()
        context!!.startActivity(i)
        notificationManager.cancelAlarm(context!!)
    }
}