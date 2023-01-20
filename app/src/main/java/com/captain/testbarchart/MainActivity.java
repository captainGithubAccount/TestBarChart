package com.captain.testbarchart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    CaptainBarChartView cvTest;
    TextView tvHighPrive;
    TextView tvLowPrive;
    TextView tvDate;
    TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cvTest = findViewById(R.id.cvTest);
        tvHighPrive = findViewById(R.id.tvHighPrive);
        tvLowPrive = findViewById(R.id.tvLowPrive);
        tvDate = findViewById(R.id.tvDate);
        tvTip = findViewById(R.id.tvTip);
        cvTest.setDatas(gettheDatas());
        cvTest.setBarClickListener((barModel, index, isClickUseful) -> {
            if(isClickUseful){
                updateTips(barModel, index);
            }else{
                clearTips();
            }

        });
    }

    private void updateTips(BarModel barModel, int index) {
        tvTip.setVisibility(View.GONE);
        tvHighPrive.setText(getString(R.string.highPrice, String.valueOf(barModel.getHighPrice())));
        tvLowPrive.setText(getString(R.string.lowPrice, String.valueOf(barModel.getLowPrice())));
        tvDate.setText(getString(R.string.date, String.valueOf(barModel.getTradeDate())));
    }

    private void clearTips() {
        tvTip.setVisibility(View.VISIBLE);
        tvHighPrive.setText("");
        tvLowPrive.setText("");
        tvDate.setText("");
    }



    private ArrayList<BarModel> gettheDatas(){
        ArrayList<BarModel> datas = new ArrayList<BarModel>();
        for(int i = 0; i < 15; i ++){
            Random random = new Random();

            double avgPrice = getFormatDouble(1 * random.nextDouble());

            double closePrice = getFormatDouble(10000 * random.nextDouble());

            String contractId = "MAIN";
            String dateTimeStamp = "20220325000000000";
            double highPrice = getFormatDouble(10000 * random.nextDouble());

            double lowPrice = getFormatDouble(highPrice - 7000 * random.nextDouble());

            double openPrice = getFormatDouble(10000 * random.nextDouble());
            int position = 1111064;
            Object preKlineEntity = null;

            double settlePrice = getFormatDouble(10000 * random.nextDouble());

            double time = 0.0;
            String totalQty = "1542709";
            String tradeDate = String.valueOf(20220320 + i);
            boolean validity = false;
            String volume = "1542709";
            boolean isDrawTime = false;
            if(i % 3 == 0){
                isDrawTime = true;
            }

            datas.add(new BarModel(avgPrice, closePrice,contractId, dateTimeStamp, highPrice, lowPrice, openPrice, position, null, settlePrice, time, totalQty, tradeDate, validity, volume, isDrawTime));

        }
        return datas;
    }

    private double getFormatDouble(double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String afterFormat = decimalFormat.format( value);
        return Double.parseDouble(afterFormat);
    }
}