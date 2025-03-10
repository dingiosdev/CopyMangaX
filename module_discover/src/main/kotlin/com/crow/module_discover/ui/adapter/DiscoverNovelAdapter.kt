package com.crow.module_discover.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.NoTransition
import com.crow.base.copymanga.entity.IBookAdapterColor
import com.crow.base.copymanga.formatValue
import com.crow.base.copymanga.getComicCardHeight
import com.crow.base.copymanga.getComicCardWidth
import com.crow.base.copymanga.glide.AppGlideProgressFactory
import com.crow.base.copymanga.mSize10
import com.crow.base.tools.extensions.BASE_ANIM_200L
import com.crow.base.tools.extensions.doOnClickInterval
import com.crow.base.ui.adapter.BaseGlideLoadingViewHolder
import com.crow.module_discover.databinding.DiscoverFragmentRvBinding
import com.crow.module_discover.model.resp.novel_home.DiscoverNovelHomeResult

class DiscoverNovelAdapter(
    inline val mDoOnTapComic: (DiscoverNovelHomeResult) -> Unit
) : PagingDataAdapter<DiscoverNovelHomeResult, DiscoverNovelAdapter.ViewHolder>(DiffCallback())
    , IBookAdapterColor<DiscoverNovelAdapter.ViewHolder> {

    class DiffCallback : DiffUtil.ItemCallback<DiscoverNovelHomeResult>() {
        override fun areItemsTheSame(oldItem: DiscoverNovelHomeResult, newItem: DiscoverNovelHomeResult): Boolean {
            return oldItem.mName == newItem.mName
        }

        override fun areContentsTheSame(oldItem: DiscoverNovelHomeResult, newItem: DiscoverNovelHomeResult): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(binding: DiscoverFragmentRvBinding) : BaseGlideLoadingViewHolder<DiscoverFragmentRvBinding>(binding)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        return ViewHolder(DiscoverFragmentRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)).also { vh ->

            val layoutParams = vh.rvBinding.discoverRvImage.layoutParams
            layoutParams.width = getComicCardWidth() - mSize10
            layoutParams.height = getComicCardHeight()

            vh.rvBinding.discoverRvBookCard.doOnClickInterval {
                mDoOnTapComic(getItem(vh.absoluteAdapterPosition) ?: return@doOnClickInterval)
            }
        }
    }



    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        vh.rvBinding.discoverLoading.isVisible = true
        vh.rvBinding.discoverProgressText.isVisible = true
        vh.rvBinding.discoverProgressText.text = AppGlideProgressFactory.PERCENT_0
        vh.mAppGlideProgressFactory?.doRemoveListener()?.doClean()
        vh.mAppGlideProgressFactory = AppGlideProgressFactory.createGlideProgressListener(item.mImageUrl) { _, _, percentage, _, _ ->
            vh.rvBinding.discoverProgressText.text = AppGlideProgressFactory.getProgressString(percentage)
        }
        
        Glide.with(vh.itemView.context)
            .load(item.mImageUrl)
            .listener(vh.mAppGlideProgressFactory?.getRequestListener())
            .transition(GenericTransitionOptions<Drawable>().transition { dataSource, _ ->
                if (dataSource == DataSource.REMOTE) {
                    vh.rvBinding.discoverLoading.isInvisible = true
                    vh.rvBinding.discoverProgressText.isInvisible = true
                    DrawableCrossFadeTransition(BASE_ANIM_200L.toInt(), true)
                } else {
                    vh.rvBinding.discoverLoading.isInvisible = true
                    vh.rvBinding.discoverProgressText.isInvisible = true
                    NoTransition<Drawable>()
                }
            })
            .into(vh.rvBinding.discoverRvImage)

        vh.rvBinding.discoverRvName.text = item.mName
        vh.rvBinding.discoverRvAuthor.text = item.mAuthor.joinToString { it.mName }
        vh.rvBinding.discoverRvHot.text = formatValue(item.mPopular)
        vh.rvBinding.discoverRvTime.text = item.mDatetimeUpdated
    }

    override fun setColor(vh: ViewHolder, color: Int) {
        vh.rvBinding.discoverRvName.setTextColor(color)
        vh.rvBinding.discoverRvAuthor.setTextColor(color)
        vh.rvBinding.discoverRvTime.setTextColor(color)
        vh.rvBinding.discoverRvHot.setTextColor(color)
    }
}
