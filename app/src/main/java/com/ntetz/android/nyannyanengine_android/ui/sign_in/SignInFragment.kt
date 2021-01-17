package com.ntetz.android.nyannyanengine_android.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ntetz.android.nyannyanengine_android.MainActivity
import com.ntetz.android.nyannyanengine_android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private val args: SignInFragmentArgs by navArgs()

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.signInEvent.observe(viewLifecycleOwner, Observer {
            (activity as? MainActivity)?.updateTweetList()
            findNavController().navigate(R.id.action_singInFragment_to_mainFragment)
        })
        viewModel.executeSignIn(
            oauthVerifier = this.args.oauthVerifier,
            oauthToken = this.args.oauthToken
        )
    }
}
