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

    //String a48, a67, a22, a31, a37;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_check_calc);
        /**
        DatabaseReference userGrades = FirebaseDatabase.getInstance().getReference().child("users").child("Tamam").child("POSt Courses Grades");

        userGrades.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    a48 = dataSnapshot.child("CSC A48 Grade").getValue().toString();
                    a67 = dataSnapshot.child("CSC A67 Grade").getValue().toString();
                    a22 = dataSnapshot.child("MATA A22 Grade").getValue().toString();
                    a31 = dataSnapshot.child("MATA A31 Grade").getValue().toString();
                    a37 = dataSnapshot.child("MATA A37 Grade").getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError error){
                Log.e(TAG, "Failed to read value", error.toException());
            }
        });
         */

//issue possibly here
        double a67In = getIntent().getDoubleExtra("decimGrade1", 0.0);
        double a48In = getIntent().getDoubleExtra("a48In", 0.0);
        double a22In = getIntent().getDoubleExtra("a22In", 0.0);
        double a31In = getIntent().getDoubleExtra("a31In", 0.0);
        double a37In = getIntent().getDoubleExtra("a37In", 0.0);

        /**
        double a67In = Double.parseDouble(a67);
        double a48In = Double.parseDouble(a48);
        double a22In = Double.parseDouble(a22);
        double a31In = Double.parseDouble(a31);
        double a37In = Double.parseDouble(a37);
         */

        //call requirement checking functions with user input grades
        //TO DO: store GPA for users on firebase
        boolean Req1 = getGPA.passedReq1(a67In, a48In, a22In, a31In, a37In);
        boolean Req2 = getGPA.passedReq2(a48In);
        boolean Req3 = getGPA.passedReq3(a67In, a22In, a37In);

        if(Req1 && Req2 && Req3){
            TextView result = findViewById(R.id.textView9);
            result.setText("You currently qualify for the Computer Science POSt!");

            TextView gpaRes = findViewById(R.id.textView20);
            double gpaVal = getGPA.gpaCalc(a67In, a48In, a22In, a31In, a37In);
            gpaRes.setText("GPA = " + gpaVal);

            TextView req1Res = findViewById(R.id.textView25);
            req1Res.setText("Status: PASSED");

            TextView req2Res = findViewById(R.id.textView22);
            req2Res.setText("Status: PASSED");

            TextView req3Res = findViewById(R.id.textView24);
            req3Res.setText("Status: PASSED");


            //state requirement 1 to make POSt
            //TextView POStReqs = findViewById(R.id.textView10);
            //POStReqs.setText(getString(R.string.POSt_req1));
        }
        else{
            TextView result = findViewById(R.id.textView9);
            result.setText("You do not meet the requirements for the Computer Science POSt.");

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

            //state requirement 1
            //TextView POStReqs = findViewById(R.id.textView10);
            //POStReqs.setText(getString(R.string.POSt_req1));
        }
    }

}