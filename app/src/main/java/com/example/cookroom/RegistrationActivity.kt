package com.example.cookroom

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

//Активность регистрации
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
            if (isEmailValid(email!!.text) and (password!!.text.toString() == passwordConfirm!!.text.toString())) {
                val userEmail = this.email?.text.toString().trim()
                val userPassword = this.password?.text.toString().trim()
                register(userEmail, userPassword)
            } else if (!isEmailValid(email!!.text)){
                Toast.makeText(this, "Неверный email-адрес", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_LONG).show()
            }
        }
    }
    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email!!)
            .matches()
    }
    //регистрация
    fun register(userEmail: String, userPassword:String) :Boolean {
        val stringRequest = object : StringRequest(
            Method.POST, URL_REGIST,
            Response.Listener { response ->
                try {
                    val obj = JSONObject(response.toString())
                    Toast.makeText(applicationContext, obj.getString("message"),
                        Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(applicationContext, error?.message,
                Toast.LENGTH_LONG).show() }) {
            @Throws (AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params : HashMap<String, String> = HashMap()
                    params["email"] = userEmail
                    params["password"] = userPassword
                    return params
                }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
        return true
    }
}