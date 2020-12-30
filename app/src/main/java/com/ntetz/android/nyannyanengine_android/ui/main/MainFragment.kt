package com.ntetz.android.nyannyanengine_android.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainFragmentBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding
    private val adapter: MainAdapter by lazy { MainAdapter(this.context, this) }
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.tweetList.adapter = adapter
        binding.tweetListFrame.setOnRefreshListener {
            adapter.refresh()
            binding.tweetListFrame.isRefreshing = false
        }
        binding.postNekogoFragmentOpenButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_postNekogoFragment)
        }
        setupAdapter()
    }

    private fun setupAdapter() {
        searchJob?.cancel()
        searchJob = viewModel.viewModelScope.launch {
            viewModel.tweetStream.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
