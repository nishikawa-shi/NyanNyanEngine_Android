package com.ntetz.android.nyannyanengine_android.ui.post_nekogo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.PostNekogoFragmentBinding
import com.ntetz.android.nyannyanengine_android.model.config.DefaultUserConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostNekogoFragment : Fragment() {

    companion object {
        fun newInstance() = PostNekogoFragment()
    }

    private val viewModel: PostNekogoViewModel by viewModels()
    private lateinit var binding: PostNekogoFragmentBinding

    override fun onStop() {
        super.onStop()
        closeKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PostNekogoFragmentBinding.inflate(inflater, container, false)
        binding.tweetButton.setOnClickListener {
            viewModel.postNekogo(
                binding.nekogoResult.text.toString(),
                context ?: return@setOnClickListener
            )
        }
        binding.input.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                closeKeyboard()
            }
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
                Toast.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
        })
        viewModel.userInfoEvent.observe(viewLifecycleOwner, {
            binding.signedIn = (it != DefaultUserConfig.getNotSignInUser(context ?: return@observe))
        })
        viewModel.loadUserInfo()
        super.onActivityCreated(savedInstanceState)
    }

    private fun closeKeyboard() {
        val manager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager ?: return
        manager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
