package com.example.weatherforecast.favorite.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Favorite

class FavAdapter(private val context: Context, private val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<FavAdapter.ViewHolder>() {

    private var favList: List<Favorite> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_row, parent, false)

        return FavAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityTxt.text = favList[position].addressLine

       holder.menuIcon.setOnClickListener {
            val popUp: PopupMenu = PopupMenu(context, holder.menuIcon)
            popUp.inflate(R.menu.options_menu)
            popUp.setOnMenuItemClickListener {
                onItemClickListener.onDeleteClick(favList[position])
                return@setOnMenuItemClickListener false
            }

            popUp.show()

        }

        holder.itemView.setOnClickListener { onItemClickListener.onClick(favList[position]) }
    }

    override fun getItemCount(): Int {
        return favList.size
    }
    fun setData(favorites: List<Favorite>){
        favList = favorites
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cityTxt: TextView = itemView.findViewById(R.id.favCityName)
        val menuIcon: TextView = itemView.findViewById(R.id.menuIcon)
    }

}