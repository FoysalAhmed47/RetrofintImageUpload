package com.example.retrofitimageupload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Img_name;
    private Button BnChoose,BnUpload;
    private ImageView Img;
    private static final int IMAGE_REQUEST=777;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Img_name = (EditText)findViewById(R.id.EditText_1);
        //Img_email = (EditText)findViewById(R.id.EditText_2);
        BnChoose = (Button)findViewById(R.id.choose_btn);
        BnUpload = (Button)findViewById(R.id.upload_btn);
        Img = (ImageView)findViewById(R.id.imageView);

        BnChoose.setOnClickListener(this);
        BnUpload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.choose_btn:
                selectImage();
                break;

            case R.id.upload_btn:
                uploadImage();
                break;
        }
    }

    private void uploadImage()
    {
      String Image = imageToString();
      String Name = Img_name.getText().toString();
      //String Email = Img_email.getText().toString();

      ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass> call = (Call<ImageClass>) apiInterface.uploadImage(Name,Image);

        call.enqueue(new Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, Response<ImageClass> response) {
               ImageClass imageClass = response.body();
                Toast.makeText(MainActivity.this,"Server Response:" +imageClass.getResponse(),Toast.LENGTH_SHORT).show();
                Img.setVisibility(View.GONE);
                Img_name.setVisibility(View.GONE);
                //Img_email.setVisibility(View.GONE);
                BnChoose.setEnabled(true);
                BnUpload.setEnabled(false);

                Img_name.setText("");
                //Img_email.setText("");

            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Server Response:" +call.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null))
        {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                Img.setImageBitmap(bitmap);
                    Img.setVisibility(View.VISIBLE);
                    Img_name.setVisibility(View.VISIBLE);
                    //Img_email.setVisibility(View.VISIBLE);

                    BnChoose.setEnabled(false);
                    BnUpload.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //compress image and Decode it
    private String imageToString()
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte,Base64.DEFAULT);
    }
}
