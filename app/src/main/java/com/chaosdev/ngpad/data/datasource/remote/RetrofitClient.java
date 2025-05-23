package com.chaosdev.ngpad.data.datasource.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  private static final String BASE_URL =
      "https://tutorial-api-7nw2.onrender.com/"; // Replace with your API base URL
  private static Retrofit retrofit;

  public static ApiService getApiService() {
    if (retrofit == null) {
      retrofit =
          new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit.create(ApiService.class);
  }
}
