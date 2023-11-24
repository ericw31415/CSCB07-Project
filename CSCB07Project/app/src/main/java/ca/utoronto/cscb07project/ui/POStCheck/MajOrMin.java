package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import ca.utoronto.cscb07project.R;

public class MajOrMin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maj_or_min);
    }

    public void goToPostCheck(View view) {
        startActivity(new Intent(this, POStCheckActivity.class));
    }

    public void ToMinPOStCheck(View view) {
        startActivity(new Intent(this, MinPOStCheck.class));
    }
}