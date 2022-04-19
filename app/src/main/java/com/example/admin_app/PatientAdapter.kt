package com.example.admin_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class PatientAdapter(
    private val patientList: PatientActivity
) : RecyclerView.Adapter<PatientAdapter.ViewHolder>() {

    //var doc_list:List<data> = doctors().doc_list()

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//        val current = patientList[p1]
//        p0.patient_img.setImageResource(current.patientImg)
//        p0.patient_name.text=current.patientname
//        p0.patient_id.text=current.patientId

        p0.card.setOnClickListener{
            p0.card.getContext().startActivity(Intent(p0.card.getContext(), PatientProfileActivity::class.java))


        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.patient_card, p0, false)
        return ViewHolder(v);
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patient_name = itemView.findViewById<TextView>(R.id.patientname)
        val patient_id = itemView.findViewById<TextView>(R.id.patientid)
        val patient_img = itemView.findViewById<ImageView>(R.id.patientimg)
        val card =  itemView.findViewById<CardView>(R.id.card)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}