package com.popular.movies.details;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.popular.movies.details.reviews.ReviewsFragment;
import com.popular.movies.details.trailers.TrailersFragment;

public class MovieDetailsFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final String[] mSectionTabTitles;
    private final SparseArray<Fragment> mFragments;

    MovieDetailsFragmentPagerAdapter(@NonNull final FragmentManager fragmentManager, @NonNull final String... sectionTabTitles) {
        super(fragmentManager);
        mSectionTabTitles = sectionTabTitles;
        mFragments = new SparseArray<>();
    }

    @Override
    public Fragment getItem(final int position) {
        final Fragment fragment;
        if (position == 0) {
            fragment = MovieDetailsFragment.newInstance();
        } else if (position == 1) {
            fragment = TrailersFragment.newInstance();
        }
        else if (position == 2) {
            fragment = ReviewsFragment.newInstance();
        } else {
            throw new IllegalArgumentException("The position" + position + " is greater than 2");
        }
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        super.destroyItem(container, position, object);
        mFragments.remove(position);
    }

    SparseArray<Fragment> getFragments(){
        return mFragments;
    }

    @Override
    public int getCount() {
        return mSectionTabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mSectionTabTitles[position];
    }
}
