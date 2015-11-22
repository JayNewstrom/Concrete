package com.jaynewstrom.concretesample.main;

import android.os.Bundle;
import android.widget.TextView;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concretesample.BaseActivity;
import com.jaynewstrom.concretesample.R;
import com.jaynewstrom.concretesample.details.DetailActivity;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.OnClick;

public final class MainActivity extends BaseActivity {

    @Inject @Named("title") String title;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(title);
    }

    @Override protected ConcreteBlock concreteBlock() {
        return new MainActivityConcreteBlock();
    }

    @OnClick({R.id.first_details_button, R.id.second_details_button}) void onDetailsButtonClicked(TextView button) {
        startActivity(DetailActivity.buildIntent(this, button.getText().toString()));
    }
}
