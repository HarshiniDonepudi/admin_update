package com.example.admin_app

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.admin_app.doctor.DoctorAdapter


class DiagnosisAdapter (var diagnosis_list: ArrayList<diagnosis>,  private val view: Click):
    RecyclerView.Adapter<DiagnosisAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        val date : TextView = itemView.findViewById(R.id.ddate)
        val rid : TextView = itemView.findViewById(R.id.rid)
        val did : TextView = itemView.findViewById(R.id.did)
        val patient_name: TextView =  itemView.findViewById(R.id.patientname)
        val prescription : ImageView =  itemView.findViewById(R.id.prescription)
        val card : CardView = itemView.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("adapter","oncreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.diagnosis_list, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("adapter","onbind")
        holder.rid.text =  diagnosis_list[position].r_id
        holder.did.text = diagnosis_list[position].did
        holder.date.text = diagnosis_list[position].date
        holder.patient_name.text = diagnosis_list[position].pname
        holder.prescription.load(diagnosis_list[position].img?.toUri()){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
        holder.card.setOnClickListener {
            view.go_to_main_view(diagnosis_list[position].img!!.toString())
        }
        Log.e("on","array:${diagnosis_list}")

    }

    override fun getItemCount(): Int {
        return diagnosis_list.size
    }
    interface Click{
        fun go_to_main_view(url : String)

    }

}
