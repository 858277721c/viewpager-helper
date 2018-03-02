package com.fanwe.viewpager_helper;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fanwe.lib.viewpager.helper.FPagerPercentChangeListener;
import com.fanwe.lib.viewpager.helper.FPagerSelectChangeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private ViewPager mViewPager;
    private List<String> mListModel = new ArrayList<>();

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinearLayout = findViewById(R.id.ll);
        mViewPager = findViewById(R.id.viewpager);

        mViewPager.setAdapter(mAdapter);

        mPagerSelectChangeListener.setViewPager(mViewPager);
        mPagerPercentChangeListener.setViewPager(mViewPager);

        fillData();
    }

    private void fillData()
    {
        mListModel.clear();
        for (int i = 0; i < 20; i++)
        {
            mListModel.add(String.valueOf(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 滚动百分比监听
     */
    private FPagerPercentChangeListener mPagerPercentChangeListener = new FPagerPercentChangeListener()
    {
        @Override
        public void onShowPercent(int position, float showPercent, boolean isEnter, boolean isMoveLeft)
        {
            if (isEnter)
            {
                Log.i(TAG, position + " (" + showPercent + ") " + isMoveLeft);
            } else
            {
                Log.e(TAG, position + " (" + showPercent + ") " + isMoveLeft);
            }
        }
    };

    /**
     * 选中监听
     */
    private FPagerSelectChangeListener mPagerSelectChangeListener = new FPagerSelectChangeListener()
    {
        @Override
        protected void onPageCountChanged(int count)
        {
            mLinearLayout.removeAllViews();
            for (int i = 0; i < count; i++)
            {
                PagerIndicatorItem item = new PagerIndicatorItem(MainActivity.this);
                item.setText(String.valueOf(i));
                item.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mViewPager.setCurrentItem(mLinearLayout.indexOfChild(v));
                    }
                });

                mLinearLayout.addView(item, new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            Log.i(TAG, "onPageCountChanged:" + count);
        }

        @Override
        protected void onSelectChanged(int index, boolean selected)
        {
            PagerIndicatorItem item = (PagerIndicatorItem) mLinearLayout.getChildAt(index);
            if (item != null)
            {
                item.onSelectChanged(selected);
            }
            Log.i(TAG, "onSelectChanged:" + index + " " + selected);
        }
    };

    private PagerAdapter mAdapter = new PagerAdapter()
    {
        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public int getCount()
        {
            return mListModel.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, final int pageIndex)
        {
            Button button = new Button(MainActivity.this);
            button.setText(String.valueOf(pageIndex));
            container.addView(button);
            return button;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    };
}
