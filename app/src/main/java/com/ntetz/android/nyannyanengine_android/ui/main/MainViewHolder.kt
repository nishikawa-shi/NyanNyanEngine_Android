package com.ntetz.android.nyannyanengine_android.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ntetz.android.nyannyanengine_android.R
import com.ntetz.android.nyannyanengine_android.databinding.MainCellBinding
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet
import com.ntetz.android.nyannyanengine_android.util.toNyanNyan

class MainViewHolder(
    private val viewModel: MainViewModel,
    private val context: Context?,
    private val binding: MainCellBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(item: Tweet, lifecycleOwner: LifecycleOwner) {
        val context = context ?: return
        binding.root.setOnClickListener {
            binding.isNyanNyan = !(binding.isNyanNyan as Boolean)
            viewModel.logToggleTweet(bindingAdapterPosition, (binding.isNyanNyan as Boolean))
        }
        binding.isNyanNyan = !item.isError
        binding.nyanNyanTweetTextBody = item.text.toNyanNyan(context)
        binding.tweetTextBody = item.createdAt + item.text
        binding.lifecycleOwner = lifecycleOwner
    }

    companion object {
        fun create(viewModel: MainViewModel, context: Context?, parent: ViewGroup) = MainViewHolder(
            viewModel,
            context,
            DataBindingUtil.inflate<MainCellBinding>(
                LayoutInflater.from(parent.context),
                R.layout.main_cell,
                parent,
                false
            )
        )
    }
}
