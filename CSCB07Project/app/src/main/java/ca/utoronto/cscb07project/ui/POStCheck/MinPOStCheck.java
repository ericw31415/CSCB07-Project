package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import ca.utoronto.cscb07project.R;

public class MinPOStCheck extends AppCompatActivity {

    public CheckBox credsComplete;
    public CheckBox a08;
    public CheckBox a48;
    public CheckBox a67;
    public CheckBox a2223;
    public CheckBox a303132;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_post_check);

        credsComplete = findViewById(R.id.checkBox);
        a08 = findViewById(R.id.checkBox2);
        a48 = findViewById(R.id.checkBox3);
        a67 = findViewById(R.id.checkBox4);
        a2223 = findViewById(R.id.checkBox5);
        a303132 = findViewById(R.id.checkBox6);
    }

    public void ToMinPOStCalc(View view) {
        //startActivity(new Intent(this, MinPOStCalc.class));
        boolean req1Checked = credsComplete.isChecked();
        boolean a08Checked = a08.isChecked();
        boolean a48Checked = a48.isChecked();
        boolean Check3 = a67.isChecked() || a2223.isChecked() || a303132.isChecked();

        Intent intent = new Intent(this, MinPOStCalc.class);
        intent.putExtra("REQ1_CHECKED", req1Checked);
        intent.putExtra("A08_CHECKED", a08Checked);
        intent.putExtra("A48_CHECKED", a48Checked);
        intent.putExtra("3_CHECKED", Check3);
        startActivity(intent);
    }
}