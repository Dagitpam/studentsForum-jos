package com.example.studentsforum

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.TextureView
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsforum.helpers.DatabaseHelper
import com.example.studentsforum.model.Users

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //Declaration
    private lateinit var allUsersRecycler: RecyclerView
    private lateinit var listView: MutableList<Users>
    private lateinit var  recyclerAdapter: UsersRecyclerAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        //Shared preference
        /*val sharedPref = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        val saveEmail =  sharedPref.getString("email","")*/

        // The recycler view from content_main xml
        allUsersRecycler = findViewById<View>(R.id.all_users_recycler) as RecyclerView
        listView = ArrayList()
        recyclerAdapter = UsersRecyclerAdapter(listView, this)

        //Linear manager and grid manager

        val mLayoutManager = LinearLayoutManager(this)

        allUsersRecycler.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
        //given the card view a fix size
        allUsersRecycler.setHasFixedSize(true)
        //Links your recycler adapter class to the recycler view on your xml file

        allUsersRecycler.adapter = recyclerAdapter

        databaseHelper = DatabaseHelper(this)

        //calling our function
        GetDataFromSQLite().execute()



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_logout)
        {
            logout()
        }
        return super.onOptionsItemSelected(item)

    }
    //Clear shared preference
    fun logout()
    {

        val logoutPref = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE)
        //call the edit file and clear the content and apply
        logoutPref.edit().clear().apply()

        //Redirect to login

        val toLogin = Intent(this, LoginActivity::class.java)
        startActivity(toLogin)
        finish()
    }
    //this class will help your get data from SQLite db without laggin

    // We use AyncTask whenever we want to fetch bulky work and make our app to work faster

    inner class GetDataFromSQLite: AsyncTask<Void, Void, List<Users>>()
    {
        override fun doInBackground(vararg p0: Void?): List<Users> {
            return databaseHelper.fetchUsers()
        }

        override fun onPostExecute(result: List<Users>?) {
            super.onPostExecute(result)
            //It clears the list view
            listView.clear()

            listView.addAll(result!!)
        }

    }
}
