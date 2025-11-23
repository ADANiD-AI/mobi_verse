package com.mobiverse.MobiVerse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class StockPortfolioActivity : AppCompatActivity() {

    private lateinit var rvHoldings: RecyclerView
    // private lateinit var adapter: HoldingsAdapter // You would create this adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_portfolio)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        rvHoldings = findViewById(R.id.rv_holdings)
        rvHoldings.layoutManager = LinearLayoutManager(this)
        // adapter = HoldingsAdapter() // You would create this adapter
        // rvHoldings.adapter = adapter

        // TODO: Load data from Firestore / local DB
        loadPortfolioData()
    }

    private fun loadPortfolioData() {
        // Here you can put your MobiverseStockPortfolio logic + Firebase mapping
    }
}
