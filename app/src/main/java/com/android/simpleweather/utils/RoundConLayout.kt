package com.android.simpleweather.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import com.android.simpleweather.R
import java.lang.Integer.min

class RoundConLayout(context: Context,attrs:AttributeSet):FrameLayout(context, attrs) {
    private var mPaint:Paint?=null
    private var mPath: Path?=null
    private var mRadius=0F
    private var mLTRadius=0F
    private var mLBRadius=0F
    private var mRTRadius=0F
    private var mRBRadius=0F
    init {
        mPaint=Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color=Color.RED
        mPaint?.strokeWidth=10F
        mPath=Path()
        val typedArray=context.obtainStyledAttributes(attrs, R.styleable.RoundConLayout)
        mRadius=typedArray.getDimensionPixelSize(R.styleable.RoundConLayout_radius,0).toFloat()
        mLTRadius=typedArray.getDimensionPixelSize(R.styleable.RoundConLayout_left_top_radius,0).toFloat()
        mLBRadius=typedArray.getDimensionPixelSize(R.styleable.RoundConLayout_left_bottom_radius,0).toFloat()
        mRTRadius=typedArray.getDimensionPixelSize(R.styleable.RoundConLayout_right_top_radius,0).toFloat()
        mRBRadius=typedArray.getDimensionPixelSize(R.styleable.RoundConLayout_right_bottom_radius,0).toFloat()
        typedArray.recycle()
    }


    override fun dispatchDraw(canvas: Canvas?) {
        initPath()
        canvas?.clipPath(mPath!!)
        super.dispatchDraw(canvas)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    protected fun initPath(){
        val minLength=min(width,height)/2
        val radiusFlag=mRadius>minLength
        //Log.d("initPath","$minLength")
        if(mLTRadius!=0F) {
            mLTRadius=if(mLTRadius>minLength) minLength.toFloat() else mLTRadius
        } else{
            mLTRadius=if(radiusFlag) minLength.toFloat() else mRadius
        }
        if(mLBRadius!=0F) {
            mLBRadius=if(mLBRadius>minLength) minLength.toFloat() else mLBRadius
        } else{
            mLBRadius=if(radiusFlag) minLength.toFloat() else mRadius
        }
        if(mRTRadius!=0F) {
            mRTRadius=if(mRTRadius>minLength) minLength.toFloat() else mRTRadius
        } else{
            mRTRadius=if(radiusFlag) minLength.toFloat() else mRadius
        }
        if(mRBRadius!=0F) {
            mRBRadius=if(mRBRadius>minLength) minLength.toFloat() else mRBRadius
        } else{
            mRBRadius=if(radiusFlag) minLength.toFloat() else mRadius
        }
        //println("$mLTRadius,$mLBRadius,$mRTRadius,$mRBRadius")
        //Log.d("initPath","$minLength,$mRTRadius")
        if(mPaint!=null&&width!=0&&height!=0){
            //Log.d("initPath","$minLength,$mRTRadius")
            val pathTopLength=width-mLTRadius-mRTRadius
            val pathBottomLength=width-mLBRadius-mRBRadius
            val pathLeftLength=height-mLTRadius-mLBRadius
            val pathRightLength=height-mRTRadius-mRBRadius
            mPath?.setLastPoint(mLTRadius,0F)
            mPath?.lineTo(mLTRadius+pathTopLength,0F)
            mPath?.quadTo(width.toFloat(),0F,width.toFloat(),mRTRadius)
            mPath?.lineTo(width.toFloat(),mRTRadius+pathRightLength)
            mPath?.quadTo(width.toFloat(),height.toFloat(),mLBRadius+pathBottomLength,height.toFloat())
            mPath?.lineTo(mLBRadius,height.toFloat())
            mPath?.quadTo(0F,height.toFloat(),0F,mLTRadius+pathLeftLength)
            mPath?.lineTo(0F,mLTRadius)
            mPath?.quadTo(0F,0F,mLTRadius,0F)
        }
    }
}