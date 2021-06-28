package com.example.imatch.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.imatch.R;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import static com.example.imatch.Activity.MainActivity.user_email;
import static com.example.imatch.Activity.MainActivity.sp;
import static com.example.imatch.Activity.MainActivity.UserPassword;
import static com.example.imatch.Activity.MainActivity.UserPhoneNumber;

public class Update_Details_Activity extends AppCompatActivity {



    private TextInputLayout  inputEmailLayout , inputPasswordLayout ,inputMobileLayout , inputConfirmPasswordLayout ,inputOldPasswordLayout ;
    private EditText  editTextEmail_var , editTextPassword_var ,editTextMobile_var ,editTextConfirmPassword_var ,editTextOldPassword_var;
    private Button Savebtn ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        findides();

        Savebtn.setOnClickListener(v -> updateDetails());

    }

    private void updateDetails() {
        boolean isValid ;

        FirebaseDatabase firebasedatabase ;
        DatabaseReference reference ;

        final String email = editTextEmail_var.getText().toString();
        final String mobile = editTextMobile_var.getText().toString();
        final String oldpassword = editTextOldPassword_var.getText().toString();
        final String password = editTextPassword_var.getText().toString();
        final String confirmpassword = editTextConfirmPassword_var.getText().toString();

        isValid = validationOfDetails(email,mobile,oldpassword, password ,confirmpassword);

        if(isValid){

            firebasedatabase = FirebaseDatabase.getInstance();
            reference = firebasedatabase.getReference( "userdata");

            UserPassword = null ;
            UserPhoneNumber = null ;


            reference.child(user_email).child("password").setValue(password);
            reference.child(user_email).child("phonenumber").setValue(mobile);

            sp = getSharedPreferences("Login",MODE_PRIVATE);


            Toast.makeText(Update_Details_Activity.this,"Details Updated Successfully",Toast.LENGTH_SHORT).show();
            Intent intentDetailUpdate = new Intent(Update_Details_Activity.this,MatchingActivity.class);

            sp.edit().putString("UserPhoneNumber",mobile).apply();
            sp.edit().putString("UserPassword",password).apply();
            UserPassword = password ;
            UserPhoneNumber = mobile ;

            startActivity(intentDetailUpdate);
            finish();


        }

    }

    private boolean validationOfDetails(String email, String mobile, String oldpassword, String password, String confirmpassword) {

        boolean isvVlid = true ;


        if(email.isEmpty()){
            inputEmailLayout.setError("Please Enter Email");
            isvVlid = false;
        }
        else{
            if(email.equals(user_email)){
                inputEmailLayout.setErrorEnabled(false);
            }
            else{
                inputEmailLayout.setError("Email Don't Match");
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

        if(oldpassword.isEmpty()){
            inputOldPasswordLayout.setError("Enter Old Password");
            isvVlid = false;
        }
        else{
            if(oldpassword.equals(UserPassword)) {
                inputOldPasswordLayout.setErrorEnabled(false);
            }
            else{
                inputOldPasswordLayout.setError("Old Password Don't Match");
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

    private void findides() {
        inputEmailLayout = findViewById(R.id.textInputEmail);
        inputMobileLayout  = findViewById(R.id.textInputMobile);
        inputOldPasswordLayout = findViewById(R.id.textInputOldPassword);
        inputPasswordLayout  = findViewById(R.id.textInputPassword);
        inputConfirmPasswordLayout  = findViewById(R.id.textInputConfirmPassword);

        editTextEmail_var = findViewById(R.id.editTextEmail);
        editTextMobile_var = findViewById(R.id.editTextMobile);
        editTextOldPassword_var = findViewById(R.id.editTextOldPassword);
        editTextPassword_var = findViewById(R.id.editTextPassword);
        editTextConfirmPassword_var = findViewById(R.id.editTextConfirmPassword);

        Savebtn = findViewById(R.id.save_btn);

    }
}