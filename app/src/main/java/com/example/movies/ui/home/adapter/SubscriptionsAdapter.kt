package com.example.movies.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.models.entity.Shows
import com.example.movies.models.entity.Tv
import com.example.movies.models.subscriptions.Subscription

class SubscriptionsAdapter(
    private val listener: AdapterActions
) :
    ListAdapter<Subscription, SubscriptionsAdapter.SubscriptionsViewHolder>(DiffUtilCallbackSubs) {

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
             listener.navigateToShowId(item.showId)
        }
    }

}

private object DiffUtilCallbackSubs : DiffUtil.ItemCallback<Subscription>() {
    override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean = oldItem == newItem
}