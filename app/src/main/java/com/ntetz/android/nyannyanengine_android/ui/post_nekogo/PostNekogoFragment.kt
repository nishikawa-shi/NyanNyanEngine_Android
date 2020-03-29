package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ntetz.android.nyannyanengine_android.databinding.PostNekogoFragmentBinding
import org.koin.android.viewmodel.ext.android.viewModel

class PostNekogoFragment : Fragment() {

    companion object {
        fun newInstance() = PostNekogoFragment()
    }

    private val viewModel: PostNekogoViewModel by viewModel()
    private lateinit var binding: PostNekogoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostNekogoFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
