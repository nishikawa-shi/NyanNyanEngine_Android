package com.ntetz.android.nyannyanengine_android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainCellBinding
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet

class MainViewHolder(
    private val binding: MainCellBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: Tweet, lifecycleOwner: LifecycleOwner) {
        binding.root.setOnClickListener {
            binding.isNyanNyan = !(binding.isNyanNyan as Boolean)
        }
        binding.isNyanNyan = true
        binding.nyanNyanTweetTextBody = "nya--n"
        binding.tweetTextBody = item.createdAt + item.text
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
