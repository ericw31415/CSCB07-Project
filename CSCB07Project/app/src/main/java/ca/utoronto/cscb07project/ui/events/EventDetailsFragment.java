package ca.utoronto.cscb07project.ui.events;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.utoronto.cscb07project.MainActivity;
import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.FragmentEventDetailsBinding;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    private Event event;
    private SharedViewModel viewModel;

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        // Check if getArguments() is not null and contains an "event" key
        if (getArguments() != null && getArguments().containsKey("event")) {
            // Check if the associated value is not null before casting and assigning it to event
            Serializable eventSerializable = getArguments().getSerializable("event");
            if (eventSerializable instanceof Event) {
                event = (Event) eventSerializable;
            }
        }

        // Check if event is not null before using it
        if (event != null) {
            // Set the event details
            binding.textViewTitle.setText(event.getTitle());
            binding.textViewDate.setText(event.getDate());

            // Set the click listener for the RSVP button
            binding.buttonRsvp.setOnClickListener(v -> {
                // Add the user to the event's attending list
                event.addUserAttending("User"); // Replace "User" with the actual user

                viewModel.rsvpEvent(event);

                // Navigate back to the EventsFragment
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_eventDetailsFragment_to_eventsFragment);
            });
        }
        else {
            Log.e("EventDetailsFragment", "Event is null");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    /**
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
     Bundle savedInstanceState) {
     // Inflate the layout for this fragment
     return inflater.inflate(R.layout.fragment_event_details, container, false);
     }
     **/
}