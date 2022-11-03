package com.example.cookroom

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat.getSystemService
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.HashMap


class UserFragment : Fragment() {
    var email : TextView? = null
    var logout : Button? = null
    var timePicker : TimePicker? = null
    var notifOnOff : SwitchCompat? = null
    var saveTime : Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_user, container, false)
        timePicker = view.findViewById(R.id.timePicker)
        notifOnOff = view.findViewById(R.id.notif)
        email = view?.findViewById(R.id.email)
        logout = view?.findViewById(R.id.logout)
        saveTime = view?.findViewById(R.id.saveTime)

        timePicker?.setIs24HourView(true)

        val sessionManager = SessionManager(requireContext())
        sessionManager.sessionManage(requireContext())
        sessionManager.checkLogin()
        val user : HashMap<String, String> = sessionManager.getUserDetails()
        val mEmail = user.get(sessionManager.EMAIL)
        val user_id = user.get(sessionManager.ID)
        val time = user.get(sessionManager.TIME)

        val sharedPreferences = requireContext().getSharedPreferences("User_Id", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", user_id).apply()
        email?.setText(mEmail)

        logout?.setOnClickListener {
            sessionManager.logout()
        }

        if (time != "null") {
            notifOnOff?.isChecked = true
            timePicker?.visibility = View.VISIBLE
            saveTime?.visibility = View.VISIBLE
            val times = time!!.split(":").toTypedArray()
            timePicker?.hour = times[0].toInt()
            timePicker?.minute = times[1].toInt()

        }

        notifOnOff?.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                timePicker?.visibility = View.VISIBLE
                saveTime?.visibility = View.VISIBLE
            }else{
                timePicker?.visibility = View.GONE
                saveTime?.visibility = View.GONE
            }
        }


        saveTime?.setOnClickListener{
            var calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker!!.getHour());
            calendar.set(Calendar.MINUTE, timePicker!!.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Toast.makeText(requireActivity(), calendar.toString(), Toast.LENGTH_LONG).show()
        }
        return view
    }



}