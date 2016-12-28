package com.github.sutv.mmovie.di;

import com.github.sutv.mmovie.di.scope.FragmentScope;
import com.github.sutv.mmovie.fragment.AboutFragment;
import com.github.sutv.mmovie.fragment.SessionDetailFragment;
import com.github.sutv.mmovie.fragment.SessionsFragment;
import com.github.sutv.mmovie.fragment.SessionsTabFragment;
import com.github.sutv.mmovie.fragment.SettingsFragment;
import com.github.sutv.mmovie.fragment.SponsorsFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(SettingsFragment fragment);

    void inject(AboutFragment fragment);

    void inject(SessionsTabFragment fragment);

    void inject(SessionsFragment fragment);

    void inject(SessionDetailFragment fragment);

    void inject(SponsorsFragment fragment);
}
