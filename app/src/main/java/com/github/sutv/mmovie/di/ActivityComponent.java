package com.github.sutv.mmovie.di;

import dagger.Subcomponent;
import com.github.sutv.mmovie.activity.ContributorsActivity;
import com.github.sutv.mmovie.activity.MainActivity;
import com.github.sutv.mmovie.activity.SearchActivity;
import com.github.sutv.mmovie.activity.SearchedSessionsActivity;
import com.github.sutv.mmovie.activity.SessionDetailActivity;
import com.github.sutv.mmovie.activity.SessionFeedbackActivity;
import com.github.sutv.mmovie.di.scope.ActivityScope;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(SessionDetailActivity activity);

    void inject(SearchActivity activity);

    void inject(SearchedSessionsActivity activity);

    void inject(SessionFeedbackActivity activity);

    void inject(ContributorsActivity activity);

    FragmentComponent plus(FragmentModule module);

}
