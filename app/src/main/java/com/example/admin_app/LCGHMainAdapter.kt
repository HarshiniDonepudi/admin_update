package com.example.admin_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load

class LCGHMainAdapter(var dataList: ArrayList<lcghnews>, private val view: LCGHMainActivity) : RecyclerView.Adapter<LCGHMainAdapter.ViewHolder>() {





    // Provide a direct reference to each of the views with data items

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView =itemView.findViewById(R.id.image)
        var text: TextView =itemView.findViewById(R.id.title)
        val card : CardView = itemView.findViewById(R.id.card)


    }

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LCGHMainAdapter.ViewHolder {

        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: LCGHMainAdapter.ViewHolder, position: Int) {

        // Get the data model based on position
        var data = dataList[position]

        // Set item views based on your views and data model

        holder.image.load(dataList[position].image?.toUri()){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        holder.card.setOnClickListener {
            dataList[position].image?.let { it1 -> view.go_to_main_view(it1) }
        }
        holder.text.text = data.title
    }

    //  total count of items in the list
    override fun getItemCount() = dataList.size

    interface Click{
        fun go_to_main_view(url : String)

    }
}