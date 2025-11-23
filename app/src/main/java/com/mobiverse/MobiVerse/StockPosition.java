package com.mobiverse.MobiVerse;

public class StockPosition {
    private String userId;
    private String symbol;
    private String exchange;
    private int quantity;
    private double avgBuyPrice;
    private double currentPrice;
    private String currency;
    private String sector;
    private boolean isHalal;

    public StockPosition() {}

    public StockPosition(String userId, String symbol) {
        this.userId = userId;
        this.symbol = symbol;
    }

    public String getUserId() { return userId; }
    public String getSymbol() { return symbol; }
    public String getExchange() { return exchange; }
    public int getQuantity() { return quantity; }
    public double getAvgBuyPrice() { return avgBuyPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public String getCurrency() { return currency; }
    public String getSector() { return sector; }
    public boolean isHalal() { return isHalal; }

    public void applyTrade(StockTrade trade) {
        if (trade.getSide().equals("buy")) {
            double totalCost = (quantity * avgBuyPrice) + (trade.getQuantity() * trade.getPrice());
            this.quantity += trade.getQuantity();
            this.avgBuyPrice = totalCost / this.quantity;
        } else { // sell
            this.quantity -= trade.getQuantity();
        }
    }
}
