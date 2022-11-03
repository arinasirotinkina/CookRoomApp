package com.example.cookroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import kotlin.jvm.Throws

class RegistrationActivity : AppCompatActivity() {
    var email : EditText? = null
    var password : EditText? = null
    var passwordConfirm : EditText? = null
    var registerButton : Button? = null
    var alreadyHaveAcc : TextView? = null
    var URL_REGIST = "https://cookroom.site/register.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        passwordConfirm = findViewById(R.id.passwordConfirm)
        registerButton = findViewById(R.id.registerButton)
        alreadyHaveAcc = findViewById(R.id.alreadyHaveAcc)
        alreadyHaveAcc?.setOnClickListener{
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
        registerButton?.setOnClickListener{
            Regist()
        }
    }
    fun Regist() {
        var userEmail = this.email?.text.toString().trim()
        var userPassword = this.password?.text.toString().trim()

        var stringRequest = object : StringRequest(
            Method.POST, URL_REGIST,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Toast.makeText(applicationContext, error?.message, Toast.LENGTH_LONG).show()
                }
            }) {

            @Throws (AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    var params : HashMap<String, String> = HashMap<String, String>()
                    params.put("email", userEmail)
                    params.put("password", userPassword)
                    return params

                }
        }
        var requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}