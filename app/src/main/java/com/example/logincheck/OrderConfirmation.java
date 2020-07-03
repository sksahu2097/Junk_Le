package com.example.logincheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OrderConfirmation extends AppCompatActivity {

    private TextView displayUser;
    private String username;
    private Button logout;
    private TextView address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        displayUser = (TextView) findViewById(R.id.displayUser);
        logout = (Button) findViewById(R.id.logout);
        Intent intent = getIntent();
        username=intent.getStringExtra("username");
        displayUser.setText(username);
        address = (TextView) findViewById(R.id.address);



    }


    public void logout(View view){

        Intent intent = new Intent(OrderConfirmation.this,MainActivity.class);
        startActivity(intent);

    }

    public void getLocation(View view){

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .appendQueryParameter("q",address.getText().toString());
        Uri addressUri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);
        if (intent.resolveActivity(getPackageManager())!=null){


            startActivity(intent);
        }

    }



}
