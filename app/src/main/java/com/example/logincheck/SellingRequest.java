package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class SellingRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    private TextView showDate;
    private TextView displayUser;
    private String username;
    private EditText quantity;
    private float price1=0.0025f;
    private float amount;
    private TextView price;
    private  EditText address;
    float q;
    private ProgressBar mLoadingIndicator;
    private Spinner category;
    String cat=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_request);
        showDate = (TextView) findViewById(R.id.showDate);

        displayUser = (TextView) findViewById(R.id.displayUser);

        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        displayUser.setText(username);
        price = (TextView) findViewById(R.id.price);
        address = (EditText) findViewById(R.id.address);
        category = (Spinner) findViewById(R.id.category);
        category.setOnItemSelectedListener(this);
        //cat=String.valueOf(category.getSelectedItem());
        //cat.toLowerCase();
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        quantity = (EditText) findViewById(R.id.quantity);
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                q=Float.parseFloat(quantity.getText().toString());
                //Toast.makeText(SellingRequest.this,
                  //      ""+ String.valueOf(category.getSelectedItem()),Toast.LENGTH_SHORT).show();
                checkMembership();

            }
        });



    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

        cat = category.getItemAtPosition(position).toString();
        cat.toLowerCase();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void chooseDate(View v){

        calendar=Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog=new DatePickerDialog(SellingRequest.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                i1++;
                showDate.setText(i+"/"+i1+"/"+i2);

            }
        },day,month,year);
        datePickerDialog.show();


    }

    public void insatiateMembership(View view){

        Intent intent=new Intent(SellingRequest.this,membership.class);
        intent.putExtra("userid",""+displayUser.getText().toString());
        startActivity(intent);

    }

    public void placeOrder(View view){

        URL login_url = PlaceOrderValidate.buildUrl(username,cat,quantity.getText().toString(),""+amount,address.getText().toString(),showDate.getText().toString());
        //url_display.setText(login_url.toString());
        new PlaceOrderTask().execute(login_url);

    }



    private void checkMembership() {

        URL login_url = CheckMembershipValidation.buildUrl(username);
        //url_display.setText(login_url.toString());
        new CheckMembershipTask().execute(login_url);
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
                amount=price1*q*0.9f;
                price.setText("Amount = "+amount);


            } else {

                amount=price1*q;
                price.setText("Amount = "+amount);
                //showErrorMessage();
            }
        }
    }

    public class PlaceOrderTask extends AsyncTask<URL, Void, String> {

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
                githubSearchResults = PlaceOrderValidate.getResponseFromHttpUrl(searchUrl);
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
                Toast.makeText(SellingRequest.this,
                        "Order Placed", Toast.LENGTH_LONG).show();
                Intent intent= new Intent(SellingRequest.this,MainActivity.class);
                startActivity(intent);



            } else {


                //showErrorMessage();
            }
        }
    }





}
