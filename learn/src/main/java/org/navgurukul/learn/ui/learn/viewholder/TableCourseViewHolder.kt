package org.navgurukul.learn.ui.learn.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.navgurukul.learn.R
import org.navgurukul.learn.courses.db.models.TableBaseCourseContent
import org.navgurukul.learn.courses.db.models.TableColumn
import org.navgurukul.commonui.platform.ListSpacingDecoration
import org.navgurukul.learn.ui.learn.adapter.TableAdapter

class TableCourseViewHolder(itemView: View) :
    BaseCourseViewHolder(itemView) {

    private val tableView: RecyclerView = populateStub(R.layout.item_table_content)

    override val horizontalMargin: Int
        get() = tableView.context.resources.getDimensionPixelOffset(R.dimen.spacing_4x)

    init {
        super.setHorizontalMargin(horizontalMargin)
    }

    fun bindView(item: TableBaseCourseContent) {
        super.bind(item)

        if (item.value.isNotEmpty()) {
            val noOfRows = item.value[0].items?.size?.plus(1) ?: 0
            val tableAdapter = TableAdapter(noOfRows, getFlattenedTableList(item.value))

            tableView.layoutManager = GridLayoutManager(
                tableView.context, noOfRows, GridLayoutManager.HORIZONTAL, false
            )
            tableView.adapter = tableAdapter
            tableView.addItemDecoration(
                ListSpacingDecoration(
                    tableView.context,
                    R.dimen.table_margin_offset
                )
            )

            tableAdapter.notifyDataSetChanged()
        }
    }


    private fun getFlattenedTableList(list: List<TableColumn>): List<String> {
        val flatList = ArrayList<String>()
        for (item in list) {
            flatList.add(item.header ?: "")
            item.items?.let { flatList.addAll(it) }
        }
        return flatList
    }

}
