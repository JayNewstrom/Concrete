package com.jaynewstrom.concretesample.details

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

internal class DetailsListAdapter : BaseAdapter() {
    override fun getCount(): Int = 50

    override fun getItem(position: Int) = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView: DetailsListItemView = if (convertView == null) {
            DetailsListItemView(parent.context)
        } else {
            convertView as DetailsListItemView
        }
        listItemView.setPosition(position)
        return listItemView
    }
}
