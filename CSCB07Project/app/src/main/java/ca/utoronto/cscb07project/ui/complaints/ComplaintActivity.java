package ca.utoronto.cscb07project.ui.complaints;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.utoronto.cscb07project.R;

public class ComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint); // Make sure you have this layout file


            // Create an instance of the ComplaintFragment
            ComplaintFragment complaintFragment = new ComplaintFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, complaintFragment).commit();
        }
    }

