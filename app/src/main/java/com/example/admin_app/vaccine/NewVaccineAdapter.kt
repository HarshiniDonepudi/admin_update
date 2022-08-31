package com.example.admin_app.vaccine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.admin_app.ImageAdapter
import com.example.admin_app.R
import com.example.admin_app.image

class NewVaccineAdapter (var vaccineList: ArrayList<newvaccine>, private val view: Click) : RecyclerView.Adapter<NewVaccineAdapter.ViewHolder>() {





        // Provide a direct reference to each of the views with data items

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var image: ImageView =itemView.findViewById(R.id.vaccine_img)
            var text: TextView =itemView.findViewById(R.id.vaccine_name)
            val card : CardView = itemView.findViewById(R.id.card)


        }

        // Usually involves inflating a layout from XML and returning the holder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewVaccineAdapter.ViewHolder {

            // Inflate the custom layout
            var view = LayoutInflater.from(parent.context).inflate(R.layout.vaccine_row, parent, false)
            return ViewHolder(view)
        }

        // Involves populating data into the item through holder
        override fun onBindViewHolder(holder: NewVaccineAdapter.ViewHolder, position: Int) {

            // Get the data model based on position
            var data = vaccineList[position]

            // Set item views based on your views and data model

            holder.image.load(vaccineList[position].image?.toUri()){
                placeholder(R.drawable.loading_animation)
                error(R.drawable.ic_broken_image)
            }
            holder.card.setOnClickListener {
                view.go_to_main_view(vaccineList[position].text,vaccineList[position].image)
            }
            holder.text.text = data.text
        }

        //  total count of items in the list
        override fun getItemCount() = vaccineList.size

        interface Click{
            fun go_to_main_view(text : String,url:String)

        }


}
