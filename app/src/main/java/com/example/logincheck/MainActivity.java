package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText user;
    private EditText pass;
    private TextView url_display;
    private Button btn;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private TextView mSearchResultsTextView;
    private RadioGroup roles;
    private RadioButton ans;
    String res[];
    String role=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText) findViewById(R.id.usr);
        user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String usr=user.getText().toString();

                if (usr.length() < 1){

                    user.setError("Error : Please Enter user Name");

                }
            }
        });
        pass=(EditText) findViewById(R.id.pwd);
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                String pwd=pass.getText().toString();

                if (pwd.length() < 1){

                    pass.setError("Error : Please Enter Password");

                }

            }
        });

        btn=(Button) findViewById(R.id.login);
        btn.setOnClickListener(this);
        url_display=(TextView) findViewById(R.id.url_display);
        mLoadingIndicator=(ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay=(TextView) findViewById(R.id.tv_error_message_display);
        mSearchResultsTextView=(TextView) findViewById(R.id.tv_github_search_results_json);
        roles=(RadioGroup) findViewById(R.id.role);

    }

    public void signup(View view){

        Intent intent=new Intent(this,signup.class);
        startActivity(intent);


    }

    @Override
    public void onClick(View view) {

        int selectedRole=roles.getCheckedRadioButtonId();
        ans=(RadioButton) findViewById(selectedRole);
        loginSearchQuery();

    }

    private void loginSearchQuery() {
        String username = user.getText().toString();
        String password = pass.getText().toString();
        role=ans.getText().toString();
        Toast.makeText(MainActivity.this,
                ans.getText(), Toast.LENGTH_SHORT).show();
        URL login_url = LoginValidation.buildUrl(username,password,role);
        //url_display.setText(login_url.toString());
        new LoginTask().execute(login_url);
    }


    private void showErrorMessage() {

        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    private void showJsonDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);

    }

    public class LoginTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            String result=null;
            try {
                githubSearchResults = LoginValidation.getResponseFromHttpUrl(searchUrl);
                JSONObject msg=new JSONObject(githubSearchResults);
                result=msg.getString("msg");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,
                    ""+githubSearchResults, Toast.LENGTH_LONG).show();
            if (githubSearchResults.equals("Valid")) {

                //showJsonDataView();
                //mSearchResultsTextView.setText(githubSearchResults);


                if(role.equals("Seller")){

                    Intent intent = new Intent(MainActivity.this,SellingRequest.class);
                    intent.putExtra("username",""+user.getText().toString());
                    startActivity(intent);

                }else if(role.equals("Buyer")){

                    Intent intent = new Intent(MainActivity.this,BuyingRequest.class);
                    intent.putExtra("username",""+user.getText().toString());
                    startActivity(intent);

                }else{

                    Intent intent = new Intent(MainActivity.this,pickup_boy.class);
                    intent.putExtra("username",""+user.getText().toString());
                    startActivity(intent);

                }


            }else if (!githubSearchResults.equals("") && githubSearchResults.equals("Invalid")){

                mErrorMessageDisplay.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this,
                        "Invalid Username or password", Toast.LENGTH_LONG).show();
                mErrorMessageDisplay.setVisibility(View.INVISIBLE);

            } else {

                showErrorMessage();
            }
        }
    }



}
