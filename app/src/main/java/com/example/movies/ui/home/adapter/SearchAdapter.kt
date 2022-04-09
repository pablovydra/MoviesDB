package com.example.movies.ui.home.adapter

import android.content.res.Resources
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
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(private val listener: AdapterActions) :
    ListAdapter<Shows, SearchAdapter.SearchViewHolder>(DiffUtilCallbackSearch) {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val name: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.search_item,
            parent, false
        )
        return SearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)

        val res: Resources = holder.itemView.context.resources

        val uri: String = "https://image.tmdb.org/t/p/original/" + item.poster_path.toString()

        Glide.with(holder.itemView.context)
            .load(uri)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            listener.navigateToItem(item)
        }

        holder.itemView.name.text = item.name
        holder.itemView.genre.text = item.genre

        if (item.subscribed) {
            holder.itemView.subscribe_background.setImageResource(R.drawable.button_subscribe)
            holder.itemView.subscribe_background.tag = R.drawable.button_subscribe
            holder.itemView.subscribe_text.text = res.getString(R.string.subscribed_button)
            holder.itemView.subscribe_text.setTextColor(res.getColor(R.color.black_no_black))
        } else {
            holder.itemView.subscribe_background.setImageResource(R.drawable.button_unsubscribe_search)
            holder.itemView.subscribe_background.tag = R.drawable.button_unsubscribe_search
            holder.itemView.subscribe_text.text = res.getString(R.string.subscribe_button)
            holder.itemView.subscribe_text.setTextColor(res.getColor(R.color.white))
        }

        holder.itemView.constraint_button.setOnClickListener {
            if (holder.itemView.subscribe_background.tag == R.drawable.button_subscribe) {
                holder.itemView.subscribe_background.setImageResource(R.drawable.button_unsubscribe_search)
                holder.itemView.subscribe_background.tag = R.drawable.button_unsubscribe_search
                holder.itemView.subscribe_text.text = res.getString(R.string.subscribe_button)
                holder.itemView.subscribe_text.setTextColor(res.getColor(R.color.white))
                listener.deleteSubscribe(item)
            } else {
                holder.itemView.subscribe_background.setImageResource(R.drawable.button_subscribe)
                holder.itemView.subscribe_background.tag = R.drawable.button_subscribe
                holder.itemView.subscribe_text.text = res.getString(R.string.subscribed_button)
                holder.itemView.subscribe_text.setTextColor(res.getColor(R.color.black_no_black))
                listener.addSubscribe(item)
            }
        }
    }

}

private object DiffUtilCallbackSearch : DiffUtil.ItemCallback<Shows>() {
    override fun areItemsTheSame(oldItem: Shows, newItem: Shows): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Shows, newItem: Shows): Boolean = oldItem == newItem
}