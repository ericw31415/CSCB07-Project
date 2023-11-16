package ca.utoronto.cscb07project.ui.POStCheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.POStCheck.POStCheckCalc;

public class POStCheckActivity extends AppCompatActivity {

    private Button POStProceed;

    //private DatabaseReference grades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_check);

        //POSt Check PROCEED button
        POStProceed = (Button) findViewById(R.id.button6);
        POStProceed.setOnClickListener(this::toPOStCheckCalc);
    }

    public void toPOStCheckCalc(View view){
        int viewID = view.getId();

        if (viewID == R.id.button6){
            startActivity(new Intent(this, POStCheckCalc.class));
        }
    }

}