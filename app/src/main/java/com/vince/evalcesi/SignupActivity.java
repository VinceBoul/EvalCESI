package com.vince.evalcesi;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vince.evalcesi.helper.NetworkHelper;
import com.vince.evalcesi.model.HttpResult;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText username;
    EditText pwd;
    ProgressBar pg;
    Button btn;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.signup_username);
        pwd = (EditText) findViewById(R.id.signup_pwd);
        pg = (ProgressBar) findViewById(R.id.signup_pg);

        btn = (Button) findViewById(R.id.signup_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading(true);
                new SignupAsyncTask(v.getContext()).execute(username.getText().toString(), pwd.getText().toString());
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
    protected class SignupAsyncTask extends AsyncTask<String, Void, HttpResult> {

        Context context;

        public SignupAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected HttpResult doInBackground(String... params) {
            if(!NetworkHelper.isInternetAvailable(context)){
                //error
                return new HttpResult(500, null);
            }
            try {

                Map<String, String> p = new HashMap<>();
                p.put("username", params[0]);
                p.put("pwd", params[1]);

                HttpResult result = NetworkHelper.doPost(context.getString(R.string.url_signup), p, null);

                return result;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(final HttpResult response){
            loading(false);
            if(response.code == 200){
                SignupActivity.this.finish();
            } else {
                Toast.makeText(context, context.getString(R.string.error_signup), Toast.LENGTH_LONG).show();
            }
        }
    }
}
