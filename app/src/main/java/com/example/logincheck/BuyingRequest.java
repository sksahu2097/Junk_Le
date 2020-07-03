package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;

public class BuyingRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    private TextView showDate;
    private TextView displayUser;
    private String username;
    private EditText quantity;
    private float price1=4;
    private float amount;
    private TextView price;
    float q;
    private ProgressBar mLoadingIndicator;
    private Spinner category;
    String cat=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying_request);

        showDate = (TextView) findViewById(R.id.showDate);

        displayUser = (TextView) findViewById(R.id.displayUser);

        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        displayUser.setText(username);
        price = (TextView) findViewById(R.id.price);
        category = (Spinner) findViewById(R.id.category);
        category.setOnItemSelectedListener(this);
        //cat=String.valueOf(category.getSelectedItem());
        //cat.toLowerCase();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        quantity = (EditText) findViewById(R.id.quantity);


    }


    public void chooseDate(View v){

        q=Float.parseFloat(quantity.getText().toString());
        checkMembership();
        calendar=Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog=new DatePickerDialog(BuyingRequest.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                i1++;
                showDate.setText(i+"/"+i1+"/"+i2);

            }
        },day,month,year);
        datePickerDialog.show();


    }

    public void insatiateMembership(View view){

        Intent intent=new Intent(BuyingRequest.this,membership.class);
        intent.putExtra("userid",""+displayUser.getText().toString());
        startActivity(intent);

    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        cat = category.getItemAtPosition(position).toString();
        cat.toLowerCase();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void checkMembership() {

        URL login_url = CheckMembershipValidation.buildUrl(username);
        //url_display.setText(login_url.toString());
        new BuyingRequest.CheckMembershipTask().execute(login_url);
    }

    public void buyerOrder(View view){

        URL buyerurl = BuyerOrderValidate.buildUrl(username,cat,quantity.getText().toString(),""+amount,showDate.getText().toString());

        new BuyerOrderTask().execute(buyerurl);
    }



    public class BuyerOrderTask extends AsyncTask<URL, Void, String> {

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
                githubSearchResults = BuyerOrderValidate.getResponseFromHttpUrl(searchUrl);
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
                /*Toast.makeText(MainActivity.this,
                        "Order Placed", Toast.LENGTH_LONG).show();
                Intent intent= new Intent(SellingRequest.this,MainActivity.class);
                startActivity(intent);*/
                Toast.makeText(BuyingRequest.this,
                        "Order Placed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BuyingRequest.this,OrderConfirmation.class);
                intent.putExtra("username",""+username);
                startActivity(intent);


            } else {


                //showErrorMessage();
            }
        }
    }

    public class CheckMembershipTask extends AsyncTask<URL, Void, String> {

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

            if (githubSearchResults.equals("yes")) {

                //showJsonDataView();
                //mSearchResultsTextView.setText(githubSearchResults);
                amount=price1*q*0.95f;
                price.setText("Amount = "+amount);


            } else {

                amount=price1*q;
                price.setText("Amount = "+amount);
                //showErrorMessage();
            }
        }
    }


}
