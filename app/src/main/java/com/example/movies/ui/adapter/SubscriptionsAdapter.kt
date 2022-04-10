package com.example.movies.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.models.database.Shows

class SubscriptionsAdapter(
    private val listener: AdapterActions
) :
    ListAdapter<Shows, SubscriptionsAdapter.SubscriptionsViewHolder>(DiffUtilCallbackSubs) {

    class SubscriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.subscription_item,
            parent, false
        )
        return SubscriptionsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubscriptionsViewHolder, position: Int) {
        val item = getItem(position)

        val uri: String = "https://image.tmdb.org/t/p/original/" + item.poster_path.toString()

        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
             listener.navigateToItem(item)
        }
    }

}

private object DiffUtilCallbackSubs : DiffUtil.ItemCallback<Shows>() {
    override fun areItemsTheSame(oldItem: Shows, newItem: Shows): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shows, newItem: Shows): Boolean = oldItem == newItem
}