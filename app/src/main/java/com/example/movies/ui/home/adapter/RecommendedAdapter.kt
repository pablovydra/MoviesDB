package com.example.movies.ui.home.adapter

import android.content.Context
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
import com.example.movies.models.entity.Tv

class RecommendedAdapter(
    private val listener: RecommendedAdapterActions
) :
    ListAdapter<Tv, RecommendedAdapter.RecommendedViewHolder>(DiffUtilCallback) {

    interface RecommendedAdapterActions {
        fun addToFavorite(tv: Tv, callback: (() -> Unit)? = null)
        fun removeFavorite(tv: Tv, callback: (() -> Unit)? = null)
        fun navigateToItem(id: Int)
    }

    class RecommendedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val category: TextView = itemView.findViewById(R.id.category)
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

        holder.title.text = item.original_name

        if (item.keywords?.isNotEmpty() == true) {
            holder.category.text = item.keywords!![0].name ?: ""
        }

        val uri: String = "https://image.tmdb.org/t/p/original/" + item.poster_path.toString()

        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            listener.navigateToItem(item.id)
        }
    }

}

private object DiffUtilCallback : DiffUtil.ItemCallback<Tv>() {
    override fun areItemsTheSame(oldItem: Tv, newItem: Tv): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem == newItem
}