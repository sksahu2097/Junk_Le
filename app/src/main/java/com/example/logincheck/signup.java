package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.regex.*;

public class signup extends AppCompatActivity {

    EditText confpwd;
    EditText pwd;
    EditText email;
    EditText userid;
    EditText fname;
    EditText lname;
    EditText no;
    RadioGroup roles;
    RadioButton ans;
    ProgressBar mLoadingIndicator;
    TextView mErrorMessageDisplay;
    private final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private Pattern pattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pwd=(EditText) findViewById(R.id.edtPass);
        confpwd=(EditText) findViewById(R.id.edtConfirmPass);
        confpwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                String conf=confpwd.getText().toString();
                String pass=pwd.getText().toString();

                if(pass.equals(conf)){

                }else{

                    confpwd.setError("ERROR : password doesn't matched");

                }

            }
        });

        email=(EditText) findViewById(R.id.edtEmail);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                String mail=email.getText().toString();
                pattern=pattern.compile(EMAIL_REGEX,Pattern.CASE_INSENSITIVE);
                matcher=pattern.matcher(mail);
                if(matcher.matches()){

                }else{

                    email.setError("ERROR : please Enter a valid email");

                }

            }
        });

        userid = (EditText) findViewById(R.id.edtUsername);
        fname = (EditText) findViewById(R.id.edtfirstname);
        lname = (EditText) findViewById(R.id.edtlastname);
        roles = (RadioGroup) findViewById(R.id.role);
        no = (EditText) findViewById(R.id.no);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

    }

    public void LoginClick(View view){


        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    public  void register(View v){

        int selectedRole=roles.getCheckedRadioButtonId();
        ans=(RadioButton) findViewById(selectedRole);
        signupSearchQuery();

    }

    private void signupSearchQuery(){

        String fnames = fname.getText().toString();
        String lnames = lname.getText().toString();
        String username = userid.getText().toString();
        String pass = pwd.getText().toString();
        String mail = email.getText().toString();
        String role=ans.getText().toString();
        String mno = no.getText().toString();
        Toast.makeText(signup.this,"Welcome",Toast.LENGTH_SHORT).show();
        URL signup_url = SignupValidation.buildUrl(username,pass,role,fnames,lnames,mail,mno);
        new SignupTask().execute(signup_url);


    }

    private void showErrorMessage() {

        mErrorMessageDisplay.setVisibility(View.VISIBLE);

    }


    public class SignupTask extends AsyncTask<URL, Void, String> {

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
            if (githubSearchResults != null && !githubSearchResults.equals("")) {

                if(githubSearchResults.equals("success")){

                    Intent intent=new Intent(signup.this,MainActivity.class);
                    startActivity(intent);

                }
                //showJsonDataView();
            } else {

                showErrorMessage();
            }
        }
    }

}
