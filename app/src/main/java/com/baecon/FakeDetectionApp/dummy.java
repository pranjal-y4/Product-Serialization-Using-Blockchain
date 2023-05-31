package com.baecon.FakeDetectionApp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class dummy extends AppCompatActivity {

    ImageView img;
    TextView tx1,tx2,tx3,tx4,tx5,tx6,btn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        getSupportActionBar().hide();

        img=findViewById(R.id.img);
        tx1=findViewById(R.id.text1);
        tx2=findViewById(R.id.text2);
        tx3=findViewById(R.id.text3);
        tx4=findViewById(R.id.text4);
        tx5=findViewById(R.id.text5);
        tx6=findViewById(R.id.text6);

        String data=getIntent().getStringExtra("result");
        System.out.println(data);
        String[] arrSplit = data.split("[|]");

        tx1.setText(arrSplit[1]);
        tx2.setText(arrSplit[2]);
        tx3.setText(arrSplit[3]);
        tx4.setText(arrSplit[4]);
        tx5.setText(arrSplit[5]);
        tx6.setText(arrSplit[6]);

        String stUrl = UrlLinks.urlserverpython +"static/ipfs/"+arrSplit[0]+".jpg";
        //String url = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(stUrl).openStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        img.setImageBitmap(bitmap);

        btn = findViewById(R.id.claim);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(arrSplit.length == 9){
                    Snackbar.make(findViewById(android.R.id.content), "Ownship is already taken buy someone.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(dummy.this);
                alertDialog.setTitle("Enter Name : ");

                final EditText input = new EditText(dummy.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.logo);

                alertDialog.setPositiveButton("Claim",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String data1 = input.getText().toString();

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                            String url = UrlLinks.claimownership;

                            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                            nameValuePairs.add(new BasicNameValuePair("name", data1));
                            nameValuePairs.add(new BasicNameValuePair("list", getIntent().getStringExtra("result")));

                            String result = null;
                            try {
                                result = jSOnClassforData.forCallingStringAndreturnSTring(url, nameValuePairs);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (result.equals("success"))
                            {
                                Snackbar.make(findViewById(android.R.id.content), "Successful !", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                Intent io = new Intent(dummy.this, MainActivity.class);
                                startActivity(io);
                            }
                            else
                            {
                                Snackbar.make(findViewById(android.R.id.content), "Something wrong", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        }
                    });

                alertDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                alertDialog.show();

                }
            }
        });

    }
}