package com.mobiverse.MobiVerse;

import java.util.List;

public interface PortfolioLoadCallback {
    void onLoaded(List<StockPosition> positions, double initialValue);
    void onError(Exception e);
}
