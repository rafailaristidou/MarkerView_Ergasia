package com.example.markerview_ergasia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class popupactivity extends AppCompatActivity {
    String desc,type_metritis;
    double val_metr;
    TextView desctxt,type_metrtxt,val_metrtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupactivity);

        //Get Data From Main Activity
        Bundle bundle = getIntent().getExtras();

        //Extract the data
        desc = bundle.getString("desc");
        type_metritis = bundle.getString("type_metritis");
        val_metr=bundle.getDouble("val_metr");

        desctxt=(TextView) findViewById(R.id.dectxt);
        type_metrtxt=(TextView) findViewById(R.id.type_metrtxt);
        val_metrtxt=(TextView) findViewById(R.id.metrisistxt);

        //Emfanisi ton stixio kathe Marker
        desctxt.setText(desc);
        type_metrtxt.setText(type_metritis);
        val_metrtxt.setText(""+val_metr);




    }

    public void closepopup(View v) {// klisimo tou activity
        this.finish();
    }
}