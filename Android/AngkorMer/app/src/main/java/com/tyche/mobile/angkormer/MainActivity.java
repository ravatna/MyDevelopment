package com.tyche.mobile.angkormer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private EditText edtPrice;
    Button btnGenQr;
    Button btnOne,btnTwo,btnThree,btnFour,btnFive,btnSix,btnSeven,btnEight,btnNine,btnZero,btnPoint,btnPlus,btnMinus,btnMultiply,btnDiv,btnEquals;



    private ImageView imgDel;
    private int mStarter;
    private TextView txvStarter;
    private TextView txvLogSpace;
    private String tempOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnGenQr = (Button) findViewById(R.id.btnGenerateQr);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        imgDel = (ImageView)findViewById(R.id.imgDelNumber);

        txvStarter = (TextView)findViewById(R.id.txvStarter);
        txvLogSpace = (TextView)findViewById(R.id.txvLogSpace);

        btnOne = (Button) findViewById(R.id.btnOne);
        btnTwo = (Button) findViewById(R.id.btnTwo);
        btnThree = (Button) findViewById(R.id.btnThree);
        btnFour = (Button) findViewById(R.id.btnFour);
        btnFive = (Button) findViewById(R.id.btnFive);
        btnSix = (Button) findViewById(R.id.btnSix);
        btnSeven = (Button) findViewById(R.id.btnSeven);
        btnEight = (Button) findViewById(R.id.btnEight);
        btnNine = (Button) findViewById(R.id.btnNine);
        btnZero = (Button) findViewById(R.id.btnZero);
        btnPoint = (Button) findViewById(R.id.btnPoint);
        btnEquals = (Button) findViewById(R.id.btnEquals);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        btnMultiply = (Button) findViewById(R.id.btnMultiple);
        btnDiv = (Button) findViewById(R.id.btnDiv);




        btnPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!edtPrice.getText().toString().contains(".")){
                    appendText(".");

                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    tempOperation = "+";
                    //appendText("+");
                    updateStarter();
                    edtPrice.setText("");

            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   tempOperation = "-";
                    //appendText("-");
                    updateStarter();
                edtPrice.setText("");

            }
        });

        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempOperation = "X";
                   //appendText("X");
                    updateStarter();
                edtPrice.setText("");
            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 tempOperation = "/";
                    //appendText("/");
                    updateStarter();
                edtPrice.setText("");
            }
        });

btnOne.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        appendText("1");
    }
});

        btnTwo .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("2");
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("3");
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("4");
            }
        });

       btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("5");
            }
        });

        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("6");
            }
        });

        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("7");
            }
        });

       btnEight .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("8");
            }
        });

        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appendText("9");
            }
        });

        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtPrice.getText().toString().length()>1) {
                    appendText("0");
                }
            }
        });



        imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPrice.setText("");
                tempOperation = "";
                mStarter = 0;
                txvStarter.setText("");
                txvLogSpace.setText("");
            }
        });

        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               calculate();
            }
        });

        btnGenQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QrTradeActivity.class);
                intent.putExtra("put_price",edtPrice.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void calculate(){



        int operater = Integer.parseInt(edtPrice.getText().toString());

        updateLogSpace(operater);

        if(tempOperation.equals("+")){
            edtPrice.setText( "" + (mStarter +operater) );
        }

        if(tempOperation.equals("-")){
            edtPrice.setText( "" + (mStarter -operater));
        }

        if(tempOperation.equals("X")){
            edtPrice.setText( "" + (mStarter *operater));
        }
        if(tempOperation.equals("/")){
            edtPrice.setText( "" + (mStarter /operater));
        }


    }

    private void updateStarter() {
        mStarter = Integer.parseInt(edtPrice.getText().toString());
        txvStarter.setText(mStarter + "");
    }

    private void updateLogSpace(int operater){
        txvLogSpace.setText(txvLogSpace.getText() + "\n" + mStarter + " " + tempOperation + " " + operater);
    }


    private void appendText(String v){

        edtPrice.setText(edtPrice.getText().toString() + v);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if(-1 == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = this.getContentResolver().openInputStream(imageUri);
                   // final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //((ImageView)findViewById(R.id.imgProfile)).setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
