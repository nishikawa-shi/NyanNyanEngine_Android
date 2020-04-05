package com.ntetz.android.nyannyanengine_android.ui.setting.hashtag

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

class HashtagSettingAdapter(
    private val viewModel: HashtagSettingViewModel,
    private val parentLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HashtagSettingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HashtagSettingViewHolder {
        return HashtagSettingViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return viewModel.settingsList.size
    }

    override fun onBindViewHolder(holder: HashtagSettingViewHolder, position: Int) {
        holder.onBind(viewModel, position, parentLifecycleOwner)
    }
}
