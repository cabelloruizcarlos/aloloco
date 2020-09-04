package es.ccrr.aloloco.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.ccrr.aloloco.R

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListAdapter.MainListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_main, null)
        return MainListHolder(view)
    }

    override fun onBindViewHolder(holder: MainListAdapter.MainListHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class MainListHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}
