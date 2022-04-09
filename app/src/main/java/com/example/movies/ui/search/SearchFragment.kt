package com.example.movies.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.databinding.FragmentSearchBinding
import com.example.movies.models.database.Shows
import com.example.movies.ui.home.HomeViewModel
import com.example.movies.ui.home.adapter.AdapterActions
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

        viewModel.getSubscriptions()

        adapter = SearchAdapter(this)

        adapter.setData(viewModel.showList.value)

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter.filter.filter(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        binding.recycler.adapter = adapter

        viewModel.setRecommendedList.observe(viewLifecycleOwner, Observer {
            adapter.setData(viewModel.showList.value)
        })

        viewModel.showListWasEdited.observe(viewLifecycleOwner, Observer {
            adapter.setData(viewModel.showList.value)
        })

        binding.recycler.layoutManager = LinearLayoutManager(this.context)
        binding.recycler.setHasFixedSize(true)

        binding.searchClose.setOnClickListener {
            binding.search.text.clear()
        }

        binding.cancelSearch.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchClose.visibility = View.GONE

        binding.search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (s.isNullOrEmpty()) binding.searchClose.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.searchClose.visibility = View.VISIBLE
            }
        })

        return binding.root
    }

    override fun addSubscribe(show: Shows, callback: (() -> Unit)?) {
        viewModel.insert(show)
    }

    override fun deleteSubscribe(show: Shows, callback: (() -> Unit)?) {
        viewModel.delete(show.id)
    }

    override fun navigateToItem(show: Shows) {
        viewModel.selectedShow.value = show
        view?.findNavController()
            ?.navigate(R.id.action_global_DetailsFragment)
    }

}
