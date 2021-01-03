package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.R
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
        binding.inputText = context?.getString(R.string.post_input_original_text)
        binding.testButton.setOnClickListener {
            viewModel.postNekogo(
                binding.nekogoResult.text.toString(),
                context ?: return@setOnClickListener
            )
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel.postTweetEvent.observe(viewLifecycleOwner, {
            val textBody = listOf(
                it?.point,
                context?.getString(R.string.post_point),
                it?.text?.splitToSequence("\n")?.first(),
                context?.getString(R.string.post_result)
            ).joinToString("")
            Toast.makeText(
                context,
                textBody,
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_postNekogoFragment_to_mainFragment)
        })
        viewModel.userInfoEvent.observe(viewLifecycleOwner, {
            binding.testButton.isEnabled = (it != null)
        })
        viewModel.loadUserInfo()
        super.onActivityCreated(savedInstanceState)
    }
}
