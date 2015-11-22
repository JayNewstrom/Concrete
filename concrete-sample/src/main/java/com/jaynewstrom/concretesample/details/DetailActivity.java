package com.jaynewstrom.concretesample.details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.jaynewstrom.concrete.ConcreteBlock;
import com.jaynewstrom.concretesample.BaseActivity;
import com.jaynewstrom.concretesample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public final class DetailActivity extends BaseActivity {

    private static final String INTENT_KEY_TITLE = "intent.title";

    public static Intent buildIntent(Context context, String detailsTitle) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(INTENT_KEY_TITLE, detailsTitle);
        return intent;
    }

    @Bind(R.id.list_view) ListView listView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setTitle(getIntent().getStringExtra(INTENT_KEY_TITLE));
        listView.setAdapter(new DetailsListAdapter());
    }

    @Override protected ConcreteBlock getConcreteBlock() {
        return new DetailBlock(getIntent().getStringExtra(INTENT_KEY_TITLE));
    }
}
