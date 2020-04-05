package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.HashtagSettingCellBinding

class HashtagSettingViewHolder(
    private val binding: HashtagSettingCellBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(viewModel: HashtagSettingViewModel, position: Int, lifecycleOwner: LifecycleOwner) {
        binding.viewModel = viewModel
        binding.position = position
        binding.lifecycleOwner = lifecycleOwner
    }

    companion object {
        fun create(parent: ViewGroup) = HashtagSettingViewHolder(
            DataBindingUtil.inflate<HashtagSettingCellBinding>(
                LayoutInflater.from(parent.context),
                R.layout.hashtag_setting_cell,
                parent,
                false
            )
        )
    }
}
