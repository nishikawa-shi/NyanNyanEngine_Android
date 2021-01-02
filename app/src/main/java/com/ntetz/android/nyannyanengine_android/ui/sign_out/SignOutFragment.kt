package com.ntetz.android.nyannyanengine_android.ui.sign_out

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ntetz.android.nyannyanengine_android.MainActivity
import com.ntetz.android.nyannyanengine_android.R
import org.koin.android.viewmodel.ext.android.viewModel

class SignOutFragment : Fragment() {

    companion object {
        fun newInstance() = SignOutFragment()
    }

    private val viewModel: SignOutViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_out_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.signOutEvent.observe(viewLifecycleOwner, {
            (activity as? MainActivity)?.updateTweetList()
            findNavController().popBackStack()
        })
        viewModel.executeSignOut()
    }
}
