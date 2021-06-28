package com.example.imatch.Activity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.imatch.Activity.MainActivity.interest;
import static com.example.imatch.Activity.MainActivity.sp;

import com.example.imatch.Adapters.MyAdapter;
import com.example.imatch.R;


import static com.example.imatch.Activity.MainActivity.user_email;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InterestActivity extends AppCompatActivity {

    ListView listView;
    TextView save;
    BottomNavigationView Bottom_Nevigation ;

    String is_selected ;

    FirebaseDatabase firebasedatabaseforinterest ;
    DatabaseReference referencetostoreinterest ;

    public static String[] interestslist = {
            "Football", "Cricket", "Sports" , "Movies_and_Series", "Travelling",
            "Cooking","Board_Games", "Bollywood" , "Music" , "Photograhpy" , "Video_Games",
            "Creative_Art", "Reading", "Writing" , "Dancing" , "History" ,
            "Gardening", "Technology" ,"Meet_New_People"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        sp = getSharedPreferences("Login",MODE_PRIVATE);
        user_email = sp.getString("user_email","");

        findides();


        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            is_selected = interestslist[position];
            System.out.println(interestslist[position]);
            Toast.makeText(InterestActivity.this, "You Selected " + interestslist[position] + "", Toast.LENGTH_SHORT).show();
            view.setSelected(true);

                save.setOnClickListener((View v) -> {


                    firebasedatabaseforinterest = FirebaseDatabase.getInstance();
                    referencetostoreinterest = firebasedatabaseforinterest.getReference("userdata");
//                    datastoring datastoring = new datastoring(is_selected);
                    if (!(user_email == null)) {
                        referencetostoreinterest.child(user_email).child("Interest").setValue(is_selected);
                    }

                    sp = getSharedPreferences("Login",MODE_PRIVATE);

                    sp.edit().putString("interest",is_selected).apply();
                    interest = is_selected ;
                    Intent intentstartmatching = new Intent(getApplicationContext(), MatchingActivity.class);
                    startActivity(intentstartmatching);
                    finish();
                });
        });



        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Toast.makeText(InterestActivity.this,"long clicked ",Toast.LENGTH_SHORT).show();
            is_selected = interestslist[position];
            Intent intentShowInformation = new Intent(getApplicationContext(), Interest_Information_Activity.class);
            intentShowInformation.putExtra("interest",is_selected);
            startActivity(intentShowInformation);
            return true;
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void findides() {

        MyAdapter adapter = new MyAdapter(this, interestslist);


        Bottom_Nevigation = findViewById(R.id.bottom_Nevigation);
        save = findViewById(R.id.savebtn);
        listView = findViewById(R.id.listview);


        listView.setAdapter(adapter);

        if(interest.isEmpty()) {
            Bottom_Nevigation.setVisibility(View.GONE);
        }

        Bottom_Nevigation.setSelectedItemId(R.id.interets);


        Bottom_Nevigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.interets:
                    return true;
                case R.id.matching:
                    startActivity(new Intent(getApplicationContext(), MatchingActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
                case R.id.chat:
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
                case R.id.setting:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return false;
            }
            return false;
        });

    }


}