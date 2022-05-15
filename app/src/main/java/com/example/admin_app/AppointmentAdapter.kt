package com.example.admin_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.doctor.DoctorAdapter


class AppointmentAdapter (var app_list: ArrayList<appointment>, private val approved: AppointmentAdapter.Click, private val disapprove: AppointmentAdapter.Click):
        RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val id : TextView = itemView.findViewById(R.id.aid)
            val doc_name :TextView= itemView.findViewById(R.id.dname)
            val date :TextView= itemView.findViewById(R.id.date)
            val timeslot:TextView =  itemView.findViewById(R.id.atime)
            val status : TextView=  itemView.findViewById(R.id.status)
            val approved : Button = itemView.findViewById(R.id.approved)
            val disapproved : Button = itemView.findViewById(R.id.cancel)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            Log.e("adapter","oncreate")
            val v = LayoutInflater.from(parent.context).inflate(R.layout.appointment_listcard, parent, false)
            return ViewHolder(v);
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Log.e("adapter","onbind")
            holder.id.text =  app_list[position].appointment_id
            holder.doc_name.text = app_list[position].name
            holder.date.text = app_list[position].date
            holder.timeslot.text = app_list[position].timeslot
            holder.status.text = app_list[position].status
            if(app_list[position].status=="approved" ||app_list[position].status=="disapproved" ){
                holder.approved.isVisible=false
                holder.disapproved.isVisible=false
            }
            else{
                holder.approved.isVisible=true
                holder.disapproved.isVisible=true
            }
            Log.e("on","array:${app_list}")
            holder.approved.setOnClickListener{
                approved.go_to_main_approved(position)
            }
            holder.disapproved.setOnClickListener {
                disapprove.go_to_main_disapproved(position)
            }
        }

        override fun getItemCount(): Int {
            return app_list.size
        }
    interface Click{
        fun go_to_main_approved(R_id : Int)
        fun go_to_main_disapproved(R_id : Int)
    }
    }

