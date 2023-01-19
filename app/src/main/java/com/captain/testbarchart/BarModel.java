package com.captain.testbarchart;

import androidx.annotation.Nullable;

/**
 * @version 1.0.0
 * @autor lwj
 * @single create by 2023年-01月
 */
public class BarModel{
    private  double avgPrice;
    private  double closePrice;



    public BarModel(double avgPrice, double closePrice, String contractId, String dateTimeStamp, double highPrice, double lowPrice, double openPrice, int position, Object preKlineEntity, double settlePrice, double time, String totalQty, String tradeDate, boolean validity, String volume, boolean isDrawTime) {
        this.avgPrice = avgPrice;
        this.closePrice = closePrice;
        this.contractId = contractId;
        this.dateTimeStamp = dateTimeStamp;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.position = position;
        this.preKlineEntity = preKlineEntity;
        this.totalQty = totalQty;
        this.tradeDate = tradeDate;
        this.validity = validity;
        this.volume = volume;
        this.isDrawTime = isDrawTime;
    }

    public double getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(double avgPrice) {
        this.avgPrice = avgPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getDateTimeStamp() {
        return dateTimeStamp;
    }

    public void setDateTimeStamp(String dateTimeStamp) {
        this.dateTimeStamp = dateTimeStamp;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getPreKlineEntity() {
        return preKlineEntity;
    }

    public void setPreKlineEntity( Object preKlineEntity) {
        this.preKlineEntity = preKlineEntity;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public boolean isDrawTime() {
        return isDrawTime;
    }

    public void setDrawTime(boolean drawTime) {
        isDrawTime = drawTime;
    }

    private  String contractId;
    private  String dateTimeStamp;
    private  double highPrice;
    private  double lowPrice;
    private  double openPrice;
    private  int position;
    private  Object preKlineEntity;
    private  String totalQty;
    private  String tradeDate;
    private  boolean validity;
    private  String volume;

    public boolean isPirceUp() {
        if(openPrice > closePrice){
            return true;
        }else{
            return false;
        }
    }

    private boolean isDrawTime;

}
