package com.example.studentsforum

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users
import java.io.ByteArrayOutputStream
import java.io.IOException

class UpdateUser : AppCompatActivity() {
    private lateinit var et_name: EditText;
    private lateinit var et_email:EditText
    private lateinit var et_password:EditText
    private lateinit var et_address:EditText
    private lateinit var im_image:ImageView
    private lateinit var update_img_btn : Button
    //Declear global variables
    private lateinit var update_name:String
    private lateinit var update_email:String
    private lateinit var update_password:String
    private lateinit var update_address:String
    private lateinit var update_image:ByteArray
    //two constants to specify our actions, either we are picking images from gallery or camera
    private val GALLERY = 10
    private val CAMERA = 21



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        val id = intent.getIntExtra("id",-1)
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        val address =intent.getStringExtra("address")
        val image = intent.getByteArrayExtra("image")

        //getting the ids for the updates values
        et_name = findViewById(R.id.et_update_user_name)
        et_email = findViewById(R.id.et_update_user_email)
        et_password = findViewById(R.id.et_update_user_password)
        et_address = findViewById(R.id.et_update_user_address)
        im_image = findViewById(R.id.im_update_upload_image)
        update_img_btn = findViewById(R.id.bt_update_upload)

        val bmp = BitmapFactory.decodeByteArray(image,0,image.size)

        im_image.setImageBitmap(bmp)



        et_name.setText(name)
        et_email.setText(email)
        et_password.setText(password)
        et_address.setText(address)


        //
        update_img_btn.setOnClickListener(View.OnClickListener {

            //create an object of the alert dialog
            val pictureDialog = AlertDialog.Builder(this)

            // we set out title
            pictureDialog.setTitle("Select Action")

            //we specify the options on this line
            val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
            //we set our actions here. if user select any option what should it do
            pictureDialog.setItems(pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    //action 1 chooses image from the gallery
                    0 -> choosePhotoFromGallary()//this function that performs the action is below
                    //action 2 takes a photo from the camera
                    1 -> takePhotoFromCamera()//this function that perform this action is below
                }
            }
            //always put this line for the dialog to show
            pictureDialog.show()


        })



        val update_bt: Button = findViewById(R.id.update_user_button)

        update_bt.setOnClickListener(View.OnClickListener {
             //Get updated values from update user layout
            update_name = et_name.text.toString().trim()
            update_email = et_email.text.toString().trim()
            update_password = et_password.text.toString().trim()
            update_address = et_address.text.toString().trim()



            val bitmap = (im_image.getDrawable() as BitmapDrawable).getBitmap()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val image_view = stream.toByteArray()


            //create the database helper instance
            val db_helper = DatabaseHelper(this)

            //insert the updated values to the user class object

            val users = Users(id = id, name = update_name, email = update_email, password = update_password, address = update_address, image = image_view)

            //Call the update user function of the database helper to update the users record
            db_helper.updateUser(users)
            //redirect the user to main activity

            val toUpdate = Intent(this, MainActivity::class.java)
            startActivity(toUpdate)



        })
    }
    fun choosePhotoFromGallary() {

        //create an object of an Intent that picks files for you and spcify that it should pick images
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)

    }
    private fun takePhotoFromCamera() {
        //allows you to use your phone's camera to snap pitures
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        startActivityForResult(intent, CAMERA)
    }
    //After selecting an image from gallery or capturing photo from camera, an onActivityResult() method is executed.
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        // checks if we picked image from Gallery
        if (requestCode == GALLERY) {
            if (data != null) {
                //gets the image we picked
                val contentURI = data!!.data
                try {
                    im_image = findViewById(R.id.im_update_upload_image)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //displays the image for us on our image view
                    im_image!!.setImageBitmap(bitmap)

                }
                //catches erros if there is any
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@UpdateUser, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        //checks if we snapped picture with camera
        else if (requestCode == CAMERA) {
            //gets the image we snapped with the camera
            im_image = findViewById(R.id.im_update_upload_image)
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            //displays the image on our image view
            im_image!!.setImageBitmap(thumbnail)
            Toast.makeText(this@UpdateUser, "Image Saved!", Toast.LENGTH_SHORT).show()
        }


    }
}
