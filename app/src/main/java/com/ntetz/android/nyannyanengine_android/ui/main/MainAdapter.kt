package com.ntetz.android.nyannyanengine_android.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.ntetz.android.nyannyanengine_android.model.entity.dao.retrofit.Tweet

class MainAdapter(
    private val viewModel: MainViewModel,
    private val context: Context?,
    private val parentLifecycleOwner: LifecycleOwner
) : PagingDataAdapter<Tweet, MainViewHolder>(REPO_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.create(viewModel, context, parent)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(getItem(position) ?: return, parentLifecycleOwner)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Tweet>() {
            override fun areItemsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Tweet, newItem: Tweet): Boolean =
                oldItem == newItem
        }
    }
}
