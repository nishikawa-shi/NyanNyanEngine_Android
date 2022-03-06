package com.ntetz.android.nyannyanengine_android.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.ntetz.android.nyannyanengine_android.MainActivity
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding
    private val adapter: MainAdapter by lazy { MainAdapter(viewModel, this.context, this) }
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.tweetList.adapter = adapter
        binding.tweetListFrame.setOnRefreshListener {
            adapter.refresh()
            binding.tweetListFrame.isRefreshing = false
        }
        binding.postNekogoFragmentOpenButton.setOnClickListener {
            viewModel.logOpenPostNekogoScreen()
            findNavController().navigate(R.id.action_mainFragment_to_postNekogoFragment)
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.tweetProgressLoader.isGone = !(it.refresh is LoadState.Loading)
            }
        }

        viewModel.userInfoEvent.observe(viewLifecycleOwner, {
            (activity as? MainActivity)?.updateUserInfo(it)
        })
        viewModel.nyanNyanConfigEvent.observe(viewLifecycleOwner, {
            viewModel.loadNyanNyanUserInfo()
        })
        viewModel.nyanNyanUserEvent.observe(viewLifecycleOwner, {
            // Firestoreからいい感じで同期的に取得できる処理があれば、configとuserの取得処理に2つのLiveDataを使わなくてすみそう。
            (activity as? MainActivity)?.updateNyanNyanUserInfo(it)
        })
        (activity as? MainActivity)?.refreshTweetListEvent?.observe(viewLifecycleOwner, {
            if (!it) {
                return@observe
            }
            viewModel.loadUserInfo()
            adapter.refresh()
        })
        viewModel.loadUserInfo()
        setupAdapter()

        return binding.root
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
