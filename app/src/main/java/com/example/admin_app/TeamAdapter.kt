package com.example.admin_app

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ramotion.foldingcell.FoldingCell

class TeamAdapter(var team_list: ArrayList<team>,  private val edit: Click,private val delete:Click):
    RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.teamname)
        val designation : TextView = itemView.findViewById(R.id.teamdesignation)
        val edit : Button = itemView.findViewById(R.id.edit)
        val number : TextView = itemView.findViewById(R.id.teamnumber)
        val about : TextView = itemView.findViewById(R.id.teamabout)
        val img_view :ImageView=itemView.findViewById(R.id.teamimg)
        val delete : Button = itemView.findViewById(R.id.delete)
        val relativeLayout:RelativeLayout = itemView.findViewById(R.id.relative)
        val card:CardView = itemView.findViewById(R.id.card)




    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("adapter", "onbind")
        holder.name.text = team_list[position].name
        holder.designation.text = team_list[position].designation
        holder.number.text = "Phone number :  " + team_list[position].number
        holder.about.text = team_list[position].about

        holder.img_view.load(team_list[position].image?.toUri()) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }


        holder.edit.setOnClickListener {
            edit.go_to_main_edit(position)
        }
        holder.delete.setOnClickListener {
            delete.go_to_main_delete(position)
        }
        val isVisible: Boolean = team_list[position].visibility


        holder.relativeLayout.visibility = if(isVisible) View.VISIBLE else View.GONE

        holder.card.setOnClickListener{
            team_list[position].visibility = !team_list[position].visibility
            notifyItemChanged(position)
        }
    }



    override fun getItemCount(): Int {
        return team_list.size
    }
    interface Click{
        fun go_to_main_delete(R_id : Int)
        fun go_to_main_edit(R_id : Int)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.team_item, parent, false)
        return ViewHolder(v);
    }

}
