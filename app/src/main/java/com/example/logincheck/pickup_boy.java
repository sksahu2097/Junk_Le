package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;
import android.widget.Toast;

public class pickup_boy extends AppCompatActivity {

    private Calendar calendar;
    DatePickerDialog datePickerDialog;
    private TextView showDate;
    private Spinner category;
    private TextView displayUser;
    private TextView order1;
    private String username=null;
    private String cat=null;
    private String date;
    private ProgressBar mLoadingIndicator;
    private String userid,quantity,price,pickup_address,mno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_boy);

        category = (Spinner) findViewById(R.id.category);
        displayUser = (TextView) findViewById(R.id.displayUser);
        order1 = (TextView) findViewById(R.id.order1);

        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        displayUser.setText(username);
        showDate = (TextView) findViewById(R.id.showDate);

        showDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                //Toast.makeText(SellingRequest.this,
                //      ""+ String.valueOf(category.getSelectedItem()),Toast.LENGTH_SHORT).show();

            }
        });

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);



    }

    public void chooseDate(View v){

        calendar= Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog=new DatePickerDialog(pickup_boy.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                i1++;
                showDate.setText(i+"/"+i1+"/"+i2);

            }
        },day,month,year);
        datePickerDialog.show();


    }


    public void getOrder(View view){

        date=showDate.getText().toString();
        cat=String.valueOf(category.getSelectedItem());
        cat.toLowerCase();
        URL getOrderURL = PickupboyValidate.buildUrl(cat,date);
        new PickupBoyTask().execute(getOrderURL);
        //URL getOrderURL = PickupboyValidate.buildUrl();
    }


    public void getLocation(View view){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .appendQueryParameter("q",pickup_address);
        Uri addressUri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);
        if (intent.resolveActivity(getPackageManager())!=null){


            startActivity(intent);
        }

    }


    public class PickupBoyTask extends AsyncTask<URL, Void, String> {

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
                githubSearchResults = PickupboyValidate.getResponseFromHttpUrl(searchUrl);
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

            if (!githubSearchResults.equals("")) {

                //showJsonDataView();
                //mSearchResultsTextView.setText(githubSearchResults);

                //Toast.makeText(pickup_boy.this,""+,Toast.LENGTH_SHORT).show();
                String arr[]=githubSearchResults.split(",");
                userid = arr[0];
                quantity= arr[1];
                price=arr[2];
                mno=arr[3];
                pickup_address=arr[4];

                order1.setText("name = "+userid+"\n quantity = "+quantity+"\n price = "+price+"\n phone no= "+mno+"\n pickup_address = "+pickup_address);




            } else {


                //showErrorMessage();
            }
        }
    }



}
