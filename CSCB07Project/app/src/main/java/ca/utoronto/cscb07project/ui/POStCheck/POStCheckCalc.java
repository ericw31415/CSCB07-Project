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

        boolean Req1 = getGPA.passedReq1(a67In, a48In, a22In, a31In, a37In);
        boolean Req2 = getGPA.passedReq2(a48In);
        boolean Req3 = getGPA.passedReq3(a67In, a22In, a37In);

        TextView result = findViewById(R.id.textView9);
        TextView gpaRes = findViewById(R.id.textView20);
        TextView req1Res = findViewById(R.id.textView25);
        TextView req2Res = findViewById(R.id.textView22);
        TextView req3Res = findViewById(R.id.textView24);

        result.setText(Req1 && Req2 && Req3 ? getResources().getString(R.string.MajSpec_Qualif) :
                "You do not meet the requirements for the Computer Science Major and Specialist POSt.");

        double gpaVal = getGPA.gpaCalc(a67In, a48In, a22In, a31In, a37In);
        gpaRes.setText("GPA = " + gpaVal);

        updateRequirementStatus(req1Res, Req1);
        updateRequirementStatus(req2Res, Req2);
        updateRequirementStatus(req3Res, Req3);





    }
    private void updateRequirementStatus(TextView textView, boolean requirementPassed) {
        textView.setText("Status: " + (requirementPassed ? "PASSED" : "FAILED"));
    }

}