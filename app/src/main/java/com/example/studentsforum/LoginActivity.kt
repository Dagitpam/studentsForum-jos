package com.example.studentsforum

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.studentsforum.helpers.DatabaseHelper
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var tv_new_user = findViewById<View>(R.id.tv_not_register) as TextView

        var tv_forgot_password: TextView = findViewById(R.id.tv_forgot_password)
        val et_login_email: EditText = findViewById(R.id.et_login_email)
        val et_login_password: EditText = findViewById(R.id.et_login_password)
        val bt_submit_login: Button = findViewById(R.id.bt_login_submit)
        //the shared preference is call here to check if is empty or not
        val sharedPref = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
         val saveEmail =  sharedPref.getString("email","")
        val savePassword = sharedPref.getString("password","")

        if (saveEmail !="" && savePassword != ""){
            val dashboard = Intent(this, MainActivity::class.java)
            startActivity(dashboard)
            finish()
        }

        bt_submit_login.setOnClickListener(View.OnClickListener {
            //conversion to string
            val email:String = et_login_email.text.toString().trim()
            val password: String = et_login_password.text.toString().trim()
            //check if is empty
            if (email.isEmpty()){
                et_login_email.setError("Email is empty")
                Toast.makeText(applicationContext, "Email is empty", Toast.LENGTH_LONG).show()
            }
            //contains is a funtion over loading. Funtion over loading is define as a function where a function with the same name has different parameter list
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                et_login_email.setError("Invalid email")
                Toast.makeText(applicationContext, "Invalid email", Toast.LENGTH_LONG).show()
            }
            else if (password.isEmpty()){
                et_login_password.setError("Password empty")

            }
            else if (password.length < 6){
                et_login_password.setError("Password too short")

            }
            else {

                //Create an object of the db helper class
                val databaseHelper = DatabaseHelper(this)
                //Check if email and password is inside the db
                if (databaseHelper.checkUser(email, password))
                {
                    //creating shared preference (cookies)
                    val sharedPreference = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("email",email)
                    editor.putString("password",password)
                    editor.apply()

                    Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
                    val dashboard = Intent(this, MainActivity::class.java)
                    //Passing data from one activity to another activity. U can either pass it to shared preference and get it from any activity or pass it using putExtra and get it on the specify dashboard
                    dashboard.putExtra("email",email)
                    startActivity(dashboard)

                }
                else
                {
                    Toast.makeText(this, "Email/Password invalid", Toast.LENGTH_LONG).show()
                }


            }
        })


        tv_new_user.setOnClickListener(View.OnClickListener {
            //move from login activity to signUp activity

            var signup = Intent(this, SignUp::class.java)

            startActivity(signup)
            //Finish() function is use to terminate the intent from going back  to other activity
            //finish()
        })
        tv_forgot_password.setOnClickListener(View.OnClickListener {
            var forgot = Intent(this, Forgotpassword::class.java)
            startActivity(forgot)

        })
    }
}