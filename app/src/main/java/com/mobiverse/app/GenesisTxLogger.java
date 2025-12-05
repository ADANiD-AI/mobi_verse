
package com.mobiverse.app;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class GenesisTxLogger {

    private static final String BASE_URL = "http://localhost:8000/";
    private final GenesisApiService apiService;

    public GenesisTxLogger() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GenesisApiService.class);
    }

    public void logTx(String userId, String action, String module) {
        TxPayload payload = new TxPayload(userId, action, module);
        apiService.logTx(payload).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Handle successful response (or not)
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private interface GenesisApiService {
        @POST("tx")
        Call<Void> logTx(@Body TxPayload payload);
    }

    private static class TxPayload {
        final String user;
        final String action;
        final String module;

        TxPayload(String user, String action, String module) {
            this.user = user;
            this.action = action;
            this.module = module;
        }
    }
}
