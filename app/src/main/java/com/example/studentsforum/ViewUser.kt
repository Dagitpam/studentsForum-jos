package com.example.studentsforum

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users
import java.sql.Blob

class ViewUser : AppCompatActivity() {
    private lateinit var tv_name : TextView
    private lateinit var  tv_email: TextView
    private lateinit var tv_address: TextView
    private lateinit var tv_password: TextView
    private lateinit var tv_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user)
        //Collecting values from UserRecyclerAdapter class
        val id = intent.getIntExtra("id",-1)
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val password = intent.getStringExtra("password")
        val image = intent.getByteArrayExtra("image")

        tv_name = findViewById(R.id.tv_view_user_name)
        tv_email = findViewById(R.id.tv_view_user_email)
        tv_address = findViewById(R.id.tv_view_user_address)
        tv_password = findViewById(R.id.tv_view_user_password)
        tv_image = findViewById(R.id.iv_view_user_img)

        //converting bytes array to bitmap
        val bmp = BitmapFactory.decodeByteArray(image,0,image.size)
        tv_image.setImageBitmap(bmp)

        tv_name.setText(name)
        tv_email.setText(email)
        tv_address.setText(address)
        tv_password.setText(password)



        val update_bt:Button = findViewById(R.id.bt_update)
        val delete_bt:Button =findViewById(R.id.bt_delete)

        update_bt.setOnClickListener(View.OnClickListener {
            //redirecting to updateUser
            val toUpdate = Intent(this, UpdateUser::class.java)

            toUpdate.putExtra("id", id)
            toUpdate.putExtra("name",name)
            toUpdate.putExtra("email",email)
            toUpdate.putExtra("password",password)
            toUpdate.putExtra("address",address)
            toUpdate.putExtra("image",image)
            toUpdate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            toUpdate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            toUpdate.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(toUpdate)
            finish()



        })

        delete_bt.setOnClickListener(View.OnClickListener {

            val db_helper = DatabaseHelper(this)
            val users = Users(id = id, name = "", email = "", password = "", address = "", image = ByteArray(image.size))
            db_helper.deleteUser(users)

            val toMain = Intent(this, MainActivity::class.java)
            //We use add_flags to clear the stack
            toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            toMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(toMain)
            finish()

            Toast.makeText(this,"User deleted successfully",Toast.LENGTH_LONG).show()
        })


    }
}
