package com.captain.testbarchart;

import android.content.Context;

import java.text.DecimalFormat;

/**
 * @version 1.0.0
 * @autor
 * @single create by 2023年-01月
 */
public class Utils {
    public static String getFormatDoubleString(double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String afterFormat = decimalFormat.format( value);
        return afterFormat;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }



}
