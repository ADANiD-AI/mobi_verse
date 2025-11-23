package com.mobiverse.MobiVerse;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class AIAgent {

    private MobiverseStockPortfolio stockPortfolio;
    private Context currentContext = Context.GENERAL;

    public enum Context {
        GENERAL,
        STOCK_PORTFOLIO,
        CRYPTO_TRACKING
    }

    public AIAgent(MobiverseStockPortfolio stockPortfolio) {
        this.stockPortfolio = stockPortfolio;
    }

    public void setContext(Context context) {
        this.currentContext = context;
    }

    // This method would be asynchronous in a real app
    public String getFinancialAnalysis(String query) {
        if (currentContext != Context.STOCK_PORTFOLIO) {
            return "Please open your stock portfolio to ask questions about it.";
        }

        // Simplified for demonstration. In a real app, you would use the callback.
        stockPortfolio.loadPortfolio(new PortfolioLoadCallback() {
            @Override
            public void onLoaded(List<StockPosition> positions, double initialValue) {
                // This logic would run inside the callback
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });

        // The following is a simulation of what would happen inside onLoaded
        List<StockPosition> positions = getPositionsFromPortfolioSync(); // Simulated synchronous call
        if (positions.isEmpty()) {
            return "Your portfolio is empty. Add some stocks to get an analysis.";
        }

        if (query.toLowerCase().contains("risk") || query.toLowerCase().contains("halal")) {
            return analyzePortfolio(positions);
        }

        return "I can analyze your portfolio for risk and Shariah compliance. What would you like to know?";
    }

    private String analyzePortfolio(List<StockPosition> positions) {
        JSONObject summary = new JSONObject();
        JSONArray positionsArray = new JSONArray();
        int nonHalalCount = 0;
        String nonHalalStocks = "";

        for (StockPosition pos : positions) {
            JSONObject posJson = new JSONObject();
            posJson.put("symbol", pos.getSymbol());
            posJson.put("quantity", pos.getQuantity());
            posJson.put("isHalal", pos.isHalal());
            positionsArray.put(posJson);

            if (!pos.isHalal()) {
                nonHalalCount++;
                nonHalalStocks += pos.getSymbol() + ", ";
            }
        }

        // Build the prompt for the AI model
        String analysisPrompt = "Analyze the following portfolio summary and provide insights on risk and Shariah compliance.\n"
                              + "Data: " + positionsArray.toString();

        // --- Placeholder for actual AI call ---
        // String aiResponse = callToLLM(analysisPrompt);

        // Simulated AI Response based on the data
        String simulatedResponse = "Based on your portfolio:\n";
        if (nonHalalCount > 0) {
            simulatedResponse += "- You hold " + nonHalalCount + " stock(s) that may not be Shariah-compliant: "
                               + nonHalalStocks.substring(0, nonHalalStocks.length() - 2) + ".\n";
        } else {
            simulatedResponse += "- All your current holdings appear to be within Shariah-compliant parameters.\n";
        }
        simulatedResponse += "- Your portfolio seems concentrated in specific sectors (further analysis needed for detailed risk).\n\n";
        simulatedResponse += "Disclaimer: This is not investment advice, but an informational analysis.";

        return simulatedResponse;
    }

    // This is a helper method to simulate getting data, as the actual method is async
    private List<StockPosition> getPositionsFromPortfolioSync() {
        // In a real app, this would be handled by the async callback of loadPortfolio
        return List.of(); // Returning empty list for now
    }

    public String getResponse(String query) {
        if (currentContext == Context.STOCK_PORTFOLIO) {
            return getFinancialAnalysis(query);
        }
        return "This is a general response from the AI agent."; // Placeholder
    }
}
