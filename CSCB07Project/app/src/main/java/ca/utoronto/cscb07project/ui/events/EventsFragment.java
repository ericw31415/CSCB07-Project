package ca.utoronto.cscb07project.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.DialogAddEventBinding;
import ca.utoronto.cscb07project.databinding.FragmentEventsBinding;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {
    private List<Event> eventList;
    private EventAdapter eventAdapter;
    private FragmentEventsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the toolbar title using View Binding
        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("CMS Events");
        // Initialize the event list
        eventList = new ArrayList<>();

        // Initialize the event adapter
        eventAdapter = new EventAdapter(eventList, event -> {
            // Handle event click here
            EventDetailsFragment fragment = EventDetailsFragment.newInstance(event);
            NavController navController = Navigation.findNavController(binding.getRoot());
            navController.navigate(R.id.action_navigation_events_to_eventDetailsFragment, fragment.getArguments());
        }, this);

        // Initialize your RecyclerView and set its adapter
        RecyclerView eventsRecyclerView = binding.eventsRecyclerView;
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecyclerView.setAdapter(eventAdapter);

        // Initialize the add event button and set its click listener
        // Initialize the add event button and set its click listener
        FloatingActionButton addEventButton = binding.addEventButton;
        addEventButton.setOnClickListener(v -> showAddEventDialog());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
    private void showAddEventDialog() {
        // Create a LayoutInflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate the layout for the add event dialog using View Binding
        DialogAddEventBinding dialogBinding = DialogAddEventBinding.inflate(inflater);

        // Create an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the custom layout for the AlertDialog
        builder.setView(dialogBinding.getRoot());

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Set the click listener for the submit button
        dialogBinding.submitButton.setOnClickListener(v -> {
            // Get the title and date from the EditText fields
            String title = dialogBinding.titleEditText.getText().toString();
            String date = dialogBinding.dateEditText.getText().toString();

            // Create a new Event object
            Event event = new Event(title, date);

            // Add the event to the eventList
            eventList.add(event);

            // Notify the adapter that the data set has changed
            eventAdapter.notifyDataSetChanged();

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Set the click listener for the cancel button
        dialogBinding.cancelButton.setOnClickListener(v -> dialog.dismiss());

        // Show the dialog
        dialog.show();
    }


}

