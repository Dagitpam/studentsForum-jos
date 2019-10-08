package com.example.studentsforum

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class SignUp : AppCompatActivity() {
    private lateinit var take_picture: Button
    private lateinit var picture_display: ImageView

    //two constants to specify our actions, either we are picking images from gallery or camera
    private val GALLERY = 10
    private val CAMERA = 21


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        /*val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val date = Date()
        output = File(dir, "CheckImage " + date.toString()+".jpeg")*/
        take_picture = findViewById(R.id.bt_signup_upload)
        take_picture.setOnClickListener(View.OnClickListener {

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


        val btnSignUp = findViewById<Button>(R.id.bt_signup_submit)
        btnSignUp.setOnClickListener(View.OnClickListener {
            userInputs()


        })


    }


    fun userInputs() {
        val user_name = findViewById<EditText>(R.id.et_signup_name).text.toString()
        val user_email = findViewById<EditText>(R.id.et_signup_email).text.toString()
        val user_password = findViewById<EditText>(R.id.et_signup_password).text.toString()
        val user_Cpassword = findViewById<EditText>(R.id.et_signup_Cpassword).text.toString()
        val user_address = findViewById<EditText>(R.id.et_signup_address).text.toString()
        val mobile = findViewById<CheckBox>(R.id.cb_mobile)
        val webfront = findViewById<CheckBox>(R.id.cb_webfront)
        val webback = findViewById<CheckBox>(R.id.cb_webback)
        val arvr = findViewById<CheckBox>(R.id.cb_arvr)
        val digital = findViewById<CheckBox>(R.id.cb_digital)
        val uiux = findViewById<CheckBox>(R.id.cb_uiux)
        val state = findViewById<Spinner>(R.id.sp_signup_soo).toString()
        val male = findViewById<RadioButton>(R.id.rb_male)
        val female = findViewById<RadioButton>(R.id.rb_female)

        picture_display = findViewById(R.id.im_signup_image)

        val bitmap = (picture_display.getDrawable() as BitmapDrawable).getBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = stream.toByteArray()

        valdateInput(user_name, user_email, user_password, user_Cpassword, user_address,image, mobile,webfront,webback,arvr,digital,uiux,state,male,female)
    }

    fun valdateInput(
        user_name: String,
        user_email: String,
        user_password: String,
        user_Cpassword: String,
        user_address: String,
        image:ByteArray,
        mobile : CheckBox,
        webfront:CheckBox,
        webback : CheckBox,
        arvr : CheckBox,
        digital : CheckBox,
        uiux : CheckBox,
        state : String,
        male : RadioButton,
        female : RadioButton

    ) {
        var msg = ""
        if (user_name.trim() == "") {
            msg = "Name field empty"

        } else if (user_email.trim() == "") {
            msg = "Email field empty"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            msg = "Invalid email"
        } else if (user_password.trim() == "") {
            msg = "Password field empty"
        } else if (user_Cpassword.trim() == "") {
            msg = "confirm password field empty"
        } else if (user_password != user_Cpassword) {
            msg = "password not match"
        } else if (user_address.trim() == "") {
            msg = "Address field empty"
        }
        else if (!male.isChecked && !female.isChecked){
            msg = "One or more field(s) for Gender most be check"

        }
        else if (state.equals("Select state of origin")){
            msg = "State not selected"
        }
        else if (!mobile.isChecked && !webfront.isChecked && !webback.isChecked && !arvr.isChecked && !digital.isChecked && !uiux.isChecked){
            msg = "One or more field(s) for Track most be check"
        }



        else {
            //Create a database helper instance to push your form data to
            val databaseHelper = DatabaseHelper(this)
            //Check if email exist
            if (!databaseHelper.checkUser(user_email)) {

                val user = Users(
                    name = user_name,
                    email = user_email,
                    password = user_password,
                    address = user_address,
                    image = image
                )

                val result = databaseHelper.addUser(user)
                Log.i("Values","$result")


                msg = "Registration successful"

                //Direct the user to login
                val toLogin = Intent(this, LoginActivity::class.java)
                startActivity(toLogin)
                finish()

            } else {
                msg = "Email has been taking"
            }



            /*msg = submitRecords(user_name, user_email, user_password, user_Cpassword, user_address)
            displayToast(msg)*/
        }
        displayToast(msg)
    }

    fun submitRecords(
        name: String,
        email: String,
        password: String,
        cpassword: String,
        address: String
    ): String {
        return "Success"
    }

    fun displayToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
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
                    picture_display = findViewById(R.id.im_signup_image)
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    //displays the image for us on our image view
                    picture_display!!.setImageBitmap(bitmap)

                }
                //catches erros if there is any
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@SignUp, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        //checks if we snapped picture with camera
        else if (requestCode == CAMERA) {
            //gets the image we snapped with the camera
            picture_display = findViewById(R.id.im_signup_image)
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            //displays the image on our image view
            picture_display!!.setImageBitmap(thumbnail)
            Toast.makeText(this@SignUp, "Image Saved!", Toast.LENGTH_SHORT).show()
        }


    }




}
