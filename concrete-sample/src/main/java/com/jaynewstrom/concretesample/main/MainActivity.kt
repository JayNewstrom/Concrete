package com.jaynewstrom.concretesample.main

import android.os.Bundle
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
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
        ButterKnife.bind(this)
        setTitle(title)
    }

    override fun concreteBlock(applicationComponent: ApplicationComponent): MainActivityConcreteBlock {
        return MainActivityConcreteBlock(applicationComponent)
    }

    override fun performInject(component: MainActivityComponent) {
        component.inject(this)
    }

    @OnClick(R.id.first_details_button, R.id.second_details_button) fun onDetailsButtonClicked(button: TextView) {
        startActivity(DetailActivity.buildIntent(this, button.text.toString()))
    }
}
