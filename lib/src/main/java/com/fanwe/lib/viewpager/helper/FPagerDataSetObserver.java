package com.fanwe.lib.viewpager.helper;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * PagerAdapter数据集变化监听
 */
public abstract class FPagerDataSetObserver extends FViewPagerHolder
{
    @Override
    protected void onInit(ViewPager viewPager)
    {
        viewPager.addOnAdapterChangeListener(mOnAdapterChangeListenerInternal);

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null)
        {
            adapter.registerDataSetObserver(mDataSetObserverInternal);
        }
    }

    @Override
    protected void onRelease(ViewPager viewPager)
    {
        viewPager.removeOnAdapterChangeListener(mOnAdapterChangeListenerInternal);

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter != null)
        {
            adapter.unregisterDataSetObserver(mDataSetObserverInternal);
        }
    }

    private ViewPager.OnAdapterChangeListener mOnAdapterChangeListenerInternal = new ViewPager.OnAdapterChangeListener()
    {
        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter)
        {
            if (oldAdapter != null)
            {
                oldAdapter.unregisterDataSetObserver(mDataSetObserverInternal);
            }
            if (newAdapter != null)
            {
                newAdapter.registerDataSetObserver(mDataSetObserverInternal);
            }

            mDataSetObserverInternal.onChanged(); //Adapter变化，手动通知一次
        }
    };

    private DataSetObserver mDataSetObserverInternal = new DataSetObserver()
    {
        @Override
        public void onChanged()
        {
            FPagerDataSetObserver.this.onChanged();
        }

        @Override
        public void onInvalidated()
        {
            FPagerDataSetObserver.this.onInvalidated();
        }
    };

    protected abstract void onChanged();

    protected void onInvalidated()
    {
    }
}
