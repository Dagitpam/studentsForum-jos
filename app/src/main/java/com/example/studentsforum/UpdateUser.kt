package com.example.studentsforum

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users

class UpdateUser : AppCompatActivity() {
    private lateinit var et_name: EditText;
    private lateinit var et_email:EditText
    private lateinit var et_password:EditText
    private lateinit var et_address:EditText
    //Declear global variables
    private lateinit var update_name:String
    private lateinit var update_email:String
    private lateinit var update_password:String
    private lateinit var update_address:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        val id = intent.getIntExtra("id",-1)
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val address =intent.getStringExtra("address")

        //getting the ids for the updates values
        et_name = findViewById(R.id.et_update_user_name)
        et_email = findViewById(R.id.et_update_user_email)
        et_password = findViewById(R.id.et_update_user_password)
        et_address = findViewById(R.id.et_update_user_address)

        et_name.setText(name)
        et_email.setText(email)
        et_password.setText(password)
        et_address.setText(address)

        val update_bt: Button = findViewById(R.id.update_user_button)

        update_bt.setOnClickListener(View.OnClickListener {
             //Get updated values from update user layout
            update_name = et_name.text.toString().trim()
            update_email = et_email.text.toString().trim()
            update_password = et_password.text.toString().trim()
            update_address = et_address.text.toString().trim()

            //create the database helper instance
            val db_helper = DatabaseHelper(this)

            //insert the updated values to the user class object

            val users = Users(id = id, name = update_name, email = update_email, password = update_password, address = update_address)

            //Call the update user function of the database helper to update the users record
            db_helper.updateUser(users)
            //redirect the user to main activity

            val toUpdate = Intent(this, MainActivity::class.java)
            startActivity(toUpdate)



        })
    }
}
