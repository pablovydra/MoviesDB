package com.example.movies.ui.adapter

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
import com.example.movies.models.database.Shows

class RecommendedAdapter(
    private val listener: AdapterActions
) :
    ListAdapter<Shows, RecommendedAdapter.RecommendedViewHolder>(DiffUtilCallback) {

    class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val genre: TextView = itemView.findViewById(R.id.genre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.recommended_item,
            parent, false
        )
        return RecommendedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecommendedViewHolder, position: Int) {
        val item = getItem(position)

        holder.title.text = item.name

        holder.genre.text = item.genre

        val uri: String = "https://image.tmdb.org/t/p/original/" + item.poster_path.toString()

        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            listener.navigateToItem(item)
        }
    }

}

private object DiffUtilCallback : DiffUtil.ItemCallback<Shows>() {
    override fun areItemsTheSame(oldItem: Shows, newItem: Shows): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shows, newItem: Shows): Boolean = oldItem == newItem
}