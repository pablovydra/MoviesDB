package com.example.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.FragmentHomeBinding
import com.example.movies.models.database.Shows
import com.example.movies.models.entity.Tv
import com.example.movies.ui.home.adapter.AdapterActions
import com.example.movies.ui.home.adapter.RecommendedAdapter
import com.example.movies.ui.home.adapter.SubscriptionsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterActions {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: RecommendedAdapter
    private lateinit var adapterSubs: SubscriptionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        viewModel.getSubscriptions()

        adapter = RecommendedAdapter(this)
        binding.recycler.adapter = adapter
        viewModel.setRecommendedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.showList.value)
        })
        binding.recycler.layoutManager = LinearLayoutManager(this.context)
        binding.recycler.setHasFixedSize(true)

        adapterSubs = SubscriptionsAdapter(this)
        binding.recyclerSubs.adapter = adapterSubs
        viewModel.setSubscriptionsList.observe(viewLifecycleOwner, Observer {
            adapterSubs.submitList(viewModel.subscriptionList.value)
        })

        binding.recyclerSubs.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerSubs.setHasFixedSize(true)

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

        binding.searchIcon.setOnClickListener {
            navigateToSearch()
        }

        return binding.root
    }

    override fun addToFavorite(tv: Tv, callback: (() -> Unit)?) {}

    override fun removeFavorite(tv: Tv, callback: (() -> Unit)?) {}

    private fun navigateToSearch() {
        view?.findNavController()?.navigate(R.id.action_homeFragment_to_searchFragment)
    }

    override fun navigateToItem(show: Shows) {
        viewModel.selectedShow.value = show
        view?.findNavController()
            ?.navigate(R.id.action_global_DetailsFragment)
    }

}