package com.example.admin_app.doctor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.R

class DoctorAdapter(private val doctorlists :ArrayList<Data>, private val edit: Click, private val delete: Click) : RecyclerView.Adapter<DoctorAdapter.myViewHolder>() {
    class myViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
         val name : TextView = itemView.findViewById(R.id.doctorname)
         val docid : TextView = itemView.findViewById(R.id.docid)
         val edit : Button = itemView.findViewById(R.id.edit)
         val delete : Button = itemView.findViewById(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.doctor_item,parent,false)
        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
       val currentItem = doctorlists[position]
        holder.name.text=currentItem.name.toString()
        holder.docid.text=currentItem.id.toString()
        holder.edit.setOnClickListener{
            edit.go_to_main_edit(position)
        }
        holder.delete.setOnClickListener {
            delete.go_to_main_delete(position)
        }


    }

    override fun getItemCount(): Int {
        return doctorlists.size
    }
    interface Click{
        fun go_to_main_delete(R_id : Int)
        fun go_to_main_edit(R_id : Int)
    }


}