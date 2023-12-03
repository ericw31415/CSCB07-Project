package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.HashMap;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckCalc;

public class POStCheckActivity extends AppCompatActivity {

    public Button POStProceed;
    public EditText csca67, csca48, mata22, mata31, mata37;

    //Storing result on firebase
    public DatabaseReference userRef;
    public FirebaseUser currUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_check);

        /**
        // send grades to firebase
        // TO DO: store user grades under the user that is signed in
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child("Tamam");
         */
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        csca67 = findViewById(R.id.editTextNumberDecimal);
        csca48 = findViewById(R.id.editTextNumberDecimal3);
        mata22 = findViewById(R.id.editTextNumberDecimal4);
        mata31 = findViewById(R.id.editTextNumberDecimal5);
        mata37 = findViewById(R.id.editTextNumberDecimal6);
        POStProceed = (Button) findViewById(R.id.button6);

        POStProceed.setOnClickListener(v -> {
            // turn user grades input into strings
            String a67In = csca67.getText().toString().trim();
            String a48In = csca48.getText().toString().trim();
            String a22In = mata22.getText().toString().trim();
            String a31In = mata31.getText().toString().trim();
            String a37In = mata37.getText().toString().trim();

            

            if(!a67In.isEmpty() && !a48In.isEmpty() && !a22In.isEmpty() && !a31In.isEmpty() &&
                    !a37In.isEmpty()){
                double decimGrade1 = Double.parseDouble(a67In);
                double decimGrade2 = Double.parseDouble(a48In);
                double decimGrade3 = Double.parseDouble(a22In);
                double decimGrade4 = Double.parseDouble(a31In);
                double decimGrade5 = Double.parseDouble(a37In);

                //make sure user input grades are between 0 and 100
                if(isValidGrade(decimGrade1) && isValidGrade(decimGrade2)&& isValidGrade
                        (decimGrade3)&& isValidGrade(decimGrade4)&& isValidGrade(decimGrade5)){
                    // hashmap to represent grades
                    Map<String, Object> gradesMap = new HashMap<>();
                    gradesMap.put("CSC A67 Grade", decimGrade1);
                    gradesMap.put("CSC A48 Grade", decimGrade2);
                    gradesMap.put("MAT A22 Grade", decimGrade3);
                    gradesMap.put("MAT A31 Grade", decimGrade4);
                    gradesMap.put("MAT A37 Grade", decimGrade5);

                    /**
                    // create section under user
                    String key = "POSt Courses Grades"; //userRef.push().getKey();
                    userRef.child(key).setValue(gradesMap);
                     */
                    String key = "Major and Specialist POSt Courses Grades";
                    userRef.child(key).setValue(gradesMap);

                    // Display result of POStCheck on next screen
                    Intent POStRes= new Intent(POStCheckActivity.this, POStCheckCalc.class);
                    POStRes.putExtra("a67In", decimGrade1);
                    POStRes.putExtra("a48In", decimGrade2);
                    POStRes.putExtra("a22In", decimGrade3);
                    POStRes.putExtra("a31In", decimGrade4);
                    POStRes.putExtra("a37In", decimGrade5);

                    startActivity(POStRes);
                }
                else{
                    // Tell user that grade values are invalid
                    Toast.makeText(POStCheckActivity.this,
                            "The provided grades are invalid", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                // Tell user that 1 or more fields are empty
                Toast.makeText(POStCheckActivity.this,
                        "The required fields have not been filled", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean isValidGrade(double value){
        return value >= 0 && value <= 100;
    }

    /**
    public void loadGrades(String userId){
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("Major and Specialist POSt Courses Grades").child("CSC A48 Grade").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String a48Grade = snapshot.getValue(String.class);
                    csca48.setText(a48Grade);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
     */

}