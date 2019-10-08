package com.example.studentsforum.model

 data class Users (val id:Int=-1, val name:String, val email:String, val password:String, val address:String,   val image:ByteArray)
/*
val bitmap = (image_profile.getDrawable() as BitmapDrawable).getBitmap()
val stream = ByteArrayOutputStream()
bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
val image = stream.toByteArray()*/
