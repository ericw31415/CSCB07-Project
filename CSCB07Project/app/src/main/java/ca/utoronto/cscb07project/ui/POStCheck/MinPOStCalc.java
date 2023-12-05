package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ca.utoronto.cscb07project.R;

public class MinPOStCalc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_post_calc);

        boolean req1Checked = getIntent().getBooleanExtra("REQ1_CHECKED", false);
        boolean a08Checked = getIntent().getBooleanExtra("A08_CHECKED", false);
        boolean a48Checked = getIntent().getBooleanExtra("A48_CHECKED", false);
        boolean Check3 = getIntent().getBooleanExtra("3_CHECKED", false);

        TextView result = findViewById(R.id.textView28);
        TextView pass1 = findViewById(R.id.textView32);
        TextView pass2 = findViewById(R.id.textView33);

        if(req1Checked && a08Checked && a48Checked && Check3){
            result.setText("You currently qualify for the Computer Science Minor POSt!");
            pass1.setText("Status: PASSED");
            pass2.setText("Status: PASSED");
        }
        else{
            result.setText("You do not meet the requirements for the Computer Science Minor POSt.");
            if(req1Checked){
                pass1.setText("Status: PASSED");
            }
            else{
                pass1.setText("Status: FAILED");
            }
            if(!(a08Checked && a48Checked && Check3)){
                pass2.setText("Status: FAILED");
            }
            else{
                pass2.setText("Status: PASSED");
            }
        }
    }

}