package com.github.sutv.mmovie.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.sutv.mmovie.R;

import org.w3c.dom.Text;

import java.util.List;

public class GridAdapter extends ArrayAdapter<DemoItem> {

    private final LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<DemoItem> items) {
        super(context, 0, items);
        layoutInflater = LayoutInflater.from(context);
    }

    public GridAdapter(Context context) {
        super(context, 0);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DemoItem item = getItem(position);
        View v = layoutInflater.inflate(R.layout.item_movie_asymmetric, parent, false);
        TextView textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(item.getText());
        return v;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position%2==0 ? 1 : 0;
    }

    public void appendItems(List<DemoItem> newItems) {
        addAll(newItems);
        notifyDataSetChanged();
    }

    public void setItems(List<DemoItem> moreItems) {
        clear();
        appendItems(moreItems);
    }
}
