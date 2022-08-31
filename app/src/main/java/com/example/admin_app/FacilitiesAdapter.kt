package com.example.admin_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FacilitiesAdapter(var facilitiesList: ArrayList<facilities>, private val view: FacilitiesAdapter.Click) : RecyclerView.Adapter<FacilitiesAdapter.ViewHolder>() {








    // Provide a direct reference to each of the views with data items

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var text: TextView =itemView.findViewById(R.id.ftitle)


    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacilitiesAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.facilities, parent, false)
        return ViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: FacilitiesAdapter.ViewHolder, position: Int) {

        // Get the data model based on position
        var data = facilitiesList[position]

        // Set item views based on your views and data model
        holder.text.setOnClickListener {
            data.fac_id?.let { it1 -> view.go_to_main_view(it1) }
        }

        holder.text.text = data.text
    }

    //  total count of items in the list
    override fun getItemCount() = facilitiesList.size

    interface Click{
        fun go_to_main_view(text : String)

    }



}