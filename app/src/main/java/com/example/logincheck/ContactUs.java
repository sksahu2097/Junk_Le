package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

public class ContactUs extends AppCompatActivity {

    private EditText edtname,edtemail,edtno,edtmessage;
    private Button submit;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        edtemail = (EditText) findViewById(R.id.edtEmail);
        edtmessage = (EditText) findViewById(R.id.edtmessage);
        edtname = (EditText) findViewById(R.id.edtname);
        edtno = (EditText) findViewById(R.id.edtno);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }

    public void onSubmit(View view){

        String name = edtname.getText().toString();
        String mail = edtemail.getText().toString();
        String message = edtmessage.getText().toString();
        String no = (edtno.getText().toString());

        URL feedback_url=ContactUsValidation.buildUrl(name,mail,no,message);
        new FeedbackSubmitTask().execute(feedback_url);

    }


    public class FeedbackSubmitTask extends AsyncTask<URL, Void, String> {

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
                githubSearchResults = CheckMembershipValidation.getResponseFromHttpUrl(searchUrl);
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

                Toast.makeText(ContactUs.this,"Feedback submitted",Toast.LENGTH_SHORT).show();


            } else {

                //showErrorMessage();
            }
        }
    }

}
