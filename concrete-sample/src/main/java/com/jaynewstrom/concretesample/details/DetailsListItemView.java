package com.jaynewstrom.concretesample.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaynewstrom.concrete.Concrete;
import com.jaynewstrom.concretesample.R;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;

final class DetailsListItemView extends LinearLayout {

    @Inject @Named("detailsTitle") String detailsTitle;

    @Bind(R.id.tv_position) TextView positionTextView;
    @Bind(R.id.tv_title) TextView titleTextView;

    DetailsListItemView(Context context) {
        super(context);
        Concrete.inject(context, this);
        LayoutInflater.from(context).inflate(R.layout.details_list_item, this, true);
        ButterKnife.bind(this);
        titleTextView.setText(detailsTitle);
    }

    void setPosition(int position) {
        positionTextView.setText(Integer.toString(position));
    }
}
