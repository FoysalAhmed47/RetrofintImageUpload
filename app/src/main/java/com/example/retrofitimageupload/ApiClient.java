package com.example.retrofitimageupload;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BaseUrl = "http://10.0.2.2/imageupload/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient()
    {
        if ((retrofit==null))
        {
            retrofit = new Retrofit.Builder().baseUrl(BaseUrl).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
