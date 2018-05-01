package com.jaynewstrom.concretesample.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import com.jaynewstrom.concretesample.BaseActivity
import com.jaynewstrom.concretesample.R
import com.jaynewstrom.concretesample.application.ApplicationComponent

class DetailActivity : BaseActivity<DetailComponent>() {
    @BindView(R.id.list_view) internal lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        ButterKnife.bind(this)
        title = intent.getStringExtra(INTENT_KEY_TITLE)
        listView.adapter = DetailsListAdapter()
    }

    override fun concreteBlock(applicationComponent: ApplicationComponent): DetailConcreteBlock {
        return DetailConcreteBlock(applicationComponent, intent.getStringExtra(INTENT_KEY_TITLE))
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
