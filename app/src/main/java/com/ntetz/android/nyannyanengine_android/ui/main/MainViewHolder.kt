package com.ntetz.android.nyannyanengine_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainCellBinding

class MainViewHolder(
    private val binding: MainCellBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(viewModel: MainViewModel, position: Int, lifecycleOwner: LifecycleOwner) {
        binding.viewModel = viewModel
        binding.position = position
        binding.lifecycleOwner = lifecycleOwner
    }

    companion object {
        fun create(parent: ViewGroup) = MainViewHolder(
            DataBindingUtil.inflate<MainCellBinding>(
                LayoutInflater.from(parent.context),
                R.layout.main_cell,
                parent,
                false
            )
        )
    }
}
