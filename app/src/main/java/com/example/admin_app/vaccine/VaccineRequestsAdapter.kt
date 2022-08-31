package com.example.admin_app.vaccine

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.AppointmentAdapter
import com.example.admin_app.ImageAdapter
import com.example.admin_app.R


class VaccineRequestsAdapter(val context: VaccineRequestFragment, var app_list: ArrayList<bookvaccine>,private var view: VaccineRequestsAdapter.Click):
    RecyclerView.Adapter<VaccineRequestsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id = itemView.findViewById<TextView>(R.id.aid)
        val doc_name = itemView.findViewById<TextView>(R.id.dname)
        val date = itemView.findViewById<TextView>(R.id.date)
        val timeslot =  itemView.findViewById<TextView>(R.id.time)
        val status = itemView.findViewById<TextView>(R.id.status)
        val card = itemView.findViewById<CardView>(R.id.card)
    }



    override fun onBindViewHolder(p0: VaccineRequestsAdapter.ViewHolder, p1: Int) {
        p0?.id?.text = app_list[p1].vaccine_id
        p0?.doc_name?.text = app_list[p1].name
        p0?.date?.text = app_list[p1].distributor
        p0?.timeslot?.text = app_list[p1].dose
        p0?.status?.text = app_list[p1].status
        p0?.card?.setOnClickListener {
            app_list[p1].vaccine_id?.let { it1 -> app_list[p1].status?.let { it2 ->
                view.go_to_main_view(
                    it2, it1)
            } }
        }
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VaccineRequestsAdapter.ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.request, p0, false)
        return VaccineRequestsAdapter.ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return app_list.size
    }
    interface Click{
        fun go_to_main_view(text:String,vacid:String)
    }
}