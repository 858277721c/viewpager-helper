/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.viewpager.helper;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

/**
 * ViewPager轮播类
 */
public class FPagerPlayer extends FViewPagerHolder
{
    /**
     * 默认轮播间隔
     */
    private static final long DEFAULT_PLAY_SPAN = 5 * 1000;

    private long mPlaySpan = DEFAULT_PLAY_SPAN;
    private boolean mIsNeedPlay = false;
    private boolean mIsPlaying = false;
    private CountDownTimer mTimer;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onViewPagerChanged(ViewPager newPager, ViewPager oldPager)
    {
        if (newPager != null)
            newPager.setOnTouchListener(mInternalOnTouchListener);
    }

    private final View.OnTouchListener mInternalOnTouchListener = new View.OnTouchListener()
    {
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            if (v == getViewPager())
                processTouchEvent(event);

            return false;
        }
    };

    /**
     * 是否正在轮播中
     *
     * @return
     */
    public boolean isPlaying()
    {
        return mIsPlaying;
    }

    /**
     * 是否可以轮播
     *
     * @return
     */
    protected boolean canPlay()
    {
        if (getAdapterCount() <= 1)
        {
            stopPlay();
            return false;
        }
        return true;
    }

    /**
     * 开始轮播
     */
    public void startPlay()
    {
        startPlay(DEFAULT_PLAY_SPAN);
    }

    /**
     * 开始轮播
     *
     * @param playSpan 轮播间隔(毫秒)
     */
    public void startPlay(long playSpan)
    {
        if (!canPlay())
            return;

        if (playSpan < 0)
            playSpan = DEFAULT_PLAY_SPAN;

        mPlaySpan = playSpan;
        mIsNeedPlay = true;
        startPlayInternal();
    }

    private void startPlayInternal()
    {
        if (mIsPlaying)
            return;

        if (!mIsNeedPlay)
            return;

        if (!canPlay())
            return;

        if (mTimer == null)
        {
            mTimer = new CountDownTimer(Long.MAX_VALUE, mPlaySpan)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    onChangePage();
                }

                @Override
                public void onFinish()
                {
                }
            };

            mHandler.removeCallbacks(mStartTimerRunnable);
            mHandler.postDelayed(mStartTimerRunnable, mPlaySpan);
            mIsPlaying = true;
        }
    }

    protected void onChangePage()
    {
        if (canPlay())
        {
            int current = getViewPager().getCurrentItem();
            current++;
            if (current >= getAdapterCount())
                current = 0;

            getViewPager().setCurrentItem(current, true);
        }
    }

    private final Runnable mStartTimerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (mTimer != null)
                mTimer.start();
        }
    };

    /**
     * 停止轮播
     */
    public void stopPlay()
    {
        stopPlayInternal();
        mIsNeedPlay = false;
    }

    private void stopPlayInternal()
    {
        if (mHandler != null)
            mHandler.removeCallbacks(mStartTimerRunnable);

        if (mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
            mIsPlaying = false;
        }
    }

    /**
     * 调用此方法处理触摸事件，会根据事件暂停和恢复播放
     *
     * @param event
     */
    public void processTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                stopPlayInternal();
                break;
            case MotionEvent.ACTION_UP:
                startPlayInternal();
                break;
        }
    }
}
