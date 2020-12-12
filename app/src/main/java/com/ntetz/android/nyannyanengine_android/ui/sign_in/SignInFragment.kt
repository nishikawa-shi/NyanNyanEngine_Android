package com.ntetz.android.nyannyanengine_android.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ntetz.android.nyannyanengine_android.R
import org.koin.android.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {

    private val args: SignInFragmentArgs by navArgs()

    companion object {
        fun newInstance() = SignInFragment()
    }

    private val viewModel: SignInViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        println("oauth token is ${this.args.oauthToken}")
        viewModel.signInEvent.observe(viewLifecycleOwner, Observer {
            println("sign in finished! $it")
            findNavController().navigate(R.id.action_singInFragment_to_mainFragment)
        })
        viewModel.executeSignIn()
    }
}
