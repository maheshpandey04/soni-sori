package org.navgurukul.saral.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import org.navgurukul.learn.ui.common.DataBoundListAdapter
import org.navgurukul.saral.R
import org.navgurukul.saral.databinding.ItemRecommendedClassBinding
import org.navgurukul.saral.datasource.network.model.ClassesContainer


class OtherCourseAdapter(callback: (ClassesContainer.Classes) -> Unit) :

    DataBoundListAdapter<ClassesContainer.Classes, ItemRecommendedClassBinding>(
        mDiffCallback = object : DiffUtil.ItemCallback<ClassesContainer.Classes>() {
            override fun areItemsTheSame(oldItem: ClassesContainer.Classes, newItem: ClassesContainer.Classes): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: ClassesContainer.Classes, newItem: ClassesContainer.Classes): Boolean {
                return false
            }
        }
    ) {
    private val mCallback = callback
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemRecommendedClassBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recommended_class, parent, false
        )
    }

    override fun bind(binding: ItemRecommendedClassBinding, item: ClassesContainer.Classes) {
        binding.course = item
        binding.root.setOnClickListener {
            mCallback.invoke(item)
        }
    }

}