package com.vince.evalcesi;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vince.evalcesi.helper.JsonParser;
import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.helper.PreferenceHelper;
import com.vince.evalcesi.model.HttpResult;
import com.vince.evalcesi.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText pwd;
    ProgressBar pg;
    Button btn;
    View v ;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);
        v = findViewById(R.id.layout);
        username = (EditText) findViewById(R.id.signin_username);
        pwd = (EditText) findViewById(R.id.signin_pwd);
        pg = (ProgressBar) findViewById(R.id.signin_pg);
        btn = (Button) findViewById(R.id.signin_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                new SigninAsyncTask(v.getContext()).execute(username.getText().toString(), pwd.getText().toString());
            }
        });
        findViewById(R.id.signin_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(v.getContext(), SignupActivity.class);
                startActivity(i);*/
            }
        });

    }

    private void loading(boolean loading) {
        if(loading){
            pg.setVisibility(View.VISIBLE);
            btn.setVisibility(View.INVISIBLE);
        } else {
            pg.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * AsyncTask for sign-in
     */
    protected class SigninAsyncTask extends AsyncTask<String, Void, String> {

        Context context;

        public SigninAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                return null;
            }

            // Un stream pour récevoir la réponse
            InputStream inputStream = null;

            try {

                Map<String, String> p = new HashMap<>();
                p.put("username", params[0]);
                p.put("pwd", params[1]);

                HttpResult result = NetworkHelper.doPost(context.getString(R.string.url_signin), p, null);

                if(result.code == 200) {
                    // Convert the InputStream into a string
                    return JsonParser.getToken(result.json);
                }
                return null;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("NetworkHelper", e.getMessage());
                    }
                }
            }
        }

        @Override
        public void onPostExecute(final String token){
            loading(false);
            if(token != null){
                PreferenceHelper.setToken(MainActivity.this, token);
                Intent i = new Intent(context, MyTabActivity.class);
                i.putExtra(Constants.INTENT_TOKEN, token);
                startActivity(i);
            } else {
                Toast.makeText(v.getContext(), context.getString(R.string.error_login), Toast.LENGTH_LONG).show();
                /*
                Snackbar.make(v, context.getString(R.string.error_login), Snackbar.LENGTH_LONG).setAction("btn", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    }
                }).show();
                */
            }
        }
    }
}
