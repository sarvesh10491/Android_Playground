package com.example.unscrabble;
import com.example.unscrabble.properties.Constants;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText letters;
    private View runButton;
    private TextView results;
    String inputtext;
    String TAG = "MAIN ACTIVITY";
    String res = "";
    long cloudLatency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        letters = findViewById(R.id.letters);
        runButton = findViewById(R.id.server_run_btn);
        results = findViewById(R.id.results);



        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runButton.setEnabled(false);
                try {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert inputManager != null;
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception ignored){}
                runButton.requestFocus();

                inputtext = letters.getText().toString();
//                results.setText(inputtext);
                new processCloudReq().execute(inputtext);
                runButton.setEnabled(true);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class processCloudReq extends AsyncTask<String, Void, Void>
    {
        long startTime,endTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                res = "";
                String urlStr = Constants.CLOUD_URL+params[0];
                URL url = new URL(urlStr);
                startTime = System.currentTimeMillis();
                HttpURLConnection c = (HttpURLConnection) url.openConnection();     //Open Url Connection
                c.setRequestMethod("GET");      //Set Request Method to "GET" since we are getting data
//                c.setReadTimeout(5000);
//                c.setConnectTimeout(5000);
                c.connect();        //connect the URL Connection
                endTime = System.currentTimeMillis();
                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());
                }

                InputStream is = c.getInputStream();       //Get InputStream for connection
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                res = reader.readLine();
                is.close();
            }
            catch (Exception e){
                res = "error connecting cloud";
                endTime = System.currentTimeMillis();
                e.printStackTrace();
                Log.e(TAG, "Error Connecting Cloud Exception " + e.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            cloudLatency = endTime - startTime;
            results.setText(res);

            StringBuilder output = new StringBuilder("");;
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            assert jArray != null;
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array

                    output.append(oneObject.getString("word"));
                    output.append(" ");
                    output.append(oneObject.getString("score"));
                    output.append("\n");

                } catch (JSONException e) {
                    // Oops
                }
            }
            results.setText(output);

            runButton.setEnabled(true);
        }
    }
}
