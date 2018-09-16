package com.jaynewstrom.concretesample.details

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.jaynewstrom.concrete.Concrete
import com.jaynewstrom.concretesample.R
import javax.inject.Inject
import javax.inject.Named

class DetailsListItemView(context: Context) : LinearLayout(context) {
    @Inject @field:Named("detailsTitle") lateinit var detailsTitle: String

    private val positionTextView: TextView

    init {
        Concrete.getComponent<DetailComponent>(context).inject(this)
        LayoutInflater.from(context).inflate(R.layout.details_list_item, this, true)

        val titleTextView = findViewById<TextView>(R.id.tv_title)
        titleTextView.text = detailsTitle

        positionTextView = findViewById(R.id.tv_position)
    }

    fun setPosition(position: Int) {
        positionTextView.text = position.toString()
    }
}
