package com.example.studentsforum

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsforum.model.Users

class UsersRecyclerAdapter(private val listUsers: List<Users>, internal var context: Context): RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        //Inflating recycler item view (linking the card view)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.users_list_card,parent,false)

        return UserViewHolder(itemView)



    }

    override fun getItemCount(): Int {
        //Size

        return listUsers.size
    }

     override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //The data collected from the list view will be bind it to card view

        holder.textName.text = listUsers[position].name
        holder.textEmail.text = listUsers[position].email

        //set onClick listener on user's data
        holder.itemView.setOnClickListener(View.OnClickListener {

            val i = Intent(context, ViewUser::class.java)

            //pass the details of the user to the next activity
            i.putExtra("id", listUsers[position].id)
            i.putExtra("name",listUsers[position].name)
            i.putExtra("email",listUsers[position].email)
            i.putExtra("address",listUsers[position].address)
            i.putExtra("password",listUsers[position].password)

            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        })



    }

    // we created an inner class
    inner class UserViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textName : TextView
        var textEmail : TextView

        init {
            textName = view.findViewById(R.id.user_name) as TextView
            textEmail = view.findViewById(R.id.User_email) as TextView


        }
    }


}