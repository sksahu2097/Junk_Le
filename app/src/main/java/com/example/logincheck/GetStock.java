package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

public class GetStock extends AppCompatActivity {

    private TextView dw;
    private TextView pl;
    private TextView co;
    private TextView wf;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stock);

        dw = (TextView) findViewById(R.id.Dw);
        pl = (TextView) findViewById(R.id.pl);
        co = (TextView) findViewById(R.id.co);
        wf = (TextView) findViewById(R.id.wf);
        pb = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        getStock();


    }

    public void getStock(){

        URL stock = GetStockValidate.buildUrl();
        new GetStockTask().execute(stock);

    }


    public class GetStockTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;
            String result=null;
            try {
                githubSearchResults = GetStockValidate.getResponseFromHttpUrl(searchUrl);
                JSONObject msg=new JSONObject(githubSearchResults);
                result=msg.getString("msg");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {
            pb.setVisibility(View.INVISIBLE);

            if (!githubSearchResults.equals("")) {

                //showJsonDataView();
                //mSearchResultsTextView.setText(githubSearchResults);

                //Toast.makeText(pickup_boy.this,""+,Toast.LENGTH_SHORT).show();
                String arr[]=githubSearchResults.split("\n");

                for(int i=0;i<arr.length;i++){

                    String a[]=arr[i].split(",");
                    if(a[0].equals("Domestic Waste")){

                        dw.setText(a[1]+" KG");

                    }else if(a[0].equals("Plastic")){

                        pl.setText(a[1]+" KG");

                    }else if(a[0].equals("Waste food product")){

                        co.setText(a[1]+" KG");

                    }else{

                        wf.setText(a[1]+" KG");

                    }

                }




            } else {


                //showErrorMessage();
            }
        }
    }

}
