package com.example.movies.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.databinding.FragmentSearchBinding
import com.example.movies.models.database.Shows
import com.example.movies.models.entity.Tv
import com.example.movies.ui.home.HomeViewModel
import com.example.movies.ui.home.adapter.AdapterActions
import com.example.movies.ui.home.adapter.RecommendedAdapter
import com.example.movies.ui.home.adapter.SearchAdapter

class SearchFragment : Fragment(), AdapterActions {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: HomeViewModel by activityViewModels()
     private lateinit var adapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        adapter = SearchAdapter(this)
        binding.recycler.adapter = adapter

        viewModel.setRecommendedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.showList.value)
        })

        adapter.submitList(viewModel.showList.value)

        binding.recycler.layoutManager = LinearLayoutManager(this.context)
        binding.recycler.setHasFixedSize(true)

        return binding.root
    }

    override fun addToFavorite(tv: Tv, callback: (() -> Unit)?) {

    }

    override fun removeFavorite(tv: Tv, callback: (() -> Unit)?) {

    }

    override fun navigateToItem(show: Shows) {
        viewModel.selectedShow.value = show
        view?.findNavController()
            ?.navigate(R.id.action_global_DetailsFragment)
    }
}
