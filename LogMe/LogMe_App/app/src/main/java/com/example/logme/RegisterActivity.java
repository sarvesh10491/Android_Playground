package com.example.logme;

import android.content.Intent;
import android.util.Patterns;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

public class RegisterActivity extends  AppCompatActivity{

    EditText name, email, phone, password;
    Button register;
    TextView login;
    boolean isNameValid, isEmailValid, isPhoneValid, isPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SetValidation();
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // redirect to LoginActivity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SetValidation(){
        // Check for a valid name.
        if (name.getText().toString().isEmpty()){
            name.setError(getResources().getString(R.string.name_error));
            isNameValid = false;
        }
        else{
            isNameValid = true;
        }

        // Check for a valid email address.
        if(email.getText().toString().isEmpty()){
            email.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        }
        else{
            isEmailValid = true;
        }

        // Check for a valid phone number.
        if(phone.getText().toString().isEmpty()){
            phone.setError(getResources().getString(R.string.phone_error));
            isPhoneValid = false;
        }
        else{
            isPhoneValid = true;
        }

        // Check for a valid password.
        if(password.getText().toString().isEmpty()){
            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        }
        else if(password.getText().length() < 6){
            password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }
        else{
            isPasswordValid = true;
        }

        if (isNameValid && isEmailValid && isPhoneValid && isPasswordValid){
            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
        }

    }

}