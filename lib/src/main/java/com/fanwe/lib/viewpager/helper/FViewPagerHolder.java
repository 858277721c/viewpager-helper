package com.fanwe.lib.viewpager.helper;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import java.lang.ref.WeakReference;

/**
 * Created by zhengjun on 2018/3/2.
 */
public abstract class FViewPagerHolder
{
    private WeakReference<ViewPager> mViewPager;

    public final ViewPager getViewPager()
    {
        return mViewPager == null ? null : mViewPager.get();
    }

    public final void setViewPager(ViewPager viewPager)
    {
        final ViewPager old = getViewPager();
        if (old != viewPager)
        {
            if (old != null)
            {
                onRelease(old);
            }

            if (viewPager != null)
            {
                mViewPager = new WeakReference<>(viewPager);
                onInit(viewPager);
            } else
            {
                mViewPager = null;
            }
        }
    }

    /**
     * 返回Adapter
     *
     * @return
     */
    protected final PagerAdapter getAdapter()
    {
        final ViewPager viewPager = getViewPager();
        return viewPager == null ? null : viewPager.getAdapter();
    }

    /**
     * 返回Adapter的数据量
     *
     * @return
     */
    protected final int getAdapterCount()
    {
        final PagerAdapter adapter = getAdapter();
        return adapter == null ? 0 : adapter.getCount();
    }

    /**
     * 位置是否合法
     *
     * @param index
     * @return
     */
    protected final boolean isIndexLegal(int index)
    {
        if (index < 0 || index >= getAdapterCount())
        {
            return false;
        } else
        {
            return true;
        }
    }

    protected abstract void onInit(ViewPager viewPager);

    protected abstract void onRelease(ViewPager viewPager);
}
