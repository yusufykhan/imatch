package com.example.imatch.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imatch.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.imatch.Activity.MainActivity.sp;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {


    boolean ifexists ;
    TextView ForgotPassword ;
    private EditText editTextEmail_var , editTextPassword_var;
    private TextInputLayout inputEmailLayout , inputPasswordLayout ;
    private CircularProgressButton loginbutton ;
    String Interest ,username , myname ,userpassword ,userphonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findides();

        loginbutton.setOnClickListener((View v) -> Login());

        ForgotPassword.setOnClickListener((View v) -> forgotPassword());

    }


    private void findides() {
        inputEmailLayout = findViewById(R.id.textInputEmailLogin);
        inputPasswordLayout  = findViewById(R.id.textInputPasswordLogin);

        editTextEmail_var = findViewById(R.id.editTextEmialLogin);
        editTextPassword_var = findViewById(R.id.editTextPasswordLogin);

        ForgotPassword = findViewById(R.id.forgotpassword);
        loginbutton = findViewById(R.id.cirLoginButton);

    }

    private void Login() {

        final String email = editTextEmail_var.getText().toString();
        final String password = editTextPassword_var.getText().toString();
        boolean isValid ;
        boolean isexist ;

        isValid = loginDetailsvalidation(email,password);

        if(isValid) {
            isexist = userExistance(email, password);
            if (isexist){

                sp = getSharedPreferences("Login",MODE_PRIVATE);


                Toast.makeText(LoginActivity.this,"seccess",Toast.LENGTH_LONG).show();
                Intent intentlogin = new Intent(getApplicationContext(), Upload_Profile_Photo.class);

                sp.edit().putString("user_email",email).apply();
                sp.edit().putString("interest",Interest).apply();
                sp.edit().putString("UserName",username).apply();
                sp.edit().putString("MyName",myname).apply();
                sp.edit().putString("UserPhoneNumber",userphonenumber).apply();
                sp.edit().putString("UserPassword",userpassword).apply();
                startActivity(intentlogin);
                sp.edit().putBoolean("loggedIn",true).apply();
                finish();

            }
        }


    }

    private boolean userExistance(String email, String password) {


        FirebaseDatabase firebaseLoginData  = FirebaseDatabase.getInstance();
        DatabaseReference referenceLogin = firebaseLoginData.getReference("userdata");

        Query checkEmail = referenceLogin.orderByChild("email").equalTo(email);
        checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    inputEmailLayout.setErrorEnabled(false);
                    ifexists = true ;

                    String checkPassword = snapshot.child(email).child("password").getValue(String.class);

                    assert checkPassword != null;
                    if(checkPassword.equals(password)){
                        inputPasswordLayout.setErrorEnabled(false);
                        Interest = snapshot.child(email).child("Interest").getValue(String.class);
                        username = snapshot.child(email).child("username").getValue(String.class);
                        myname = snapshot.child(email).child("name").getValue(String.class);
                        userpassword = snapshot.child(email).child("password").getValue(String.class);
                        userphonenumber = snapshot.child(email).child("phonenumber").getValue(String.class);
                        ifexists = true ;
                    }
                    else {
                        inputPasswordLayout.setError("Wrong Passowrd");
                        ifexists = false ;
                    }
                }
                else {
                    inputEmailLayout.setError("Email Doesn't Exists!");
                    ifexists = false ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return ifexists;
    }

    private boolean loginDetailsvalidation(String email, String password) {
        boolean isvVlid = true ;

        if(email.isEmpty()){
            inputEmailLayout.setError("Please Enter Email");
            isvVlid = false;
        }
        else{
            if(email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")){
                inputEmailLayout.setErrorEnabled(false);
            }
            else{
                inputEmailLayout.setError("Please Enter Valid Email");
                isvVlid = false;
            }
        }

        if(password.isEmpty()){
            inputPasswordLayout.setError("Please Enter Password");
            isvVlid = false;
        }
        else{
            if (password.length() > 5) {
                inputPasswordLayout.setErrorEnabled(false);
            }
            else{
                inputPasswordLayout.setError("Enter a Password at least 8 Characters");
                isvVlid = false;
            }
        }

        return isvVlid ;
    }


    public void onCreateClick(View View){
        startActivity(new Intent(this,SignupActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();

    }


    private void forgotPassword() {
        startActivity(new Intent(getApplicationContext(),Forgot_Password_Activity.class));
        finish();
    }


}