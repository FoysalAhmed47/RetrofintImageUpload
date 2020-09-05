package com.example.retrofitimageupload;

import com.google.gson.annotations.SerializedName;

public class ImageClass {

    @SerializedName("name")
    private String Name;

   // @SerializedName("email")
    //private String Email;

    @SerializedName("image")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
