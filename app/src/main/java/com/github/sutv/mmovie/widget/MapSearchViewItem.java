package com.github.sutv.mmovie.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.github.sutv.mmovie.R;
import com.github.sutv.mmovie.databinding.ViewMapSearchItemBinding;
import com.github.sutv.mmovie.model.PlaceMap;
import com.github.sutv.mmovie.util.LocaleUtil;

public class MapSearchViewItem extends FrameLayout {

    private ViewMapSearchItemBinding binding;

    public MapSearchViewItem(Context context) {
        this(context, null);
    }

    public MapSearchViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapSearchViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_map_search_item, this, true);
    }

    public void bindData(@NonNull PlaceMap placeMap, @NonNull OnClickListener listener) {
        binding.imgMarker.setImageResource(placeMap.markerRes);
        binding.txtName.setText(LocaleUtil.getRtlConsideredText(getContext().getString(placeMap.nameRes)));
        binding.txtBuilding.setText(LocaleUtil.getRtlConsideredText(getContext().getString(placeMap.buildingNameRes)));
        binding.rootView.setOnClickListener(listener);
    }

}
