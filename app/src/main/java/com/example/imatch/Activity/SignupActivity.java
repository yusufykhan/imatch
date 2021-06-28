package com.example.imatch.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.imatch.R;
import com.example.imatch.JavaClasses.datastoring;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.imatch.Activity.MainActivity.sp;


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class SignupActivity extends AppCompatActivity {

    FirebaseDatabase firebasedatabase ;
    DatabaseReference reference ;

    private TextInputLayout  inputNameLayout , inputEmailLayout , inputPasswordLayout ,inputMobileLayout , inputConfirmPasswordLayout, inputUsernameLayout ;
    private EditText editTextName_var , editTextEmail_var , editTextPassword_var ,editTextMobile_var ,editTextConfirmPassword_var ,eidtTextUsername_var ;
    private CircularProgressButton registerbutton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findides();

        registerbutton.setOnClickListener((View v) -> Register());

    }

    private void findides() {
        inputNameLayout  = findViewById(R.id.textInputName);
        inputUsernameLayout = findViewById(R.id.textInputUsername);
        inputEmailLayout = findViewById(R.id.textInputEmail);
        inputMobileLayout  = findViewById(R.id.textInputMobile);
        inputPasswordLayout  = findViewById(R.id.textInputPassword);
        inputConfirmPasswordLayout  = findViewById(R.id.textInputConfirmPassword);

        editTextName_var = findViewById(R.id.editTextName);
        eidtTextUsername_var = findViewById(R.id.editTextUsername);
        editTextEmail_var = findViewById(R.id.editTextEmail);
        editTextMobile_var = findViewById(R.id.editTextMobile);
        editTextPassword_var = findViewById(R.id.editTextPassword);
        editTextConfirmPassword_var = findViewById(R.id.editTextConfirmPassword);

        registerbutton = findViewById(R.id.cirRegisterButton);
    }

    private void Register() {

        boolean isVlid ;

        final String name = editTextName_var.getText().toString();
        final String username = eidtTextUsername_var.getText().toString();
        final String email = editTextEmail_var.getText().toString();
        final String mobile = editTextMobile_var.getText().toString();
        final String password = editTextPassword_var.getText().toString();
        final String confirmpassword = editTextConfirmPassword_var.getText().toString();

        isVlid = registrationFormValidation(name,username,email,mobile,password,confirmpassword);

        if(isVlid) {

            firebasedatabase = FirebaseDatabase.getInstance();
            reference = firebasedatabase.getReference( "userdata");

            Query checkEmail = reference.orderByChild("email").equalTo(email);
            checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        inputEmailLayout.setError("Email Alredady Exists");
                    }
                    else {
                        inputEmailLayout.setErrorEnabled(false);
                        Query checkUsername = reference.orderByChild("username").equalTo(username);
                        checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    inputUsernameLayout.setError("Username Alredady Exists");
                                }
                                else {

                                    sp = getSharedPreferences("Login",MODE_PRIVATE);
                                    inputUsernameLayout.setErrorEnabled(false);
                                    datastoring storedata = new datastoring(name,username,email ,mobile,password);
                                    reference.child(email).setValue(storedata);
                                    Toast.makeText(SignupActivity.this,"Registered Seccessfully",Toast.LENGTH_SHORT).show();
                                    Intent intentregister = new Intent(getApplicationContext(),Upload_Profile_Photo.class);

                                    sp.edit().putString("user_email",email).apply();
                                    sp.edit().putString("UserName",username).apply();
                                    sp.edit().putString("MyName",name).apply();
                                    sp.edit().putString("UserPassword",password).apply();
                                    sp.edit().putString("UserPhoneNumber",mobile).apply();
                                    sp.edit().putString("Signned_is_true","Signned_is_true").apply();
                                    startActivity(intentregister);
                                    sp.edit().putBoolean("loggedIn",true).apply();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private boolean registrationFormValidation(String name ,String username,String email ,String mobile, String password, String confirmpassword) {

        boolean isvVlid = true ;

        if(name.isEmpty()){
            inputNameLayout.setError("Please Enter Name");
            isvVlid = false;
        }
        else{
            if(name.matches("(" + "\\p{Upper}(\\p{Lower}+\\s?)" + "){1,3}")){
                inputNameLayout.setErrorEnabled(false);
            }
            else{
                inputNameLayout.setError("Please Enter Valid Name");
                isvVlid = false;
            }
        }

        if(username.isEmpty()){
            inputUsernameLayout.setError("Please Enter Username");
            isvVlid = false;
        }
        else{
            if(username.matches("^[a-zA-Z0-9._]+$")){
                inputUsernameLayout.setErrorEnabled(false);
            }
            else{
                inputUsernameLayout.setError("Please Enter Valid Username");
                isvVlid = false;
            }
        }

        if(email.isEmpty()){
            inputEmailLayout.setError("Please Enter Email");
            isvVlid = false;
        }
        else{
            if(email.matches("^(.+)@(.+)$")){
                inputEmailLayout.setErrorEnabled(false);
            }
            else{
                inputEmailLayout.setError("Please Enter Valid Email");
                isvVlid = false;
            }
        }

        if(mobile.isEmpty()){
            inputMobileLayout.setError("Please Enter Mobile Number");
            isvVlid = false;
        }
        else{
            if(mobile.length()==10){
                inputMobileLayout.setErrorEnabled(false);
            }
            else{
                inputMobileLayout.setError("Enter Valid Mobile Number");
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
                inputPasswordLayout.setError("Enter a Password at least 6 Characters");
                isvVlid = false;
            }
        }

        if(confirmpassword.isEmpty()){
            inputConfirmPasswordLayout.setError("Please Re-Enter Password");
            isvVlid = false;
        }
        else{
            if(confirmpassword.equals(password)) {
                inputConfirmPasswordLayout.setErrorEnabled(false);
            }
            else{
                inputConfirmPasswordLayout.setError("Password Don't Match");
                isvVlid = false;
            }
        }

        return isvVlid ;
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }


}