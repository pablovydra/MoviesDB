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
import androidx.lifecycle.Observer
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
        savedInstanceState: Bundle?,
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
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        val isSubscribed = viewModel.subscriptionList.value?.filter { it.id == viewModel.selectedShow.value?.id }

        if (isSubscribed != null) {
            if (isSubscribed.isEmpty()) {
                binding.button.setImageResource(R.drawable.button_unsubscribe_search)
                binding.button.tag = R.drawable.button_unsubscribe_search
                binding.subscribeText.text = "Subscribe"
                binding.subscribeText.setTextColor(resources.getColor(R.color.white))
            } else {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                binding.subscribeText.text = "Subscribed"
                binding.subscribeText.setTextColor(resources.getColor(R.color.black_no_black))
            }
        }

        binding.buttonConstraint.setOnClickListener {
            if (binding.button.tag == R.drawable.button_subscribe) {
                binding.button.setImageResource(R.drawable.button_unsubscribe_search)
                binding.button.tag = R.drawable.button_unsubscribe_search
                binding.subscribeText.setTextColor(resources.getColor(R.color.white))
                binding.subscribeText.text = resources.getString(R.string.subscribe_button)

                viewModel.selectedShow.value?.let { selectedShow ->
                    viewModel.delete(selectedShow.id)
                }

                viewModel.showList.value?.forEach {
                    if (it.id == viewModel.selectedShow.value?.id) {
                        it.subscribed = false
                    }
                    viewModel.showListWasEdited.value = true
                }

            } else {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                binding.subscribeText.setTextColor(resources.getColor(R.color.black_no_black))
                binding.subscribeText.text = resources.getString(R.string.subscribed_button)

                viewModel.selectedShow.value?.let { selectedShow ->
                    viewModel.insert(selectedShow)
                }

                viewModel.showList.value?.forEach {
                    if (it.id == viewModel.selectedShow.value?.id) {
                        it.subscribed = true
                    }
                    viewModel.showListWasEdited.value = true
                }

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