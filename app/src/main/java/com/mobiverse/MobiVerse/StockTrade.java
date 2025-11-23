package com.mobiverse.MobiVerse;

import java.util.Date;

public class StockTrade {
    private String userId;
    private String symbol;
    private String side; // "buy" or "sell"
    private int quantity;
    private double price;
    private double fees;
    private Date tradeDate;
    private String note;

    public StockTrade() {}

    public String getUserId() { return userId; }
    public String getSymbol() { return symbol; }
    public String getSide() { return side; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getFees() { return fees; }
    public Date getTradeDate() { return tradeDate; }
    public String getNote() { return note; }
}
