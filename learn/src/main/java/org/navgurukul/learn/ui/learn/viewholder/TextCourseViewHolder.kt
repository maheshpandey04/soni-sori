package org.navgurukul.learn.ui.learn.viewholder

import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat
import org.navgurukul.learn.R
import org.navgurukul.learn.courses.db.models.TextBaseCourseContent

class TextCourseViewHolder(itemView: View) :
    BaseCourseViewHolder(itemView) {

    private val textView: TextView = populateStub(R.layout.item_text_content)

    override val horizontalMargin: Int
        get() = textView.context.resources.getDimensionPixelOffset(R.dimen.spacing_4x)

    init {
        super.setHorizontalMargin(horizontalMargin)
    }

    fun bindView(item: TextBaseCourseContent) {
        super.bind(item)

        setTextContent(textView, item.value)
    }

    private fun setTextContent(textContent: TextView, text: String) {
        textContent.apply {
            this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }
}

