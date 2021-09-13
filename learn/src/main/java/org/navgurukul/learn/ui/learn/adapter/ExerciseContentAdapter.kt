package org.navgurukul.learn.ui.learn.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.item_slug_detail.view.*
import org.navgurukul.commonui.resources.StringProvider
import org.navgurukul.learn.R
import org.navgurukul.learn.courses.db.models.*
import org.navgurukul.learn.databinding.ItemSlugDetailBinding
import org.navgurukul.learn.ui.common.DataBoundListAdapter


class ExerciseContentAdapter(callback: (BaseCourseContent) -> Unit) :
    DataBoundListAdapter<BaseCourseContent, ItemSlugDetailBinding>(
            mDiffCallback = object : DiffUtil.ItemCallback<BaseCourseContent>() {
                override fun areItemsTheSame(
                        oldItem: BaseCourseContent,
                        newItem: BaseCourseContent
                ): Boolean {
                    return oldItem == newItem
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                        oldItem: BaseCourseContent,
                        newItem: BaseCourseContent
                ): Boolean {
                    return oldItem == newItem
                }
            }
    ) {

    private val mCallback = callback
    override fun createBinding(parent: ViewGroup, viewType: Int): ItemSlugDetailBinding {
        return DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_slug_detail, parent, false
        )
    }

    override fun bind(
            holder: DataBoundViewHolder<ItemSlugDetailBinding>,
            item: BaseCourseContent
    ) {
        val binding = holder.binding
        binding.imageViewPlay.setOnClickListener {
            mCallback.invoke(item)
        }
        binding.bannerButton.setOnClickListener {
            mCallback.invoke(item)
        }
        binding.linkContent.setOnClickListener{
            mCallback.invoke(item)
        }
        bindItem(item, binding)
    }

    private fun bindItem(
            item: BaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        when (item) {
            is TextBaseCourseContent -> {
                initTextContent(item, binding)
            }
            is CodeBaseCourseContent -> {
                initCodeView(item, binding)
            }
            is LinkBaseCourseContent -> {
                initLinkView(item, binding)
            }
            is YoutubeBaseCourseContent -> {
                initYouTubeView(item, binding)
            }
            is ImageBaseCourseContent -> {
                initImageView(item, binding)
            }
            is BannerCourseContent -> {
                initTryTypingView(item, binding)
            }
            is BlockQuoteBaseCourseContent -> {
                initBlockQuoteView(item, binding)
            }
            is HeaderBaseCourseContent -> {
                initHeaderView(item, binding)
            }
            is TableBaseCourseContent -> {
//                initTableView(item, binding)
            }
            is UnknownBaseCourseContent -> {
                initUnknown(binding)
            }
        }
    }

    private fun initUnknown(binding: ItemSlugDetailBinding) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE
    }

    private fun initTextContent(
            item: TextBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        binding.decorContent.visibility = View.GONE
        binding.textContent.visibility = View.GONE

        item.value?.let { text ->

            item.decoration?.let {
                binding.decorContent.visibility = View.VISIBLE
                binding.decorContent.setDecoratedText(text, it)
            } ?: setTextContent(binding.textContent, text)

        }

    }

    private fun setTextContent(textContent: TextView, text: String) {
        textContent.visibility = View.VISIBLE

        textContent.apply {
            this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun initBlockQuoteView(
            item: BlockQuoteBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        item.value?.let {
            binding.blockQuoteLayout.visibility = View.VISIBLE

            binding.blockQuoteLayout.blockQuoteText.apply {
                this.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }
    }

    private fun initHeaderView(
            item: HeaderBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        item.value?.let {
            binding.titleContent.visibility = View.VISIBLE

            binding.titleContent.apply {
                this.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
            }
        }
    }

    private fun initLinkView(
            item: LinkBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.textContent.visibility = View.GONE

        item.value?.let {
            binding.linkContent.visibility = View.VISIBLE

            val stringProvider = StringProvider(binding.linkContent.resources)
            binding.linkContent.apply {
                this.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
                this.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
            }

        }
    }


    private fun initCodeView(
            item: CodeBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        binding.codeLayout.visibility = View.VISIBLE

        binding.codeTitle.visibility = View.GONE
        item.title?.let{
            binding.codeTitle.visibility = View.VISIBLE
            binding.codeTitle.text = it
        }

        when(item.codeTypes) {
            CodeType.javascript -> {
                binding.imageViewPlay.visibility = View.GONE
            }
            CodeType.python -> {
                binding.imageViewPlay.visibility = View.VISIBLE
            }
            else -> {
                binding.imageViewPlay.visibility = View.GONE
            }
        }

        binding.codeBody.text = HtmlCompat.fromHtml(item.value
                ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
    }


    private fun initYouTubeView(item: YoutubeBaseCourseContent, binding: ItemSlugDetailBinding) {
        binding.textContent.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        binding.youtubeView.visibility = View.VISIBLE

        binding.youtubeView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                item.value?.let { youTubePlayer.loadVideo(it, 0f) }
            }
        })
    }


    private fun initImageView(
            item: ImageBaseCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.relBanner.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        binding.imageView.visibility = View.VISIBLE

        item.value?.let { url ->
            Glide.with(binding.imageView.context).load(url).into(binding.imageView);
        }
        binding.imageView.contentDescription = item.alt
    }

    private fun initTryTypingView(
            item: BannerCourseContent,
            binding: ItemSlugDetailBinding
    ) {
        binding.youtubeView.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.textContent.visibility = View.GONE
        binding.imageViewPlay.visibility = View.GONE
        binding.blockQuoteLayout.visibility = View.GONE
        binding.codeLayout.visibility = View.GONE
        binding.decorContent.visibility = View.GONE
        binding.titleContent.visibility = View.GONE
        binding.linkContent.visibility = View.GONE

        binding.relBanner.visibility = View.VISIBLE

        binding.bannerButton.text = item.action?.label
        binding.bannerTitle.text = item.title
        binding.bannerBody.text = item.value
    }

}
