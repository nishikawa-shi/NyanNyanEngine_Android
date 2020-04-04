package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ntetz.android.nyannyanengine_android.databinding.HashtagSettingFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HashtagSettingFragment : Fragment() {

    companion object {
        fun newInstance() =
            HashtagSettingFragment()
    }

    private val viewModel: HashtagSettingViewModel by viewModel()
    private lateinit var binding: HashtagSettingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HashtagSettingFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
