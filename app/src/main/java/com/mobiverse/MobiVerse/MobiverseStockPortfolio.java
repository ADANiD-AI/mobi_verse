package com.mobiverse.MobiVerse;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MobiverseStockPortfolio
 * Handles user's stock holdings, real-time valuations, and analytics.
 */
public class MobiverseStockPortfolio {

    private FirebaseFirestore db;
    private String userId;

    private List<StockPosition> positions = new ArrayList<>();
    private Map<String, Double> livePrices = new HashMap<>();
    private ValueChangeListener valueChangeListener;

    public MobiverseStockPortfolio(String userId) {
        this.db = FirebaseFirestore.getInstance();
        this.userId = userId;
    }

    public void setValueChangeListener(ValueChangeListener listener) {
        this.valueChangeListener = listener;
    }

    // 1. Load portfolio (positions + aggregate snapshot)
    public void loadPortfolio(PortfolioLoadCallback callback) {
        db.collection("stock_positions")
          .whereEqualTo("userId", userId)
          .get()
          .addOnSuccessListener(query -> {
              positions.clear();
              for (DocumentSnapshot doc : query) {
                  StockPosition pos = doc.toObject(StockPosition.class);
                  positions.add(pos);
              }
              callback.onLoaded(positions, calculateCurrentValue());
          })
          .addOnFailureListener(callback::onError);
    }

    // 2. Listen for real-time prices
    public void listenToLivePrices() {
        db.collection("stock_live_prices")
          .addSnapshotListener((snap, e) -> {
              if (e != null || snap == null) return;
              for (DocumentSnapshot doc : snap) {
                  String symbol = doc.getString("symbol");
                  Double price = doc.getDouble("price");
                  livePrices.put(symbol, price);
              }
              // Notify listener on price change
              if (valueChangeListener != null) {
                  valueChangeListener.onValueChanged(calculateCurrentValue());
              }
          });
    }

    // 3. Calculate PnL and current value
    public double calculateCurrentValue() {
        double total = 0;
        for (StockPosition pos : positions) {
            double price = livePrices.getOrDefault(pos.getSymbol(), pos.getCurrentPrice());
            total += pos.getQuantity() * price;
        }
        return total;
    }

    // 4. Save new Trade with Transactional update
    public void addTrade(StockTrade trade, TradeCallback callback) {
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentReference posRef = db.collection("stock_positions")
                                         .document(userId + "_" + trade.getSymbol());
            DocumentSnapshot snap = transaction.get(posRef);

            StockPosition pos;
            if (snap.exists()) {
                pos = snap.toObject(StockPosition.class);
            } else {
                pos = new StockPosition(userId, trade.getSymbol());
            }

            pos.applyTrade(trade); // Adjust avgBuyPrice, quantity, etc.

            transaction.set(posRef, pos);
            // Save trade history
            DocumentReference tradeRef = db.collection("stock_trades").document();
            transaction.set(tradeRef, trade);

            return null;
        }).addOnSuccessListener(aVoid -> callback.onSuccess())
          .addOnFailureListener(callback::onError);
    }

    public interface ValueChangeListener {
        void onValueChanged(double newValue);
    }
}
