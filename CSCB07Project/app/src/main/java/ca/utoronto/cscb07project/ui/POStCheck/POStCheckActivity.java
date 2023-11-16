package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.HashMap;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckCalc;

public class POStCheckActivity extends AppCompatActivity {

    private Button POStProceed;

    //private DatabaseReference grades;
    private EditText csca67, csca48, mata22, mata31, mata37;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_check);

        /**
        POSt Check PROCEED button
        POStProceed = (Button) findViewById(R.id.button6);
        POStProceed.setOnClickListener(this::toPOStCheckCalc);
         */

        //send grades to firebase
        //TO DO: store user grades under the user that is signed in
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("Tamam");

        csca67 = findViewById(R.id.editTextNumberDecimal);
        csca48 = findViewById(R.id.editTextNumberDecimal3);
        mata22 = findViewById(R.id.editTextNumberDecimal4);
        mata31 = findViewById(R.id.editTextNumberDecimal5);
        mata37 = findViewById(R.id.editTextNumberDecimal6);
        POStProceed = (Button) findViewById(R.id.button6);

        POStProceed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // turn user grades input into strings
                String a67In = csca67.getText().toString().trim();
                String a48In = csca48.getText().toString().trim();
                String a22In = mata22.getText().toString().trim();
                String a31In = mata31.getText().toString().trim();
                String a37In = mata37.getText().toString().trim();

                if(!a67In.isEmpty()){
                    double decimGrade1 = Double.parseDouble(a67In);
                    double decimGrade2 = Double.parseDouble(a48In);
                    double decimGrade3 = Double.parseDouble(a22In);
                    double decimGrade4 = Double.parseDouble(a31In);
                    double decimGrade5 = Double.parseDouble(a37In);

                    // hashmap to represent grades
                    Map<String, Object> gradesMap = new HashMap<>();
                    gradesMap.put("CSC A67 Grade", decimGrade1);
                    gradesMap.put("CSC A48 Grade", decimGrade2);
                    gradesMap.put("MAT A22 Grade", decimGrade3);
                    gradesMap.put("MAT A31 Grade", decimGrade4);
                    gradesMap.put("MAT A37 Grade", decimGrade5);

                    // create section under user
                    String key = "POSt Courses Grades"; //userRef.push().getKey();
                    userRef.child(key).setValue(gradesMap);

                    // move to results screen after clicking PROCEED
                    startActivity(new Intent(POStCheckActivity.this, POStCheckCalc.class));
                }
            }
        });

    }

    /**
    public void toPOStCheckCalc(View view){
        int viewID = view.getId();

        if (viewID == R.id.button6){
            startActivity(new Intent(this, POStCheckCalc.class));
        }
    }
     */

}