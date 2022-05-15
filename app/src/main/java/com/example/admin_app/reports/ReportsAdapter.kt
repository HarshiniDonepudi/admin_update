package com.example.admin_app.reports

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.admin_app.R


class ReportsAdapter ( var reports_list: ArrayList<reports>,private val delete: ReportsAdapter.Click,
                      private val view: ReportsAdapter.Click):
    RecyclerView.Adapter<ReportsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val date : TextView = itemView.findViewById(R.id.ddate)
        val rid : TextView = itemView.findViewById(R.id.rid)
        val did : TextView = itemView.findViewById(R.id.did)
        val delete : ImageView = itemView.findViewById(R.id.delete)
        val patient_name: TextView =  itemView.findViewById(R.id.patientname)
        val pdf : ImageView = itemView.findViewById(R.id.pdfimg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("adapter","oncreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.reports_list, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("adapter","onbind")
        holder.rid.text =  reports_list[position].report_id
        holder.did.text = reports_list[position].did
        holder.date.text = reports_list[position].date
        holder.patient_name.text = reports_list[position].pname
        holder.delete.setOnClickListener {
            view.go_to_main_delete(reports_list[position].report_id)
        }
        holder.pdf.setOnClickListener {
            view.go_to_main_view(reports_list[position].pdf!!.toString(),reports_list[position].report_id.toString())
//            rep.replaceFragment(PresFragment())
        }
        Log.e("on","array:${reports_list}")

    }

    override fun getItemCount(): Int {
        return reports_list.size
    }
    interface Click{
        fun go_to_main_view(url : String,r_id : String)
        fun go_to_main_delete( rid: String)

    }
//    interface replace{
//        fun replaceFragment(fragment: Fragment)
//
//
//    }

}
