package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.ntetz.android.nyannyanengine_android.databinding.HashtagSettingFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HashtagSettingFragment : Fragment() {
    private val viewModel: HashtagSettingViewModel by viewModels()
    private lateinit var binding: HashtagSettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HashtagSettingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.initialize()

        val adapter = HashtagSettingAdapter(viewModel, this)
        binding.hashtagList.adapter = adapter
        viewModel.defaultHashtagComponents.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })

        binding.hashtagList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return binding.root
    }
}
