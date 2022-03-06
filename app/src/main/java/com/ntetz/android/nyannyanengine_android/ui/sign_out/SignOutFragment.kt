package com.ntetz.android.nyannyanengine_android.ui.sign_out

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.MainActivity
import com.ntetz.android.nyannyanengine_android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignOutFragment : Fragment() {
    private val viewModel: SignOutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.signOutEvent.observe(viewLifecycleOwner) {
            (activity as? MainActivity)?.updateTweetList()
            findNavController().popBackStack()
        }
        viewModel.executeSignOut()

        return inflater.inflate(R.layout.sign_out_fragment, container, false)
    }
}
