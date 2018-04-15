package com.coordinate.base.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.coordinate.R;
import java.util.HashMap;

/**
 * Created by LY on 2018/4/6.
 */

public class CoordinateView extends View {

    private int emptyZise = 40 ;

    private HashMap<Integer ,Integer> monthDays = new HashMap<>();
    private int [] mPortrait ;
    private int[] mLEPortrait;
    private int[] mWKPortrait;

    private Paint mZBPaint ; //  坐标轴的画笔
    private Paint mLeavePaint ; // 请假的画笔
    private Paint mWorkPaint ; // 加班的画笔
    private Paint mTextPaint ; // 文字的画笔
    private int bkColor , zbColor , leColor , wkColor , textColor ;
    private float textSize ;
    private Rect textTRect = new Rect();
    private Rect textPRect = new Rect();

    private Path tpath = new Path() , ppath = new Path();

    private int mWidth , mHeight ;

    private float mTStartx , mTStarty , mPStartx , mPStarty ;

    private int defaultHeightNumber = 5 ;
    private float defaultOneHeight = 20.0f ; // 一格的高度
    private float defaultOneWidht = 20.0f ;  // 一格的宽度
    private int mMonth = 1 ; // 当前月份
    private int mMaxNuber ;

    public CoordinateView(Context context) {
        this(context ,null);
    }

    public CoordinateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    @SuppressLint("ResourceAsColor")
    public CoordinateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs , R.styleable.CoordinateView);
        bkColor = ta.getColor(R.styleable.CoordinateView_zbbackground , R.color.zbbackground);
        zbColor = ta.getColor(R.styleable.CoordinateView_zb_color , R.color.zbcolor);
        leColor = ta.getColor(R.styleable.CoordinateView_le_color , R.color.lecolor);
        wkColor = ta.getColor(R.styleable.CoordinateView_wk_color , R.color.wkcolor);
        textSize = ta.getDimension(R.styleable.CoordinateView_text_size , 5);
        textColor = ta.getColor(R.styleable.CoordinateView_text_color , R.color.zbcolor);
        ta.recycle(); // 一定不能少
        initPain() ;
    }

    private void initPain(){
        mZBPaint = new Paint(); // 坐标
        mZBPaint.setColor(zbColor);
        mZBPaint.setStyle(Paint.Style.FILL);
        mZBPaint.setStrokeWidth(5.0f);
        mLeavePaint = new Paint(); // 秦请假
        mLeavePaint.setColor(leColor);
        mLeavePaint.setStyle(Paint.Style.FILL);
        mLeavePaint.setStrokeWidth(5.0f);
        mWorkPaint = new Paint();  // 加班
        mWorkPaint.setColor(wkColor);
        mWorkPaint.setStyle(Paint.Style.FILL);
        mWorkPaint.setStrokeWidth(5.0f); // 文字
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.getTextBounds("1日" , 0 ,2 , textTRect); // 获取文字边框
        mTextPaint.getTextBounds("1" , 0 ,1 , textPRect);

        monthDays.put(1,31);
        monthDays.put(2,28);
        monthDays.put(3,31);
        monthDays.put(4,30);
        monthDays.put(5,31);
        monthDays.put(6,30);
        monthDays.put(7,31);
        monthDays.put(8,31);
        monthDays.put(9,30);
        monthDays.put(10,31);
        monthDays.put(11,30);
        monthDays.put(12,31);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getSize(widthMeasureSpec);
        int widhtSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.EXACTLY){
            mWidth = widhtSize ;
        }else{
            mWidth = widhtSize ; //没有制定宽度时让其我父View 的宽度
        }

        if(heightMode == MeasureSpec.EXACTLY){
            mHeight = heightSize ;
        }else{
            mHeight = (int) (defaultOneHeight * defaultHeightNumber); // 没有指定高度时
        }
        Log.d("CoordinateView" , "onMeasure()   mWidth = "+ mWidth +"   mHeight = "+ mHeight);
        setMeasuredDimension(mWidth , mHeight);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        defaultOneWidht = (w - getPaddingLeft() - getPaddingRight()-textPRect.width()-emptyZise)*1.0f / monthDays.get(mMonth); // 得到单元横格的宽度+1 可能会好看一点
        defaultOneHeight = h*1.0f / ( mMaxNuber +1 ) ;         // 得到单元纵格的高度 +1 可能会好看一点
        Log.d("CoordinateView" , "onSizeChanged  text   defaultOneWidht = " + defaultOneWidht  + "   defaultOneHeight = "+ defaultOneHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);
        drawLeaveLine(canvas);
//        drawWorkLine(canvas);
    }

    /**
     * 绘制坐标
     */
    private void drawCoordinate(Canvas canvas){
        canvas.drawColor(bkColor); // 绘制背景
        float tstartX = getPaddingLeft() + textPRect.width() + mZBPaint.getStrokeWidth() + emptyZise ;
        float tstartY = mHeight - ( getPaddingBottom() + textTRect.width() + mZBPaint.getStrokeWidth() + emptyZise ) ;
        float tendX = mWidth - getPaddingRight()  ;
        float tendY = tstartY ;
        canvas.drawLine( tstartX , tstartY , tendX - 10 ,tendY , mZBPaint );

        tpath.moveTo(tendX ,tendY);
        tpath.lineTo(tendX-25,tendY-12);
        tpath.lineTo(tendX-25 , tendY + 12);
        tpath.close();
        canvas.drawPath(tpath,mZBPaint);

        mTStartx = tstartX ;

        int j = 1 ;
        for(int i = 0 ; i < monthDays.get(mMonth) ; i++){
            Log.d("CoordinateView" , "text   tstartx = " + (tstartX+(defaultOneWidht*i)) );
            if(i<9){
                canvas.drawText( "0"+(j++) , tstartX+(defaultOneWidht*i) ,tendY + mZBPaint.getStrokeWidth()+emptyZise/2 , mTextPaint);
            }else{
                canvas.drawText( ""+(j++) , tstartX+(defaultOneWidht*i) ,tendY + mZBPaint.getStrokeWidth()+emptyZise/2 , mTextPaint);
            }
            canvas.drawText( "日" , tstartX+(defaultOneWidht*i) ,tendY + mZBPaint.getStrokeWidth()+emptyZise/2 + 10 + textTRect.height()  , mTextPaint);
        }

        float pstartX = tstartX ;
        float pstartY = tstartY ;
        float pendX = pstartX ;
        float pendY = getPaddingTop() ;
        canvas.drawLine(pstartX ,pstartY ,pendX ,pendY + 10 , mZBPaint);

        ppath.moveTo(pendX ,pendY);
        ppath.lineTo(pendX-15,pendY+15);
        ppath.lineTo(pendX+15 , pendY + 15);
        ppath.close();
        canvas.drawPath(ppath,mZBPaint);

        mPStarty = pstartY ;

        int k = 0 ;
        for(int i = 0 ; i <= mMaxNuber ; i++){
            Log.d("CoordinateView" , "text   pstartx = " + (pstartY+(defaultOneHeight*i)) );
            canvas.drawText( ""+(k++) , getPaddingLeft() + emptyZise/2 ,pstartY-(defaultOneHeight*i) , mTextPaint);
        }
    }

    /**
     * 绘制请假的连线
     */
    private void drawLeaveLine(Canvas canvas){
        float[] leXY = new float[(monthDays.get(mMonth)-1)*4];
        for(int i = 0 ; i < monthDays.get(mMonth) ; i++){
            if(i==0){
                leXY[i*2] = mTStartx + defaultOneWidht*i;
                leXY[i*2+1] = mPStarty - defaultOneHeight*mLEPortrait[i];
            }else if(i == monthDays.get(mMonth)-1){
                leXY[(monthDays.get(mMonth)-1)*4-2] =  mTStartx + defaultOneWidht*i;
                leXY[(monthDays.get(mMonth)-1)*4-1] =  mPStarty - defaultOneHeight*mLEPortrait[i] ;
            }else{
                leXY[i*4-2] = mTStartx + defaultOneWidht*i ;
                leXY[i*4-1] = mPStarty - defaultOneHeight*mLEPortrait[i] ;
                leXY[i*4]   = leXY[i*4-2] ;
                leXY[i*4+1] = leXY[i*4-1] ;
            }
        }
        canvas.drawLines(leXY,mLeavePaint);
    }

    /**
     * 绘制加班的连线
     */
    private void drawWorkLine(Canvas canvas){
        float[] wkXY = new float[(monthDays.get(mMonth)-1)*4];
        for(int i = 0 ; i < monthDays.get(mMonth) ; i++){
            if(i==0){
                wkXY[i*2] = mTStartx + defaultOneWidht*i;
                wkXY[i*2+1] = mPStarty - defaultOneHeight*mWKPortrait[i];
            }else if(i == monthDays.get(mMonth)-1){
                wkXY[(monthDays.get(mMonth)-1)*4-2] = mTStartx + defaultOneWidht*i;
                wkXY[(monthDays.get(mMonth)-1)*4-1] = mPStarty - defaultOneHeight*mWKPortrait[i] ;
            }else{
                wkXY[i*4-2] = mTStartx + defaultOneWidht*i ;
                wkXY[i*4-1] = mPStarty - defaultOneHeight*mWKPortrait[i] ;
                wkXY[i*4]   = wkXY[i*4-2] ;
                wkXY[i*4+1] = wkXY[i*4-1] ;
            }
        }
        canvas.drawLines(wkXY,mWorkPaint);
    }

    /**
     * 设置月份
     */
    public void setMonth( int month , int [] lePortrait , int[] wkPortrait , int maxNuber){
        if( month <= 0 || month > 12 ){
            return ;
        }
        mMonth = month;
        defaultOneWidht = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()-textPRect.width()-emptyZise)*1.0f / monthDays.get(mMonth); // 得到单元横格的宽度+1 可能会好看一点

        this.mLEPortrait = lePortrait ;
        this.mWKPortrait = wkPortrait ;

        if(maxNuber<=0 ){
            mMaxNuber = 5 ;
        }else{
            mMaxNuber = maxNuber ;
        }
        defaultOneHeight = (getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-textTRect.height()-emptyZise)*1.0f / mMaxNuber  ;         // 得到单元纵格的高度 +1 可能会好看一点
        Log.d("CoordinateView" , "text   defaultOneWidht = " + defaultOneWidht  + "   defaultOneHeight = "+ defaultOneHeight);
        invalidate();
    }
}
