package com.example.postmyotpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.postmyotpapp.utils.OKHttpHandler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MessageListener{

    public static final String EXTRA_MESSAGE = "com.example.postmyotpapp.MESSAGE";
    public Button postAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register sms listener
        MessageReceiver.bindListener(this);

        postAPI = (Button)findViewById(R.id.PostAPItest);
        postAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String filepath = getApplicationContext().getFilesDir().getAbsolutePath()+"/otp.txt";
                    OKHttpHandler callAPI = new OKHttpHandler();
                    callAPI.postRequestFileBody(getString(R.string.api_URL),"PostOTPtester");

                }catch (IOException e){
                    Log.e("TAG error",e.getMessage(),e);
                }
            }
        });
    }

    @Override
    public void messageReceived(String message){

        Toast.makeText(this,"New Message Received: "+message,Toast.LENGTH_SHORT).show();
        //FileHandler fh = new FileHandler();
        //fh.writeDataToFile(getApplicationContext(),"otp.txt",message.substring(0,6));
        try{
            //String filepath = getApplicationContext().getFilesDir().getAbsolutePath()+"/otp.txt";
            Log.i("TAGPOSTOTPAPICall",message);
            OKHttpHandler callAPI = new OKHttpHandler();
            if(message.length() >=6) {
                callAPI.postRequestFileBody(getString(R.string.api_URL), message.substring(0, 6));
            }else{
                Toast.makeText(this,"OTP length short to post",Toast.LENGTH_SHORT).show();
            }
        }catch (IOException e){
            Log.e("TAG error",e.getMessage(),e);
        }
    }
}
