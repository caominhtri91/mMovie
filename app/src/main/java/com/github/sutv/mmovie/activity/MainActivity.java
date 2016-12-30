package com.github.sutv.mmovie.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import javax.inject.Inject;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.github.sutv.mmovie.R;
import com.github.sutv.mmovie.adapter.DemoItem;
import com.github.sutv.mmovie.adapter.GridAdapter;
import com.github.sutv.mmovie.databinding.ActivityMainBinding;
import com.github.sutv.mmovie.fragment.SessionsFragment;
import com.github.sutv.mmovie.fragment.SettingsFragment;
import com.github.sutv.mmovie.fragment.StackedPageListener;
import com.github.sutv.mmovie.model.MainContentStateBrokerProvider;
import com.github.sutv.mmovie.model.Page;
import com.github.sutv.mmovie.util.AnalyticsTracker;
import com.github.sutv.mmovie.util.AppUtil;
import com.github.sutv.mmovie.util.LocaleUtil;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private static final String EXTRA_SHOULD_REFRESH = "should_refresh";
    private static final String EXTRA_MENU = "menu";

    private static final long DRAWER_CLOSE_DELAY_MILLS = 300L;

    private AsymmetricGridView movieList;
    AsymmetricGridViewAdapter asymmetricAdapter;

    @Inject
    AnalyticsTracker analyticsTracker;

    @Inject
    MainContentStateBrokerProvider brokerProvider;

    @Inject
    CompositeSubscription subscription;

    private ActivityMainBinding binding;

    static void start(@NonNull Activity activity, boolean shouldRefresh) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_SHOULD_REFRESH, shouldRefresh);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleUtil.initLocale(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleUtil.initLocale(this);

        boolean shouldRefresh = getIntent().getBooleanExtra(EXTRA_SHOULD_REFRESH, false);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        DataBindingUtil.bind(binding.navView.getHeaderView(0));

        getComponent().inject(this);

        subscription.add(brokerProvider.get().observe().subscribe(page -> {
            toggleToolbarElevation(page.shouldToggleToolbar());
            changePage(page.getTitleResId(), page.createFragment());
            binding.navView.setCheckedItem(page.getMenuId());
        }));

        initView();

        if (savedInstanceState == null) {
            if (getIntent().hasCategory(Notification.INTENT_CATEGORY_NOTIFICATION_PREFERENCES)) {
                AppUtil.setTaskDescription(this, getString(R.string.settings), AppUtil.getThemeColorPrimary(this));
                replaceFragment(SettingsFragment.newInstance());
            } else {
                AppUtil.setTaskDescription(this, getString(R.string.all_sessions), AppUtil.getThemeColorPrimary(this));
                replaceFragment(SessionsFragment.newInstance(shouldRefresh));
            }
        } else if (savedInstanceState.getInt(EXTRA_MENU) != 0) {
            Page page = Page.forMenuId(savedInstanceState.getInt(EXTRA_MENU));
            binding.toolbar.setTitle(page.getTitleResId());
            toggleToolbarElevation(page.shouldToggleToolbar());
        }
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        // AsymmetricGridView
        movieList = (AsymmetricGridView) findViewById(R.id.movieList);
        movieList.setRequestedColumnWidth(Utils.dpToPx(this, 120));
        final List<DemoItem> items = new ArrayList<>();

        items.add(new DemoItem(2,2,0,"Example Text 1"));
        items.add(new DemoItem(1,1,0,"Example Text 2"));
        items.add(new DemoItem(1,1,0,"Example Text 3"));
        items.add(new DemoItem(2,2,0,"Example Text 4"));
        items.add(new DemoItem(1,1,0,"Example Text 5"));
        items.add(new DemoItem(1,1,0,"Example Text 6"));
        items.add(new DemoItem(1,1,0,"Example Text 7"));
        items.add(new DemoItem(2,2,0,"Example Text 8"));
        items.add(new DemoItem(1,1,0,"Example Text 9"));
        items.add(new DemoItem(1,1,0,"Example Text 10"));

        // initialize your items array
        GridAdapter adapter = new GridAdapter(this, items);
        AsymmetricGridViewAdapter asymmetricAdapter = new AsymmetricGridViewAdapter<>(this, movieList, adapter);
        movieList.setAdapter(asymmetricAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.content_view);
        if (current != null) {
            outState.putInt(EXTRA_MENU, Page.forName(current).getMenuId());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        subscription.unsubscribe();
    }

    private void initView() {
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawer, binding.toolbar, R.string.open, R.string.close);
        binding.drawer.setDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setCheckedItem(R.id.nav_all_sessions);
    }

    private void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        ft.replace(R.id.content_view, fragment, fragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsTracker.sendScreenView("main");
    }

    @Override
    public void onBackPressed() {
        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
            binding.drawer.closeDrawer(GravityCompat.START);
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        binding.drawer.closeDrawer(GravityCompat.START);

        if (item.getItemId() == R.id.nav_questionnaire) {
            new Handler().postDelayed(() -> {
                AppUtil.showWebPage(this, getString(R.string.about_inquiry_url));
            }, DRAWER_CLOSE_DELAY_MILLS);
        } else {
            Page page = Page.forMenuId(item);
            toggleToolbarElevation(page.shouldToggleToolbar());
            changePage(page.getTitleResId(), page.createFragment());
        }

        return true;
    }

    private void toggleToolbarElevation(boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float elevation = enable ? getResources().getDimension(R.dimen.elevation) : 0;
            binding.toolbar.setElevation(elevation);
        }
    }

    private void changePage(@StringRes int titleRes, @NonNull Fragment fragment) {
        new Handler().postDelayed(() -> {
            binding.toolbar.setTitle(titleRes);
            AppUtil.setTaskDescription(this, getString(titleRes), AppUtil.getThemeColorPrimary(this));
            replaceFragment(fragment);
        }, DRAWER_CLOSE_DELAY_MILLS);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_fade_enter, R.anim.activity_fade_exit);
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment current = fm.findFragmentById(R.id.content_view);
        if (current == null) {
            // no more fragments in the stack. finish.
            finish();
            return;
        }
        Page page = Page.forName(current);
        binding.navView.setCheckedItem(page.getMenuId());
        binding.toolbar.setTitle(page.getTitleResId());
        toggleToolbarElevation(page.shouldToggleToolbar());
        if (current instanceof StackedPageListener) {
            StackedPageListener l = (StackedPageListener) current;
            l.onTop();
        }
    }
}
