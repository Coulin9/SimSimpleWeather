package com.android.simpleweather.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.simpleweather.R

class DotIndicator(context:Context,attrs:AttributeSet?):View(context,attrs) {
    //默认画笔
    private val mPaint=Paint(Paint.ANTI_ALIAS_FLAG)
    //小圆点被选中后的颜色
    private var mSelectedColor: Int
    //小圆点未被选中的颜色
    private var mUnSelectedColor:Int
    //小圆点从选中状态到未选中状态的过渡色
    private var sTUnColor:Int
    //小圆点从未选中状态过渡到选中状态的过渡色
    private var unTSColor:Int
    //小圆点的大小
    private var mDotSize=3
    //小圆点之间的间距
    private var mGapSize=5
    //导航点的总个数
    private var mDotNum=1
    //当前被选中的导航点（从0到dotNum-1）
    private var mCurDot=0
    private var mLastDot=-1
    //画布
    private lateinit var mCanvas: Canvas


    init {
        val typedArray=context.obtainStyledAttributes(attrs, R.styleable.DotIndicator)
        mPaint.strokeWidth=3f
        mSelectedColor=typedArray.getColor(R.styleable.DotIndicator_selected_color,Color.argb(98,9,9,9))
        mUnSelectedColor=typedArray.getColor(R.styleable.DotIndicator_unselected_color,Color.argb(85,124,124,124))
        mDotSize=typedArray.getDimensionPixelSize(R.styleable.DotIndicator_dot_size,3)
        mGapSize=typedArray.getDimensionPixelSize(R.styleable.DotIndicator_gap_size,5)
        typedArray.recycle()
        sTUnColor=mUnSelectedColor
        unTSColor=mSelectedColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //LogUtil.d("WF:DotIndicatorMeasure","${mDotNum},${this}")
        //宽高参数无效，宽高只由导航点的大小决定
        val height=mDotSize
        val width=mDotNum*mDotSize+(mDotNum-1)*mGapSize
        setMeasuredDimension(width,height)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mCanvas=canvas!!
        //初始圆心位置
        var cx=(mDotSize/2).toFloat()
        var cy=(height/2).toFloat()
        val r=(mDotSize/2).toFloat()
        //LogUtil.d("WF:DotIndicatorDraw","${mDotNum},${this}")
        for(i in 0 until mDotNum){
            //如果mCurDot!=mLastDot表示发生了页面切换，那么需要绘制动画，那么就要依据两个过渡色
            //来绘制dot。
            if(mCurDot!=mLastDot){
                if(i==mCurDot){
                    drawDot(canvas,cx,cy,r,unTSColor)
                }else if(i==mLastDot){
                    drawDot(canvas,cx,cy,r,sTUnColor)
                }else{
                    drawDot(canvas,cx,cy,r,mUnSelectedColor)
                }
            }else{
                //否则表示没有发生页面切换，不需要依据过渡色绘制
                drawDot(canvas,cx,cy,r,if(i==mCurDot) mSelectedColor else mUnSelectedColor)
            }
            cx+=(mGapSize+mDotSize)
        }
    }
    private fun drawDot(canvas: Canvas?,cx:Float,cy:Float,radius:Float,color: Int){
        mPaint.color=color
        canvas?.drawCircle(cx, cy, radius, mPaint)
    }

    fun setSelectedDot(index:Int){
        if(index<0||index>=mDotNum) return
        //发生了页面切换
        if(index!=mCurDot){
            mLastDot=mCurDot
            mCurDot=index
            //LogUtil.d("setSelectedDot","$mLastDot,$mCurDot")
            changeAnime()
        }
    }
    //渐变动画，对于这次选中的dot，颜色从未选中变化到选中，对于上次选中的dot，颜色从选中变为未选中，其他点颜色不变。
    //逐渐改变sTUnColor和unTSColor这两个颜色
    private fun changeAnime(){
        val a1=ValueAnimator.ofArgb(mUnSelectedColor,mSelectedColor).apply {
            duration=100
            addUpdateListener { vA->
                unTSColor=vA.animatedValue as Int
                invalidate()
            }
        }
        val a2=ValueAnimator.ofArgb(mSelectedColor,mUnSelectedColor).apply {
            duration=100
            addUpdateListener { vA->
                sTUnColor=vA.animatedValue as Int
                invalidate()
            }
        }
        AnimatorSet().apply {
            play(a1).with(a2)
            start()
        }
    }

    fun setDotNum(num:Int){
        mDotNum=num
    }
}