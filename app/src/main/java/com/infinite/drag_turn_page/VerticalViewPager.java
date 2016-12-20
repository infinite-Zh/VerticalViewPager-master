package com.infinite.drag_turn_page;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inf on 2016/12/16.
 */

public class VerticalViewPager extends ViewGroup {
    private static final String TAG = "VerticalViewPager";
    private Scroller mScroller;
    private GestureDetector mDetector;

    private int mMaxFlingVelocity,mMinFlingVelocity;

    private int mCurrentPage;

    /**
     * 子view底部正好显示时的getScrollY
     */
    private List<Integer> childBottomBoarders=new ArrayList<>();
    /**
     * 子view顶部正好显示时的getScrollY
     */
    private List<Integer> childTopBoarders=new ArrayList<>();

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mDetector=new GestureDetector(getContext(),gesttureListener);
        mMaxFlingVelocity= ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mMinFlingVelocity= ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalHeight = 0;
        MarginLayoutParams lp;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            lp = (MarginLayoutParams) child.getLayoutParams();
            int lelt = lp.leftMargin;
            int top = lp.topMargin + totalHeight;
            int right = l + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();
            child.layout(lelt, top, right, bottom);
            Log.e(TAG,"top="+t);
            childTopBoarders.add(totalHeight);
            totalHeight += lp.topMargin + lp.bottomMargin + child.getMeasuredHeight();
            childBottomBoarders.add(totalHeight);
        }
        Log.e(TAG, "onLayout");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int totalHeight = 0;
        MarginLayoutParams lp;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            measureChild(child,widthMeasureSpec,MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.UNSPECIFIED));
            lp = (MarginLayoutParams) child.getLayoutParams();
            Log.e(TAG,"child 高度"+child.getMeasuredHeight()+"");
//            if (child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin < mContentHeight) {
//                measureChild(child,
//                             widthMeasureSpec,
//                             MeasureSpec.makeMeasureSpec(mContentHeight - lp.topMargin - lp.bottomMargin,
//                                                         MeasureSpec.EXACTLY));
//            }
            totalHeight += lp.topMargin + lp.bottomMargin + child.getMeasuredHeight();
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = totalHeight;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) getScreenWidth();
        }
        setMeasuredDimension(widthSize, heightSize);
        Log.e(TAG, "total height"+totalHeight);
    }

    private int mLastY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mLastY= (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int currentY= (int) ev.getY();
//                scrollBy(0,mLastY-currentY);
//                Log.e(TAG,currentY-mLastY+"");
//                mLastY=currentY;
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }

        return mDetector.onTouchEvent(ev);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private float getScreenWidth() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float widht = display.getWidth();
        float height = display.getHeight();
        return widht;
    }

    private int mContentHeight;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "w=" + w + " h=" + h + " oldw=" + oldw + " oldh=" + oldh);
        mContentHeight = getContentHeight();
//        requestLayout();

    }

    private GestureDetector.OnGestureListener gesttureListener=new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {

            return true;
        }


        @Override
        public void onShowPress(MotionEvent e) {
            Log.e(TAG,"onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e(TAG,"onSingleTapUp");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (getScrollY()<=0&&distanceY<0){
                scrollTo(0,0);
                return true;
            }
            if (getScrollY()>=childBottomBoarders.get(mCurrentPage)&&distanceY>0){
                scrollTo(0,childBottomBoarders.get(mCurrentPage));
                return true;
            }
            scrollBy(0, (int) distanceY);

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e(TAG,"onFling");
            if (getScrollY()<=0&&velocityY>0){
                scrollTo(0,0);
                return true;
            }
            int top=childTopBoarders.get(mCurrentPage);
            int bottom=childBottomBoarders.get(mCurrentPage);

            int maxScrollDistanceAvailable=bottom-getScrollY()-mContentHeight;
            if (Math.abs(velocityY)>=mMinFlingVelocity&&Math.abs(velocityY)<=mMaxFlingVelocity){
                //手指向上划，内容向上滚动
                if (velocityY<0){
                    //已经滑动到当前页的底部

                    if (getScrollY()+mContentHeight>=bottom){
                        turnPageDown();
                        return true;
                    }
                    Log.e(TAG,getScrollY()+"");
                    int dyUp=mContentHeight;
                    if (dyUp>maxScrollDistanceAvailable){
                        dyUp=maxScrollDistanceAvailable;
                    }
                    mScroller.startScroll(getScrollX(),getScrollY(),0,dyUp);

                }else {//手指向下划
                    //如果当前位置在当前页顶部，则向上翻页
                    if(mCurrentPage!=0&&getScrollY()<=top){
                        turnPageUp();
                        return true;
                    }
                    int dyDown=mContentHeight;
                    if (getScrollY()-top<=mContentHeight){
                        dyDown=getScrollY()-top;
                    }
                    mScroller.startScroll(getScrollX(),getScrollY(),0,-dyDown);

                }

                Log.e(TAG,"getscrooly="+getScrollY()+"  bottom="+bottom);
            }
            invalidate();
            return true;
        }




    };

    /**
     * 向下翻页
     */
    private void turnPageDown(){
        Log.e(TAG,"turn page down");
        //滑动到最后一页底部
        if (mCurrentPage==getChildCount()-1){
            //回弹
            mScroller.startScroll(getScrollX(),getScrollY(),0,childBottomBoarders.get(mCurrentPage)-getScrollY()-mContentHeight);
            invalidate();
            Log.e(TAG,"回弹");
            return;
        }
        mCurrentPage++;
        int bottom=childBottomBoarders.get(mCurrentPage);
        mScroller.startScroll(getScrollX(),getScrollY(),0,childTopBoarders.get(mCurrentPage)-getScrollY());
        invalidate();
    }

    /**
     * 向上翻页
     */
    private void turnPageUp(){
        mCurrentPage--;
        View targetView=getChildAt(mCurrentPage);
        if (targetView!=null){
            Rect rect=new Rect();
            targetView.getGlobalVisibleRect(rect);
            int visibleTop=rect.top;
            int visibleBottom=rect.bottom;
            //目标view可见高度，滑动时要减去
            int visibleHeight=visibleBottom- visibleTop;
            Log.e(TAG,"visible="+visibleTop+","+rect.bottom);
            mScroller.startScroll(getScrollX(),getScrollY(),0,-mContentHeight+visibleHeight);
            invalidate();
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            int curY=mScroller.getCurrY();
            float v=mScroller.getCurrVelocity();
//            Log.e(TAG,curY+"");
            scrollTo(mScroller.getCurrX(),curY);
            invalidate();
        }
    }

    private int getContentHeight(){
        int contentTop = ((Activity)getContext()).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int contentBottom = ((Activity)getContext()).getWindow().findViewById(Window.ID_ANDROID_CONTENT).getBottom();
        return contentBottom-contentTop;
    }
}
