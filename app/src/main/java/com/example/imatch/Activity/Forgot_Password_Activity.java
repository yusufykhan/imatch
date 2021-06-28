package com.example.imatch.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;


import com.example.imatch.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


import static com.example.imatch.Activity.MainActivity.user_email;

public class Forgot_Password_Activity extends AppCompatActivity {


    FirebaseDatabase firebasedatabase ;
    DatabaseReference reference ;
    boolean ifexists ;

    private TextInputLayout inputNameLayout , inputEmailLayout , inputNewPasswordLayout ,inputMobileLayout , inputConfirmNewPasswordLayout , inputUsernameLayout ;
    private EditText editTextName_var , editTextEmail_var , editTextNewPassword_var ,editTextMobile_var ,editTextConfirmNewPassword_var ,eidtTextUsername_var ;
    private CircularProgressButton SavePassword ;
    private String  databaseName , databaseUsername , databaseMobile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findides();

        SavePassword.setOnClickListener((View v) -> forgotPassword());

    }


    private void findides() {
        inputNameLayout  = findViewById(R.id.textInputName);
        inputUsernameLayout = findViewById(R.id.textInputUsername);
        inputEmailLayout = findViewById(R.id.textInputEmail);
        inputMobileLayout  = findViewById(R.id.textInputMobile);
        inputNewPasswordLayout  = findViewById(R.id.textInputNewPasswordlayout);
        inputConfirmNewPasswordLayout  = findViewById(R.id.textInputConfirmNewPasswordlayout);

        editTextName_var = findViewById(R.id.editTextName);
        eidtTextUsername_var = findViewById(R.id.editTextUsername);
        editTextEmail_var = findViewById(R.id.editTextEmail);
        editTextMobile_var = findViewById(R.id.editTextMobile);
        editTextNewPassword_var = findViewById(R.id.editTextNewPassword);
        editTextConfirmNewPassword_var = findViewById(R.id.editTextConfirmNewPassword);

        SavePassword = findViewById(R.id.cirSavePasswordButton);

    }


    private void forgotPassword() {


        final String name = editTextName_var.getText().toString();
        final String username = eidtTextUsername_var.getText().toString();
        final String email = editTextEmail_var.getText().toString();
        final String mobile = editTextMobile_var.getText().toString();
        final String newpassword = editTextNewPassword_var.getText().toString();
        final String confirmnewpassword = editTextConfirmNewPassword_var.getText().toString();

        boolean isfine = chek_every_entry_is_fine(name,username,email,mobile,newpassword,confirmnewpassword);

        if (isfine) {
            firebasedatabase = FirebaseDatabase.getInstance();
            reference = firebasedatabase.getReference("userdata");

            Query checkEmail = reference.orderByChild("email").equalTo(email);

            checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        inputEmailLayout.setErrorEnabled(false);

//                        snapshot.child(email).child("password").getValue(String.class);
                        databaseName = snapshot.child(email).child("name").getValue(String.class);
                        databaseUsername = snapshot.child(email).child("username").getValue(String.class);
                        databaseMobile = snapshot.child(email).child("phonenumber").getValue(String.class);

                        System.out.println("Name : "+databaseName);
                        System.out.println("Username"+databaseUsername);
                        System.out.println("databaseMobile"+databaseMobile);


                        ifexists = checking_Entered_Data(name, username, mobile);

                        if (ifexists) {
                            reference.child(email).child("password").setValue(newpassword);
                            Toast.makeText(Forgot_Password_Activity.this, "Password Changed Successfullly", Toast.LENGTH_LONG).show();
                            Intent intentnewpasswordset = new Intent(Forgot_Password_Activity.this, LoginActivity.class);
                            startActivity(intentnewpasswordset);
                        }


                    } else {
                        inputEmailLayout.setError("Email Doesn't Exists!");
                        ifexists = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


    }

    private boolean chek_every_entry_is_fine(String name, String username, String email, String mobile, String newpassword, String confirmnewpassword) {
    boolean isfine = true ;

        if(name.isEmpty()){
            inputNameLayout.setError("Please Enter Name");
            isfine = false;
        }
        else{
            if(name.matches("(" + "\\p{Upper}(\\p{Lower}+\\s?)" + "){1,3}")){
                inputNameLayout.setErrorEnabled(false);
            }
            else{
                inputNameLayout.setError("Please Enter Valid Name");
                isfine = false;
            }
        }

        if(username.isEmpty()){
            inputUsernameLayout.setError("Please Enter Username");
            isfine = false;
        }
        else{
            if(username.matches("^[a-zA-Z0-9._]+$")){
                inputUsernameLayout.setErrorEnabled(false);
            }
            else{
                inputUsernameLayout.setError("Please Enter Valid Username");
                isfine = false;
            }
        }

        if(email.isEmpty()){
            inputEmailLayout.setError("Please Enter Email");
            isfine = false;
        }
        else{
            if(email.matches("^(.+)@(.+)$")){
                inputEmailLayout.setErrorEnabled(false);
            }
            else{
                inputEmailLayout.setError("Please Enter Valid Email");
                isfine = false;
            }
        }

        if(mobile.isEmpty()){
            inputMobileLayout.setError("Please Enter Mobile Number");
            isfine = false;
        }
        else{
            if(mobile.length()==10){
                inputMobileLayout.setErrorEnabled(false);
            }
            else{
                inputMobileLayout.setError("Enter Valid Mobile Number");
                isfine = false;
            }
        }

        if(newpassword.isEmpty()){
            inputNewPasswordLayout.setError("Please Enter New Password");
            isfine = false;
        }
        else{
            if (newpassword.length() > 5) {
                inputNewPasswordLayout.setErrorEnabled(false);
            }
            else{
                inputNewPasswordLayout.setError("Enter a Password at least 6 Characters");
                isfine = false;
            }
        }

        if(confirmnewpassword.isEmpty()){
            inputConfirmNewPasswordLayout.setError("Please Re-Enter Password");
            isfine = false;
        }
        else{
            if(confirmnewpassword.equals(newpassword)) {
                inputConfirmNewPasswordLayout.setErrorEnabled(false);
            }
            else{
                inputConfirmNewPasswordLayout.setError("Password Don't Match");
                isfine = false;
            }
        }

    return isfine ;
    }

    private boolean checking_Entered_Data(String name, String username, String mobile) {

        ifexists = true ;

        if(name.equals(databaseName)){
            inputNameLayout.setErrorEnabled(false);
        }
        else{
            inputNameLayout.setError("Name Doesn't Match");
            ifexists = false;
        }

        if(username.equals(databaseUsername)){
            inputUsernameLayout.setErrorEnabled(false);
        }
        else{
            inputUsernameLayout.setError("Username Doesn't Match");
            ifexists = false;

        }

        if(mobile.equals(databaseMobile)){
            inputMobileLayout.setErrorEnabled(false);
        }
        else{
            inputMobileLayout.setError("Mobile Number Doesn't Match");
            ifexists = false;

        }


        return ifexists ;
    }


    public void onLoginClick(View view){
        startActivity(new Intent(Forgot_Password_Activity.this,LoginActivity.class));
        finish();
    }


}
