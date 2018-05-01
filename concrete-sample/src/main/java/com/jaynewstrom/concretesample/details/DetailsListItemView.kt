package com.jaynewstrom.concretesample.details

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concretesample.R
import javax.inject.Inject
import javax.inject.Named

class DetailsListItemView(context: Context) : LinearLayout(context) {
    @Inject @field:Named("detailsTitle") lateinit var detailsTitle: String

    @BindView(R.id.tv_position) lateinit var positionTextView: TextView
    @BindView(R.id.tv_title) lateinit var titleTextView: TextView

    init {
        Concrete.getComponent<DetailComponent>(context).inject(this)
        LayoutInflater.from(context).inflate(R.layout.details_list_item, this, true)
        ButterKnife.bind(this)
        titleTextView.text = detailsTitle
    }

    fun setPosition(position: Int) {
        positionTextView.text = position.toString()
    }
}
