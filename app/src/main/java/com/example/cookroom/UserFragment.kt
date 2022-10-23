package com.example.cookroom

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text


class UserFragment : Fragment() {
    var email : TextView? = null
    var logout : Button? = null
    var sharedPreferences : SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_user, container, false)
        email = view?.findViewById(R.id.email)
        logout = view?.findViewById(R.id.logout)

        var extraEmail = requireActivity().intent.getStringExtra("email")
        //Toast.makeText(requireContext(), extraEmail, Toast.LENGTH_LONG).show()

        var sessionManager = SessionManager(requireContext())
        sessionManager.sessionManage(requireContext())
        sessionManager.checkLogin()
        //Toast.makeText(requireContext(), sessionManager.checkLogin().toString(), Toast.LENGTH_LONG).show()
        var user : HashMap<String, String> = sessionManager.getUserDetails()
        var mEmail = user.get(sessionManager.EMAIL)
        var user_id = user.get(sessionManager.ID)
        var sharedPreferences = requireContext().getSharedPreferences("User_Id", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("user_id", user_id).apply()
        email?.setText(mEmail)

        logout?.setOnClickListener {
            sessionManager.logout()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}