package com.vidudiv.simpleapirequestsender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    Button sendBtn;
    Button clearBtn;
    Context context = this;
    EditText url_text;
    EditText req_type;
    EditText req_data;
    EditText result_txt;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getPreferences(Context.MODE_PRIVATE);





        sendBtn = (Button)findViewById(R.id.btn_send);
         clearBtn = (Button)findViewById(R.id.btn_clear);
         url_text = (EditText) findViewById(R.id.txt_url);
         req_type = (EditText) findViewById(R.id.txt_method);
         req_data = (EditText) findViewById(R.id.txt_send_data);
         result_txt = (EditText) findViewById(R.id.txt_result);

        //get from shard pref if exists
        url_text.setText(sharedPref.getString("api_url",""));
        req_type.setText(sharedPref.getString("method","GET"));
        req_data.setText(sharedPref.getString("req_data",""));



         /** on click send btn */
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                try {
                    setSendingState(true);

                    SharedPreferences.Editor seditor = sharedPref.edit();
                    seditor.putString("api_url", url_text.getText().toString());
                    seditor.putString("method", req_type.getText().toString());
                    seditor.putString("req_data", req_data.getText().toString());
                    seditor.commit();

                    sendRequest(url_text.getText().toString(),req_type.getText().toString(), req_data.getText().toString());

                } catch (Exception e) {
                    setSendingState(false);
                    e.printStackTrace();
                }
            }
            });


        /** on click clear btn */
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result_txt.setText("");
            }
        });

    }


    public void sendRequest(String url, String method , String Data){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String metohdTxt = method.toUpperCase().trim();
        Integer reqMth = -1;

        /*
        * request method Request.Method
        * old get post = -1
        * get = 0
        * post = 1
        * put = 2
        * delete = 3
        * head = 4
        * options = 5
        * trace = 6
        * patch = 7
        *
        * */

        if(metohdTxt.equals("GET")){
            reqMth = 0;
        }else if(metohdTxt.equals("POST")){
            reqMth = 1;
        }else if(metohdTxt.equals("PUT")){
            reqMth = 2;
        }
        else if(metohdTxt.equals("DELETE")){
            reqMth = 3;
        }else if(metohdTxt.equals("HEAD")){
            reqMth = 4;
        }else if(metohdTxt.equals("OPTIONS")){
            reqMth = 5;
        }else if(metohdTxt.equals("TRACE")){
            reqMth = 6;
        }else if(metohdTxt.equals("PATCH")){
            reqMth = 7;
        }

        //Log.d("metohdTxt",metohdTxt);
       // Log.d("reqMth",reqMth.toString());

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(reqMth, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        result_txt.setText("Response is: "+ response.toString());
                        setSendingState(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result_txt.setText("Request Error: "+ error.toString());
                setSendingState(false);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    void setSendingState(boolean state){
        if(state){
            sendBtn.setEnabled(false);
            sendBtn.setText("Sending..");
        }else {
            sendBtn.setEnabled(true);
            sendBtn.setText("SEND");
        }
    }
}