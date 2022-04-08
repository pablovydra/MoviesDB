package com.example.movies.ui.details

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

        val uri: String = "https://image.tmdb.org/t/p/original/" + viewModel.selectedShow.value?.poster_path.toString()

        Glide.with(requireContext())
            .load(uri)
            .into(binding.cover)

        binding.name.text = viewModel.selectedShow.value?.name

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

        return binding.root
    }

    fun getDominantColor(bitmap: Bitmap?): Int {
        val newBitmap = Bitmap.createScaledBitmap(bitmap!!, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

}