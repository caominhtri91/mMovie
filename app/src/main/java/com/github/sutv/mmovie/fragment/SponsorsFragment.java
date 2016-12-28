package com.github.sutv.mmovie.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import javax.inject.Inject;

import com.github.sutv.mmovie.R;
import com.github.sutv.mmovie.databinding.FragmentSponsorsBinding;
import com.github.sutv.mmovie.model.Sponsor;
import com.github.sutv.mmovie.util.AnalyticsTracker;
import com.github.sutv.mmovie.util.AppUtil;
import com.github.sutv.mmovie.widget.SponsorImageView;
import rx.Observable;

public class SponsorsFragment extends BaseFragment {

    private FragmentSponsorsBinding binding;

    @Inject
    AnalyticsTracker analyticsTracker;

    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSponsorsBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    private void initView() {
        Observable.from(Sponsor.createPlatinumList())
                .forEach(sponsor -> addView(sponsor, binding.platinumContainer));
        Observable.from(Sponsor.createVideoList())
                .forEach(sponsor -> addView(sponsor, binding.videoContainer));
        Observable.from(Sponsor.createFoodsList())
                .forEach(sponsor -> addView(sponsor, binding.foodsContainer));
        Observable.from(Sponsor.createNormalList())
                .forEach(sponsor -> addView(sponsor, binding.normalContainer));
    }

    private void addView(Sponsor sponsor, FlexboxLayout container) {
        SponsorImageView imageView = new SponsorImageView(getActivity());
        imageView.bindData(sponsor, v -> {
            if (TextUtils.isEmpty(sponsor.url))
                return;
            analyticsTracker.sendEvent("sponsor", sponsor.url);
            AppUtil.showWebPage(getActivity(), sponsor.url);
        });
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) getResources().getDimension(R.dimen.spacing_small);
        params.setMargins(margin, margin, 0, 0);
        container.addView(imageView, params);
    }

}
