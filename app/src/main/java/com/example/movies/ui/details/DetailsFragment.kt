package com.example.movies.ui.details

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.movies.R
import com.example.movies.databinding.FragmentDetailsBinding
import com.example.movies.ui.home.HomeViewModel


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.getSubscriptions()

        val uri: String = "https://image.tmdb.org/t/p/original/" + viewModel.selectedShow.value?.poster_path.toString()

        Glide.with(requireContext())
            .load(uri)
            .into(binding.cover)

        Glide.with(requireContext())
            .load(uri)
            .into(binding.backgroundImage)

        binding.name.text = viewModel.selectedShow.value?.name

        binding.overview.text = viewModel.selectedShow.value?.overview

        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.cover.setImageBitmap(resource)
                    binding.container.setBackgroundColor(getDominantColor(resource))
                    binding.subscribeText.setTextColor(getDominantColor(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        val isSubscribed = viewModel.subscriptionList.value?.filter { it.showId == viewModel.selectedShow.value?.id }
        Log.i("skywalker", "isSubscribed: ${isSubscribed?.size} to ${viewModel.selectedShow.value?.name}")

        if (isSubscribed != null) {
            if (isSubscribed.isEmpty()) {
                binding.button.setImageResource(R.drawable.button_unsubscribe)
                binding.button.tag = R.drawable.button_unsubscribe
                binding.subscribeText.text = "Subscribe"
            } else {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                binding.subscribeText.text = "Subscribed"
            }
        }

        binding.buttonConstraint.setOnClickListener {
            if (binding.button.tag == R.drawable.button_subscribe) {
                binding.button.setImageResource(R.drawable.button_unsubscribe)
                binding.button.tag = R.drawable.button_unsubscribe
                viewModel.selectedShow.value?.let { selectedShow -> viewModel.delete(selectedShow.id) }
                binding.subscribeText.text = "Subscribe"
            } else {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                viewModel.selectedShow.value?.let { selectedShow -> viewModel.insert(selectedShow) }
                binding.subscribeText.text = "Subscribed"
            }
        }

        return binding.root
    }

    fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

}