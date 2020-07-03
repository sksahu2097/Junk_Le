package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.net.URL;

public class membership extends AppCompatActivity {

    private TextView userid;
    private CheckBox member;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    String username=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        userid = (TextView) findViewById(R.id.userid);

        Intent intent = getIntent();
        username=intent.getStringExtra("userid");
        userid.setText(username);
        member = (CheckBox) findViewById(R.id.member);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

    }

    public void addMembership(View view){

            if(member.isChecked()){

                membershipAddQuery();

            }else{

                Toast.makeText(membership.this,
                        "first accept the condition ", Toast.LENGTH_LONG).show();


            }

    }

    private void membershipAddQuery() {

        URL login_url = MembershipValidation.buildUrl(username);
        //url_display.setText(login_url.toString());
        new MembershipTask().execute(login_url);
    }

    private void showErrorMessage() {

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }

    public class MembershipTask extends AsyncTask<URL, Void, String> {

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

            if (githubSearchResults.equals("success")) {

                //showJsonDataView();
                //mSearchResultsTextView.setText(githubSearchResults);
                Intent intent = new Intent(membership.this,SellingRequest.class);
                intent.putExtra("username",""+username);
                startActivity(intent);

            } else {

                showErrorMessage();
            }
        }
    }

}
