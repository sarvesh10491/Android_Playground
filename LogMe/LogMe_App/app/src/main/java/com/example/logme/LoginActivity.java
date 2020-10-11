package com.example.logme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static com.example.logme.properties.Constants.SERVER_IP;
import static com.example.logme.properties.Constants.SERVER_PORT;

public class LoginActivity extends AppCompatActivity{

    EditText email, password;
    Button login;
    TextView register;
    boolean isEmailValid, isPasswordValid;

    private ClientThread clientThread;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SetValidation();
                clientThread = new ClientThread();
                thread = new Thread(clientThread);
                thread.start();
                String clientMessage = "abcd"; // Placeholder input string
                if (null != clientThread) {
                    clientThread.sendMessage(clientMessage);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // redirect to RegisterActivity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void SetValidation(){
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

        // Check for a valid password.
        if(password.getText().toString().isEmpty()){
            password.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        }
        else if(password.getText().length() < 8){
            password.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        }
        else{
            isPasswordValid = true;
        }

        if(isEmailValid && isPasswordValid){
            Toast.makeText(getApplicationContext(), "Validated Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    class ClientThread implements Runnable{
        private Socket socket;
        private BufferedReader input;

        @Override
        public void run(){
            try{
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);

                while (!Thread.currentThread().isInterrupted()){
                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if(null == message || "Disconnect".contentEquals(message)){
                        Thread.interrupted();
                        message = "Server Disconnected.";
                        break;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        void sendMessage(final String message){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (null != socket) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            out.println(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }
}