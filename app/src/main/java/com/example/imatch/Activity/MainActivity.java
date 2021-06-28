package com.example.imatch.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.imatch.R;


public class MainActivity extends AppCompatActivity {

    static SharedPreferences sp ;
    public static  String  user_email , interest , UserName ,MyName , UserPassword ,UserPhoneNumber ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            sp = getSharedPreferences("Login",MODE_PRIVATE);

            if(sp.getBoolean("loggedIn",false)){
                Intent intent=new Intent(MainActivity.this, Upload_Profile_Photo.class);
                startActivity(intent);
                finish();
            }

            else {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }

}