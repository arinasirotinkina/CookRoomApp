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

class LoginActivity : AppCompatActivity() {
    var email : EditText? = null
    var password : EditText? = null
    var loginButton : Button? = null
    var reglink : TextView? = null
    var sessionManager = SessionManager(this)
    companion object  {
        var URL_LOGIN = "https://cookroom.site/login.php"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        reglink = findViewById(R.id.reglink)
        sessionManager.sessionManage(this)
        loginButton?.setOnClickListener() {
            var mEmail : String = email?.text.toString().trim()
            var mPassword : String = password?.text.toString().trim()

            if (mEmail.isNotEmpty() || mPassword.isNotEmpty()) {
                Login(mEmail, mPassword)
            }
        }
        reglink?.setOnClickListener() {
            var i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }

    }
    private fun Login(email: String, password: String){
        var stringRequest = object : StringRequest(
            Method.POST, LoginActivity.URL_LOGIN,
            Response.Listener<String> { response ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    val success = jsonObject.getString("success")
                    val jsonArray = jsonObject.getJSONArray("login")
                    if (success.equals("1")) {
                        for (i in 0 until jsonArray.length()) {
                            var obj = jsonArray.getJSONObject(i)
                            var email = obj.getString("email").trim()
                            var id = obj.getString("id").trim()
                            sessionManager.createSession(email, id)

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
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(applicationContext, error?.toString(), Toast.LENGTH_LONG).show()
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String>? {
                var params : HashMap<String, String> = HashMap<String, String>()
                params.put("email", email)
                params.put("password", password)
                return params

            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)

    }
}