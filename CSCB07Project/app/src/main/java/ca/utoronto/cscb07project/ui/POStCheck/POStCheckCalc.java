package ca.utoronto.cscb07project.ui.POStCheck;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class POStCheckCalc extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_check_calc);

        double a67In = getIntent().getDoubleExtra("a67In", 0.0);
        double a48In = getIntent().getDoubleExtra("a48In", 0.0);
        double a22In = getIntent().getDoubleExtra("a22In", 0.0);
        double a31In = getIntent().getDoubleExtra("a31In", 0.0);
        double a37In = getIntent().getDoubleExtra("a37In", 0.0);

        // call requirement checking functions with user input grades
        // TO DO: store GPA for users on firebase
        boolean Req1 = getGPA.passedReq1(a67In, a48In, a22In, a31In, a37In);
        boolean Req2 = getGPA.passedReq2(a48In);
        boolean Req3 = getGPA.passedReq3(a67In, a22In, a37In);

        if(Req1 && Req2 && Req3){
            TextView result = findViewById(R.id.textView9);
            result.setText(getResources().getString(R.string.MajSpec_Qualified));

            TextView gpaRes = findViewById(R.id.textView20);
            double gpaVal = getGPA.gpaCalc(a67In, a48In, a22In, a31In, a37In);
            gpaRes.setText("GPA = " + gpaVal);

            TextView req1Res = findViewById(R.id.textView25);
            req1Res.setText("Status: PASSED");

            TextView req2Res = findViewById(R.id.textView22);
            req2Res.setText("Status: PASSED");

            TextView req3Res = findViewById(R.id.textView24);
            req3Res.setText("Status: PASSED");
        }
        else{
            TextView result = findViewById(R.id.textView9);
            result.setText("You do not meet the requirements for the Computer Science Major and " +
                    "Specialist POSt.");

            TextView gpaRes = findViewById(R.id.textView20);
            double gpaVal = getGPA.gpaCalc(a67In, a48In, a22In, a31In, a37In);
            gpaRes.setText("GPA = " + gpaVal);


            if(!Req1){
                TextView req1Res = findViewById(R.id.textView25);
                req1Res.setText("Status: FAILED");
            }
            else{
                TextView req1Res = findViewById(R.id.textView25);
                req1Res.setText("Status: PASSED");
            }

            if(!Req2){
                TextView req2Res = findViewById(R.id.textView22);
                req2Res.setText("Status: FAILED");
            }
            else{
                TextView req2Res = findViewById(R.id.textView22);
                req2Res.setText("Status: PASSED");
            }

            if(!Req3){
                TextView req3Res = findViewById(R.id.textView24);
                req3Res.setText("Status: FAILED");
            }
            else{
                TextView req3Res = findViewById(R.id.textView24);
                req3Res.setText("Status: PASSED");
            }
        }
    }

}