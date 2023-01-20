package com.captain.testbarchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * @version 1.0.0
 * @autor captain:l
 * @single create by 2023年-01月
 */
public class CaptainBarChartView extends View {

    private int mAttrBgColor;
    private int mAttrRulerColor;
    private int mAttrBarUpColor;
    private int mAttrBarDownColor;
    private int mAttrYRulerCount;
    private int mAttrXRulerCount;

    private int mAttrXTextColor;
    private int mAttrXTextSize;
    private int mAttrYTextColor;
    private int mAttrYTextSize;

    private Paint mPaintRuler;
    private Paint mPaintBarUp;
    private Paint mPaintBarDown;
    private Paint mPaintXText;
    private Paint mPaintYText;

    private int mPaddingBoottomTextToTable = dp2px(30);
    private int mPaddingStartTextToTable = dp2px(22);
    private int mViewStartToTable = dp2px(120);
    private int mViewEndToTable = dp2px(10);
    private int mViewBottomToTable = dp2px(50);

    private int mBarStartToTable = dp2px(5);
    private int mTopDisplaySpace = dp2px(50);
    private int mHorRulerGapSize;
    private int mVerRulerGapSize;
    private float mBarWithGapWidth;

    private float mBarWidth = dp2px(10);
    private int mBarGapWidth = dp2px(4);
    private int mBarNum;
    private int mTableHeight;
    private int mTableWidth;
    double mMax;
    double mMin;


    private ArrayList<BarModel> mDatas = new ArrayList();
//    private ArrayList<String> mTimeDatas = new ArrayList();
    private ArrayList<String> mValueDatas = new ArrayList();

    public void setDatas(ArrayList<BarModel> datas){
        mDatas = datas;
        if(mDatas.size() > 0){
            convertDataToNeed(mDatas);
        }
        postInvalidate();
    }

    public void setBarClickListener(BarClickListener barClickListener){
        mBarClickListener = barClickListener;
    }
    BarClickListener mBarClickListener;
    interface BarClickListener{
        void onClickBar(BarModel barModel, int index, boolean isClickUseful);
    }

    public void convertDataToNeed(ArrayList<BarModel> datas){
        datasToValueDatas(datas);
//        datasTotimeDatas(datas);
    }

    //外部
   /* private void datasTotimeDatas(ArrayList<BarModel> datas) {
        mTimeDatas.clear();
        for(int i = 0; i < datas.size(); i++){
            mTimeDatas.add(datas.get(i).getTradeDate());
        }
    }*/

    private void datasToValueDatas(ArrayList<BarModel> datas) {
        mValueDatas.clear();
        ArrayList<Double> allTimesData = new ArrayList<Double>();
        for(int i = 0; i < datas.size(); i++){
            allTimesData.add(datas.get(i).getHighPrice());
            allTimesData.add(datas.get(i).getLowPrice());
        }
        Double[] values = getNeedValues(allTimesData);
        if(values != null){
            double gap = (values[0] - values[1])/(mAttrYRulerCount-1);
            for(int i = 0; i < mAttrYRulerCount; i++){
                double valueData = values[1] + i * gap;
                String e = Utils.getFormatDoubleString(valueData);
                mValueDatas.add( 0, e);
            }
        }
    }





    private Double[] getNeedValues( ArrayList<Double> datas) {
        Double[] values = new Double[2];
        Iterator<Double> iterator = datas.iterator();
        if(!iterator.hasNext()){
            return null;
        }
        Double max = iterator.next();
        Double min = iterator.next();
        while(iterator.hasNext()) {
            Double e = iterator.next();
            max = getMax(max, e);
            min = getMin(min, e);
        }
        values[0] = max;
        mMax = max;
        values[1] = min;
        mMin = min;
        return values;
    }

    private Double getMin(Double min, Double e) {
        return min < e ? min : e;
    }

    private Double getMax(Double max, Double e) {
        return max > e ? max : e;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float y = event.getY();
                float x = event.getX();
                int index = getClickedBarIndex(mDatas, x, y);
                Log.d("TAG-index", "index: "+index);
                if(index != -1){
                    mBarClickListener.onClickBar(mDatas.get(index), index, index != -1);
                }else{
                    mBarClickListener.onClickBar(null, -1, index != -1);
                }

                break;

            default: break;

        }
        return true;

    }


    private int getClickedBarIndex(ArrayList<BarModel> datas, float onKeyDownX, float onKeyDownY){
        int index = -1;
        boolean isCompareOnKeyDownY = false;
        for(int i = 0; i < datas.size(); i++){
            float left = i * (mBarWidth + mBarGapWidth);
            float leftInView = left + mViewStartToTable + mBarStartToTable + getPaddingLeft();
            float right = left + mBarWidth;
            float rightInView = right + mViewStartToTable + mBarStartToTable + getPaddingLeft();
            if(leftInView< onKeyDownX  && onKeyDownX < rightInView){
                index = i;
                isCompareOnKeyDownY = true;
            }
            if(isCompareOnKeyDownY){
                float currentBarApexPercent = (float) ((mMax - datas.get(i).getHighPrice())/(mMax -mMin));
                float top = currentBarApexPercent * mTableHeight;
                float topHInView = top + mTopDisplaySpace + getPaddingTop();

                float highLowGap = (float) (datas.get(i).getHighPrice() - datas.get(i).getLowPrice());
                float currentBarHeightPercent = (float) (highLowGap/(mMax -mMin));
                float currentBarHeght = currentBarHeightPercent * mTableHeight;
                float bottom = top + currentBarHeght;
                float bottomHInView = bottom + mTopDisplaySpace + getPaddingTop();

                if(onKeyDownY < topHInView || onKeyDownY > bottomHInView){
                    index = -1;
                }
                if(index != -1) return index;
            }
        }
        return index;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mAttrBgColor);
        if(mValueDatas.size() > 0){
            drawTable(canvas);
            drawYAxisText(canvas, mValueDatas);
            drawBarChart(canvas, mDatas);
        }

    }

    private void drawBarChart(Canvas canvas, ArrayList<BarModel> datas) {
        translationFromX_Y(canvas, mViewStartToTable + mBarStartToTable, mTopDisplaySpace, ()->{
            for(int i = 0; i < datas.size(); i++){
                float left = i * (mBarWidth + mBarGapWidth);
                float currentBarApexPercent = (float) ((mMax - datas.get(i).getHighPrice())/(mMax -mMin));
                float top = currentBarApexPercent * mTableHeight;
                float right = left + mBarWidth;
                float highLowGap = (float) (datas.get(i).getHighPrice() - datas.get(i).getLowPrice());
                float currentBarHeightPercent = (float) (highLowGap/(mMax -mMin));
                float currentBarHeght = currentBarHeightPercent * mTableHeight;
                float bottom = top + currentBarHeght;
                if(datas.get(i).isPirceUp()){
                    canvas.drawRect(left, top, right, bottom, mPaintBarUp);
                }else{
                    canvas.drawRect(left, top, right, bottom, mPaintBarDown);
                }
                if(datas.get(i).isDrawTime()){
                    String time = datas.get(i).getTradeDate();
                    String afterCutTime = time.substring(4, 8);
                    float x = (left + mBarWidth/2) - getTextWidth(afterCutTime, mPaintXText)/2;
                    drawXAxisText(canvas, x, mTableHeight + mPaddingBoottomTextToTable, afterCutTime);
                }

            }
        });
    }

    private void drawXAxisText(Canvas canvas, float x, float y, String text) {
        baseDrawText(canvas, mPaintXText, x, y, text);
    }

    private void drawYAxisText(Canvas canvas, ArrayList<String> datas) {

        translationFromX_Y(canvas, mViewStartToTable, mTopDisplaySpace, ()->{
            for(int i = mAttrYRulerCount - 1; i >= 0; i--){
                float x = -(getTextWidth(datas.get(i), mPaintYText)) - mPaddingStartTextToTable;
                baseDrawText(canvas, mPaintYText, x, (float) (i*mHorRulerGapSize), datas.get(i));
            }
        });
    }

    private void drawTable(Canvas canvas){
        translationFromX_Y(canvas, mViewStartToTable, mTopDisplaySpace, ()->{
            for(int i = 0; i < mAttrYRulerCount; i++){
                canvas.drawLine(0f, i*mHorRulerGapSize, mTableWidth, i*mHorRulerGapSize, mPaintRuler);
            }
        });
    }


    private void baseDrawText(Canvas canvas, Paint paint, Float x, Float y, String text){
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float baseLine = y + (fm.bottom - fm.top) / 2 - fm.bottom;
        canvas.drawText(text, x, baseLine, paint);
    }

    private Float getTextWidth(String text, Paint paint){
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return (float)textBounds.width();
    }

    private Float getTextHeight(String text, Paint paint){
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return (float)textBounds.height();
    }




    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - mViewStartToTable - mViewEndToTable;
        mTableHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom()  - mTopDisplaySpace - mViewBottomToTable;
        mHorRulerGapSize = mTableHeight / (mAttrYRulerCount - 1);
    }



    private void translationKtx(Canvas canvas, int x, int y, Block block){
        int checkpoint = canvas.save();
        canvas.translate(x, y);
        try {
            block.withTranslation();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            canvas.restoreToCount(checkpoint);
        }
    }

    private void translationFrom0_0(Canvas canvas, Block block){
        translationKtx(canvas, getPaddingLeft(), getPaddingTop(), block);
    }

    private void translationFromX_Y(Canvas canvas, int x, int y, Block block){
        translationKtx(canvas, x + getPaddingLeft(), y + getPaddingTop(), block);
    }




    private void initPaints() {
        mPaintRuler = new Paint();
        mPaintBarUp = new Paint();
        mPaintBarDown = new Paint();
        mPaintXText = new Paint();
        mPaintYText = new Paint();
        initPaint_Line(mPaintRuler, mAttrRulerColor);
        initPaint_Fill(mPaintBarUp, mAttrBarUpColor);
        initPaint_Fill(mPaintBarDown, mAttrBarDownColor);
        initPaint_Text(mPaintXText, mAttrXTextColor, mAttrXTextSize);
        initPaint_Text(mPaintYText, mAttrYTextColor, mAttrYTextSize);
    }

    private void initPaint_Line(Paint paint, @ColorInt int color){
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float)dp2px(1));
        paint.setAntiAlias(true);
    }
    private void initPaint_Text(Paint paint, @ColorInt int color, float textSize){
        paint.setColor(color);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
    }
    private void initPaint_Fill(Paint paint, @ColorInt int color){
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CaptainBarChartView);
        mAttrBgColor = array.getColor(R.styleable.CaptainBarChartView_bgColor,  ContextCompat.getColor(context, R.color.white));
        mAttrRulerColor = array.getColor(R.styleable.CaptainBarChartView_rulerColor,  ContextCompat.getColor(context, R.color.color_EFEFEF));
        mAttrBarUpColor = array.getColor(R.styleable.CaptainBarChartView_barUpColor,  ContextCompat.getColor(context, R.color.color_00DE00));
        mAttrBarDownColor = array.getColor(R.styleable.CaptainBarChartView_barDownColor,  ContextCompat.getColor(context, R.color.color_FF3434));
        mAttrYRulerCount = array.getInt(R.styleable.CaptainBarChartView_yRulerCount,  6);
        mAttrXRulerCount = array.getInt(R.styleable.CaptainBarChartView_xRulerCount,  5);

        mAttrXTextColor = array.getColor(R.styleable.CaptainBarChartView_xTextColor,  ContextCompat.getColor(context, R.color.color_BBBBBB));
        mAttrXTextSize = array.getColor(R.styleable.CaptainBarChartView_xTextSize,  sp2px(13));
        mAttrYTextColor = array.getColor(R.styleable.CaptainBarChartView_yTextColor,  ContextCompat.getColor(context, R.color.color_BBBBBB));
        mAttrYTextSize = array.getColor(R.styleable.CaptainBarChartView_yTextSize,  sp2px(18));
        array.recycle();
    }


    private int dp2px(int dp){
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private int sp2px(float spValue) {
        float density = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * density + 0.5f);
    }

    interface Block{
        void withTranslation();
    }


    public CaptainBarChartView(Context context) {
        this(context, null);
    }

    public CaptainBarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptainBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaints();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = Utils.getScreenHeight(getContext())/2;
        setMeasuredDimension(width, height);
    }
}
