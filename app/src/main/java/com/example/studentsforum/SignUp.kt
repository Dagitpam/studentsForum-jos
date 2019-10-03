package com.example.studentsforum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val btnSignUp = findViewById<Button>(R.id.bt_signup_submit)
        btnSignUp.setOnClickListener(View.OnClickListener {
            userInputs()
        })


    }
    fun userInputs(){
        val user_name = findViewById<EditText>(R.id.et_signup_name).text.toString()
        val user_email = findViewById<EditText>(R.id.et_signup_email).text.toString()
        val user_password = findViewById<EditText>(R.id.et_signup_password).text.toString()
        val user_Cpassword = findViewById<EditText>(R.id.et_signup_Cpassword).text.toString()
        val user_address = findViewById<EditText>(R.id.et_signup_address).text.toString()
        valdateInput(user_name, user_email,user_password,user_Cpassword,user_address)
    }

    fun valdateInput(user_name:String, user_email: String,user_password: String, user_Cpassword:String, user_address:String){
         var msg = ""
        if (user_name.trim() == "")
        {
            msg = "Name field empty"

        }
        else if (user_email.trim() == "")
        {
            msg = "Email field empty"
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches())
        {
            msg = "Invalid email"
        }
        else if(user_password.trim() == "")
        {
            msg = "Password field empty"
        }
        else if (user_Cpassword.trim() == "")
        {
            msg = "confirm password field empty"
        }
        else if (user_password != user_Cpassword)
        {
            msg = "password not match"
        }
        else if (user_address.trim() == "")
        {
            msg = "Address field empty"
        }
        else{
            //Create a database helper instance to push your form data to
            val databaseHelper = DatabaseHelper(this)
            //Check if email exist
            if (!databaseHelper.checkUser(user_email)){

                val user = Users(name = user_name, email =user_email,password = user_password,address =user_address)
                databaseHelper.addUser(user)
                msg = "Registration successful"

                //Direct the user to login
                val toLogin = Intent(this, LoginActivity::class.java)
                startActivity(toLogin)
                finish()

            }
            else{
                msg = "Email has been taking"
            }



           /*msg  =  submitRecords(user_name, user_email,user_password,user_Cpassword,user_address)
            displayToast(msg)*/
        }
        displayToast(msg)
    }
    /*fun submitRecords(name:String, email: String, password: String, cpassword:String, address:String):String{
        return "Success"
    }*/
    fun displayToast(msg:String)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
