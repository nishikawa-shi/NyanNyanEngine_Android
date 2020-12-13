package com.ntetz.android.nyannyanengine_android.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

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

        val adapter = MainAdapter(viewModel, this)
        binding.tweetList.adapter = adapter
        viewModel.tweetsEvent.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
        viewModel.initialize()

        binding.postNekogoFragmentOpenButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_postNekogoFragment)
        }
    }
}
