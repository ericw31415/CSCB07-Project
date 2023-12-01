package ca.utoronto.cscb07project.ui.events;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ca.utoronto.cscb07project.data.User;
import androidx.appcompat.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import ca.utoronto.cscb07project.data.User;
import ca.utoronto.cscb07project.ui.user.UserDataViewModel;

public class EventsFragment extends Fragment {
    private List<Event> allEventsList;
    private List<Event> eventList;
    private List<Event> attendingEventsList;
    private EventAdapter allEventsAdapter;
    private EventAdapter attendingEventsAdapter;
    private FragmentEventsBinding binding;
    private SharedViewModel viewModel;
    FloatingActionButton addEventButton;
    private RecyclerView attendingEventsRecyclerView;
    private TextView attendingEventsTitle;

    private UserDataViewModel userDataViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        attendingEventsTitle = binding.attendingEventsTitle;

        userDataViewModel = new ViewModelProvider(requireActivity()).get(UserDataViewModel.class);
        userDataViewModel.getIsAdmin().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdmin) {
                if (isAdmin) {
                    attendingEventsTitle.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "User is admin", Toast.LENGTH_SHORT).show();
                } else {
                    attendingEventsTitle.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "User is not admin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addEventButton = binding.addEventButton;

        attendingEventsRecyclerView = binding.attendingEventsRecyclerView;
        attendingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        attendingEventsAdapter = new EventAdapter(new ArrayList<>(), null, this);
        attendingEventsRecyclerView.setAdapter(attendingEventsAdapter);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            viewModel.getUser(userId).observe(getViewLifecycleOwner(), user -> {
                if (user != null && user.isAdmin()) {
                    addEventButton.setVisibility(View.VISIBLE);
                    attendingEventsTitle.setVisibility(View.GONE);
                } else {
                    addEventButton.setVisibility(View.GONE);
                    attendingEventsTitle.setVisibility(View.VISIBLE);
                }
            });
        } else {
            addEventButton.setVisibility(View.GONE);
            attendingEventsTitle.setVisibility(View.VISIBLE);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        allEventsAdapter = new EventAdapter(new ArrayList<>(), event -> {
            EventDetailsFragment fragment = EventDetailsFragment.newInstance(event);
            NavController navController = Navigation.findNavController(view); // Get the NavController using the view
            navController.navigate(R.id.action_navigation_events_to_navigation_event_details, fragment.getArguments());
        }, this);

        RecyclerView allEventsRecyclerView = binding.allEventsRecyclerView;
        allEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        allEventsRecyclerView.setAdapter(allEventsAdapter);

        viewModel.getAllEventsList().observe(getViewLifecycleOwner(), events -> {
            allEventsAdapter.setEventList(events);
            allEventsAdapter.notifyDataSetChanged();
            eventList = events;
        });

        FloatingActionButton addEventButton = binding.addEventButton;
        addEventButton.setOnClickListener(v -> showAddEventDialog());


    }

    private void showAddEventDialog() {
        LayoutInflater inflater = getLayoutInflater();
        DialogAddEventBinding dialogBinding = DialogAddEventBinding.inflate(inflater);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();

        dialogBinding.submitButton.setOnClickListener(v -> {
            String title = dialogBinding.titleEditText.getText().toString();
            String dateString = dialogBinding.dateEditText.getText().toString();
            if (!title.isEmpty() && !dateString.isEmpty()) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar date = Calendar.getInstance();
                try {
                    date.setTime(format.parse(dateString));
                    Event event = new Event(title, date);
                    viewModel.addEvent(event);
                    dialog.dismiss();
                } catch (ParseException e) {
                    Toast.makeText(getContext(), "Invalid date format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBinding.cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
    public List<Event> getAllEventsList() {
        return this.eventList;
    }
    public List<Event> getAttendingEventsList() {
        // You need to implement logic to filter events that the user is attending
        // For example, if each Event has a list of users attending it, you could do something like this:
        List<Event> attendingEvents = new ArrayList<>();
        for (Event event : this.eventList) {
            if (event.getUsersAttending().contains("User")) { // replace "User" with the actual user
                attendingEvents.add(event);
            }
        }
        return attendingEvents;
    }
    public EventAdapter getAllEventsAdapter() {
        return this.allEventsAdapter; // Assuming allEventsAdapter is an EventAdapter instance variable in your class
    }
    public EventAdapter getAttendingEventsAdapter() {
        return attendingEventsAdapter;
    }

    public void handleRsvp(Event event) {
        // Remove the event from the allEventsList and notify the allEventsAdapter
        allEventsList.remove(event);
        allEventsAdapter.notifyDataSetChanged();

        // Add the event to the attendingEventsList and notify the attendingEventsAdapter
        attendingEventsList.add(event);
        attendingEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}


