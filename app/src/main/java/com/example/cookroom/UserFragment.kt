package com.example.cookroom

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.example.cookroom.db.UserDbManager
import java.util.*
import kotlin.collections.HashMap

//Фрагмент личного кабинета
class UserFragment : Fragment() {
    var email : TextView? = null
    var logout : Button? = null
    var timePicker : TimePicker? = null
    var notifOnOff : SwitchCompat? = null
    var saveTime : Button? = null
    var userDbManager = UserDbManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
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
        val mEmail = user[sessionManager.EMAIL]
        val user_id = user[sessionManager.ID]
        val time = user[sessionManager.TIME]

        val sharedPreferences = requireContext().getSharedPreferences("User_Id", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", user_id).apply()
        email?.setText(mEmail)
        //кнопка выхода
        logout?.setOnClickListener {
            sessionManager.logout()
        }
        //показ часов
        if (time != "null") {
            notifOnOff?.isChecked = true
            timePicker?.visibility = View.VISIBLE
            saveTime?.visibility = View.VISIBLE
            val times = time!!.split(":").toTypedArray()
            timePicker?.hour = times[0].toInt()
            timePicker?.minute = times[1].toInt()

        }
        //Установка в автоматическое время при включении, выключение
        notifOnOff?.setOnCheckedChangeListener { buttonView, isChecked ->
            val sessionManager = SessionManager(requireContext())
            if(isChecked){
                timePicker?.visibility = View.VISIBLE
                saveTime?.visibility = View.VISIBLE
                userDbManager.turnOnAlarm(requireContext(), user_id!!, "18:00:00")
                timePicker?.hour = 18
                timePicker?.minute = 0
                sessionManager.updateSession(requireContext(), user_id, mEmail!!, "18:00:00")
            }else{
                timePicker?.visibility = View.GONE
                saveTime?.visibility = View.GONE
                userDbManager.turnOffAlarm(requireContext(), user_id!!)
                sessionManager.updateSession(requireContext(), user_id, mEmail!!, null)
            }
        }
        //Новое время
        saveTime?.setOnClickListener{
            var time_picked = "${timePicker!!.hour}:${timePicker!!.minute}:00"
            sessionManager.updateSession(requireContext(), user_id!!, mEmail!!, time_picked)
            userDbManager.turnOnAlarm(requireContext(), user_id, time_picked)
            var calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timePicker!!.getHour());
            calendar.set(Calendar.MINUTE, timePicker!!.getMinute());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return view
    }
}