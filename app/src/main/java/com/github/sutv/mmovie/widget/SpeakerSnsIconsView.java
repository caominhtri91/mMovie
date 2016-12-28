package com.github.sutv.mmovie.widget;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.github.sutv.mmovie.R;
import com.github.sutv.mmovie.databinding.ViewSpeakerSnsIconsBinding;
import com.github.sutv.mmovie.model.Speaker;
import com.github.sutv.mmovie.util.AppUtil;

public class SpeakerSnsIconsView extends RelativeLayout {

    private ViewSpeakerSnsIconsBinding binding;

    public SpeakerSnsIconsView(Context context) {
        this(context, null);
    }

    public SpeakerSnsIconsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeakerSnsIconsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_speaker_sns_icons, this, true);
    }

    @BindingAdapter("speakerSnsIcons")
    public static void setSpeakerSnsIcons(SpeakerSnsIconsView view, @NonNull Speaker speaker) {
        ViewSpeakerSnsIconsBinding binding = view.binding;

        if (TextUtils.isEmpty(speaker.twitterName) && TextUtils.isEmpty(speaker.githubName)) {
            binding.getRoot().setVisibility(GONE);
            return;
        }

        if (TextUtils.isEmpty(speaker.twitterName)) {
            binding.coverTwitter.setVisibility(GONE);
        } else {
            binding.coverTwitter.setVisibility(VISIBLE);
            binding.coverTwitter.setOnClickListener(v ->
                    AppUtil.showWebPage((Activity) view.getContext(), AppUtil.getTwitterUrl(speaker.twitterName)));
        }

        if (TextUtils.isEmpty(speaker.githubName)) {
            binding.coverGithub.setVisibility(GONE);
        } else {
            binding.coverGithub.setVisibility(VISIBLE);
            binding.coverGithub.setOnClickListener(v ->
                    AppUtil.showWebPage((Activity) view.getContext(), AppUtil.getGitHubUrl(speaker.githubName)));
        }
    }

}
