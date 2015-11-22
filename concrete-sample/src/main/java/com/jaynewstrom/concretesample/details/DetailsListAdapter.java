package com.jaynewstrom.concretesample.details;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

final class DetailsListAdapter extends BaseAdapter {

    DetailsListAdapter() {
    }

    @Override public int getCount() {
        return 50;
    }

    @Override public Object getItem(int position) {
        return position;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        DetailsListItemView listItemView;
        if (convertView == null) {
            listItemView = new DetailsListItemView(parent.getContext());
        } else {
            listItemView = (DetailsListItemView) convertView;
        }
        listItemView.setPosition(position);
        return listItemView;
    }
}
