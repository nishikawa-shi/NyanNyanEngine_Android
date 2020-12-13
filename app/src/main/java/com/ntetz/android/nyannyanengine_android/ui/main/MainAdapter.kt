package com.ntetz.android.nyannyanengine_android.ui.main

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(
    private val viewModel: MainViewModel,
    private val parentLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return viewModel.tweetsEvent.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(viewModel, position, parentLifecycleOwner)
    }
}
