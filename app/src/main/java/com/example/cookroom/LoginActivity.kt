package com.example.cookroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

//Активность входа
class LoginActivity : AppCompatActivity() {
    var email : EditText? = null
    var password : EditText? = null
    var loginButton : Button? = null
    var notHaveAcc : TextView? = null
    var sessionManager = SessionManager(this)
    var URL_LOGIN = "https://cookroom.site/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        notHaveAcc = findViewById(R.id.notHaveAcc)
        sessionManager.sessionManage(this)
        loginButton?.setOnClickListener() {
            val mEmail : String = email?.text.toString().trim()
            val mPassword : String = password?.text.toString().trim()
            if (mEmail.isNotEmpty() || mPassword.isNotEmpty()) {
                login(mEmail, mPassword)
            }
        }
        notHaveAcc?.setOnClickListener() {
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
            finish()
        }
    }
    //вход
    private fun login(email: String, password: String){
        var stringRequest = object : StringRequest(
            Method.POST, URL_LOGIN,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val message = jsonObject.getString("message")
                    val jsonArray = jsonObject.getJSONArray("login")
                    Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show()
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)
                            val email = obj.getString("email").trim()
                            val id = obj.getString("id").trim()
                            val time = obj.getString("time").trim()
                            sessionManager.createSession(this, email, id, time)
                            val i = Intent(this, MainActivity::class.java)
                            i.putExtra("email", email)
                            startActivity(i)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "failed ${e.toString()}", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(applicationContext, error?.toString(),
                Toast.LENGTH_LONG).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params : HashMap<String, String> = HashMap()
                params["email"] = email
                params["password"] = password
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}