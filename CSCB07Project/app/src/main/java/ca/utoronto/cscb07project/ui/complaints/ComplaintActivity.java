package ca.utoronto.cscb07project.ui.complaints;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.utoronto.cscb07project.R;

public class ComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);


            ComplaintFragment complaintFragment = new ComplaintFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, complaintFragment).commit();
        }
    }

