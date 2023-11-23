package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ca.utoronto.cscb07project.R;

public class MinPOStCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_post_check);
    }

    public void ToMinPOStCalc(View view) {
        startActivity(new Intent(this, MinPOStCalc.class));
    }
}