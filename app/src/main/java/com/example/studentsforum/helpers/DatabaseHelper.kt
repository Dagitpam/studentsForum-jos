package com.example.studentsforum.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.studentsforum.model.Users

//factory return a cursor object. so in this case we are not returning anything that is why we made it null

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,
    DATABASE_VERSION) {

    //Create a table that will perform our SQL query
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+ COLUMN_USER_NAME+ " TEXT,"+
            COLUMN_USER_EMAIL+ " TEXT,"+ COLUMN_USER_PASSWORD+ " TEXT,"+ COLUMN_USER_ADDRESS+ " TEXT,"+ COLUMN_USER_IMAGE+ " BLOB"+ ")"
            )

    //Create query to drop our table. Note the dollar sign references the  or point to table name
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

    override fun onCreate(db: SQLiteDatabase?) {

        //we  execute the command to  create our table

        if (db != null) {
            db.execSQL(CREATE_USER_TABLE)
        }

           }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //we update our table

        if (p0 != null) {
            p0.execSQL(DROP_USER_TABLE)

        }
        //it drops the previous table and create the new  one
        onCreate(p0)
           }
    //A function that add users records

    fun addUser(users:Users){
        //convert bitmap to byte
        
        //db is an instance of the writable database that aid us to write to or update our db
        val db = this.writableDatabase
        //create an instance of the contents of the value. content value is a pair to pair key and value.
        val values = ContentValues()
        values.put(COLUMN_USER_NAME,users.name)
        values.put(COLUMN_USER_EMAIL,users.email)
        values.put(COLUMN_USER_PASSWORD,users.password)
        values.put(COLUMN_USER_ADDRESS,users.address)
        values.put(COLUMN_USER_IMAGE,users.image)


    db.insert(TABLE_NAME,null, values)
        db.close()

    }

    //function to check if an email exist (Function over loading)
    fun checkUser(email: String):Boolean{
        //it specifies the array of column to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db =this.readableDatabase

        //Write the selection criteria
        val selection = "$COLUMN_USER_EMAIL = ?"

        //selection arguement. execute the query
        val selectionArgs = arrayOf(email)

        //We are traversing through the emails column

        val cursor = db.query(TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
            )
        //how many rows does it fetch
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0){
            return true
        }
            return false

    }
    //To check the if the user records is in the db(Function over loading)
    fun checkUser(email:String, password:String): Boolean
    {
        //create array of columns to fetch from
        val columns = arrayOf(COLUMN_USER_ID)
        //create an instance of our db
        val db = this.readableDatabase

        //selection criteria
        val selection = "$COLUMN_USER_EMAIL= ? AND $COLUMN_USER_PASSWORD= ?"

        //selection arguments
        val selectionArgs = arrayOf(email,password)

        //query user table with conditions
        //Here, the query function is use to fetch records from the user table
        val cursor = db.query(TABLE_NAME,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
            )
        val cursorCount = cursor.count
        cursor.close()
        db.close()
        if (cursorCount > 0)
        {
            return true
        }

            return  false


    }
    //function that delete user
    fun deleteUser(user:Users)
    {
        //We are using the db instance
        val db =this.writableDatabase

        //delete user record by id

        db.delete(TABLE_NAME, "$COLUMN_USER_ID = ?", arrayOf(user.id.toString()))

    }
    //Update user table
    fun updateUser(user:Users)
    {
        val db = this.writableDatabase

        //we call the content value. key value pair

        val values = ContentValues()
        values.put(COLUMN_USER_NAME,user.name)
        values.put(COLUMN_USER_EMAIL,user.email)
        values.put(COLUMN_USER_PASSWORD,user.password)
        values.put(COLUMN_USER_ADDRESS,user.address)
        values.put(COLUMN_USER_IMAGE,user.image)

        db.update(TABLE_NAME,values,"$COLUMN_USER_ID=?",
            arrayOf(user.id.toString()))
        db.close()
    }
    //Fetch all users
    fun fetchUsers():List<Users>
    {
        //array of columns to fetch from
        val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL,
            COLUMN_USER_PASSWORD, COLUMN_USER_ADDRESS, COLUMN_USER_IMAGE)
        //Sorting order
        val sortOrder = "$COLUMN_USER_NAME ASC"
        val userList = arrayListOf<Users>()
        val db = this.readableDatabase

        //query the user table

        val cursor =  db.query(TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            sortOrder)
        if (cursor.moveToFirst())
        {
            do {
                val user = Users(id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)).toInt(),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                    email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)),
                    address = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ADDRESS)),
                    image =cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_IMAGE)) )

                userList.add(user)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return userList

    }
    //WE are having a companion object
    companion object{
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "UsersDB.db"
        private val TABLE_NAME = "users"

        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"
        private val COLUMN_USER_ADDRESS = "user_address"
        private val COLUMN_USER_IMAGE= "user_image"
    }

}

