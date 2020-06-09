package com.jaynewstrom.concretesample.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import com.jaynewstrom.concretesample.BaseActivity
import com.jaynewstrom.concretesample.R
import com.jaynewstrom.concretesample.application.ApplicationComponent

class DetailActivity : BaseActivity<DetailComponent>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        title = intent.getStringExtra(INTENT_KEY_TITLE)
        val listView = findViewById<ListView>(R.id.list_view)
        listView.adapter = DetailsListAdapter()
    }

    override fun concreteBlock(applicationComponent: ApplicationComponent): DetailConcreteBlock {
        return DetailConcreteBlock(applicationComponent, intent.getStringExtra(INTENT_KEY_TITLE)!!)
    }

    override fun performInject(component: DetailComponent) {
        component.inject(this)
    }

    companion object {
        private const val INTENT_KEY_TITLE = "intent.title"

        fun buildIntent(context: Context, detailsTitle: String): Intent {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(INTENT_KEY_TITLE, detailsTitle)
            return intent
        }
    }
}
