package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.models.entity.Tv
import com.example.movies.ui.home.adapter.RecommendedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

@AndroidEntryPoint
class HomeFragment : Fragment(), RecommendedAdapter.RecommendedAdapterActions {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RecommendedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        adapter = RecommendedAdapter(this)
        binding.recycler.adapter = adapter

        viewModel.setAdapterOnView.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.showList.value)
        })
        binding.recycler.layoutManager = LinearLayoutManager(this.context)
        binding.recycler.setHasFixedSize(true)

        binding.recycler.addOnScrollListener(
            object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (recyclerView.canScrollVertically(-1)) {
                        viewModel.getShows()
                    }
                }
            })

        return binding.root
    }

    override fun addToFavorite(tv: Tv, callback: (() -> Unit)?) {
    }

    override fun removeFavorite(tv: Tv, callback: (() -> Unit)?) {
    }

    override fun navigateToItem(id: Int) {
    }

}