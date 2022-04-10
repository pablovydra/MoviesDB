package com.example.movies.ui.details

import android.animation.ObjectAnimator
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
import com.example.movies.databinding.FragmentDetails2Binding
import com.example.movies.ui.home.HomeViewModel
import com.example.movies.utils.ImageUtils
import com.google.android.material.appbar.AppBarLayout


class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetails2Binding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDetails2Binding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.getSubscriptions()
        val selectedShow = viewModel.selectedShow.value

        val uri: String = "https://image.tmdb.org/t/p/original/" + selectedShow?.poster_path.toString()

        Glide.with(requireContext())
            .load(uri)
            .into(binding.cover)

        Glide.with(requireContext())
            .load(uri)
            .into(binding.backgroundImage)

        binding.name.text = selectedShow?.name

        binding.overview.text = selectedShow?.overview

        binding.year.text = selectedShow?.first_air_date?.split("-")?.first()

        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.cover.setImageBitmap(resource)
                    binding.container.setBackgroundColor(ImageUtils.getDominantColor(resource))

                    binding.overview.setTextColor(ImageUtils.getBlackOrWhiteColor(ImageUtils.getDominantColor(resource)))
                    binding.name.setTextColor(ImageUtils.getBlackOrWhiteColor(ImageUtils.getDominantColor(resource)))
                    binding.overviewTitle.setTextColor(ImageUtils.getBlackOrWhiteColor(ImageUtils.getDominantColor(resource)))
                    binding.year.setTextColor(ImageUtils.getBlackOrWhiteColor(ImageUtils.getDominantColor(resource)))
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

        if (selectedShow != null) {
            if (selectedShow.subscribed) {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                binding.subscribeText.text = "Subscribed"
                binding.subscribeText.setTextColor(resources.getColor(R.color.black_no_black))
            } else {
                binding.button.setImageResource(R.drawable.button_unsubscribe_search)
                binding.button.tag = R.drawable.button_unsubscribe_search
                binding.subscribeText.text = "Subscribe"
                binding.subscribeText.setTextColor(resources.getColor(R.color.white))
            }
        }

        binding.buttonConstraint.setOnClickListener {
            if (binding.button.tag == R.drawable.button_subscribe) {
                binding.button.setImageResource(R.drawable.button_unsubscribe_search)
                binding.button.tag = R.drawable.button_unsubscribe_search
                binding.subscribeText.setTextColor(resources.getColor(R.color.white))
                binding.subscribeText.text = resources.getString(R.string.subscribe_button)

                selectedShow?.let { selectedShow ->
                    viewModel.delete(selectedShow.id)
                }

                viewModel.showList.value?.forEach {
                    if (it.id == selectedShow?.id) {
                        it.subscribed = false
                    }
                    viewModel.showListWasEdited.value = true
                }

            } else {
                binding.button.setImageResource(R.drawable.button_subscribe)
                binding.button.tag = R.drawable.button_subscribe
                binding.subscribeText.setTextColor(resources.getColor(R.color.black_no_black))
                binding.subscribeText.text = resources.getString(R.string.subscribed_button)

                selectedShow?.let { selectedShow ->
                    viewModel.insert(selectedShow)
                }

                viewModel.showList.value?.forEach {
                    if (it.id == selectedShow?.id) {
                        it.subscribed = true
                    }
                    viewModel.showListWasEdited.value = true
                }
            }
        }

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val offsetAlpha = appBarLayout.y / appBarLayout.totalScrollRange

            val scaleDownX = ObjectAnimator.ofFloat(binding.headerConstraint, "scaleX", offsetAlpha + 1)
            val scaleDownY = ObjectAnimator.ofFloat(binding.headerConstraint, "scaleY", offsetAlpha + 1)
            scaleDownX.setDuration(0).start()
            scaleDownY.setDuration(0).start()

            val alpha = 1 - (offsetAlpha * -1)
            binding.headerConstraint.alpha = alpha

        })

        return binding.root
    }


}