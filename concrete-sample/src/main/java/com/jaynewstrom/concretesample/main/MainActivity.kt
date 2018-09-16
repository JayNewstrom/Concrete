package com.jaynewstrom.concretesample.main

import android.os.Bundle
import android.widget.TextView
import com.jaynewstrom.concretesample.BaseActivity
import com.jaynewstrom.concretesample.R
import com.jaynewstrom.concretesample.application.ApplicationComponent
import com.jaynewstrom.concretesample.details.DetailActivity
import javax.inject.Inject
import javax.inject.Named

class MainActivity : BaseActivity<MainActivityComponent>() {
    @Inject @field:Named("title") internal lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle(title)

        findViewById<TextView>(R.id.first_details_button).let { button ->
            button.setOnClickListener {
                onDetailsButtonClicked(button)
            }
        }

        findViewById<TextView>(R.id.second_details_button).let { button ->
            button.setOnClickListener {
                onDetailsButtonClicked(button)
            }
        }
    }

    override fun concreteBlock(applicationComponent: ApplicationComponent): MainActivityConcreteBlock {
        return MainActivityConcreteBlock(applicationComponent)
    }

    override fun performInject(component: MainActivityComponent) {
        component.inject(this)
    }

    private fun onDetailsButtonClicked(button: TextView) {
        startActivity(DetailActivity.buildIntent(this, button.text.toString()))
    }
}
