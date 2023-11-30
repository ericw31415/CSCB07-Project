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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventDetailsFragment extends Fragment {
    private FragmentEventDetailsBinding binding;
    private Event event;
    private SharedViewModel viewModel;

    public static EventDetailsFragment newInstance(Event event) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("title", event.getTitle());
        args.putLong("date", event.getDate().getTimeInMillis());
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

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            long dateTimestamp = getArguments().getLong("date");
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(dateTimestamp);
            event = new Event(title, date);
        }

        if (event != null) {
            binding.textViewTitle.setText(event.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = format.format(event.getDate().getTime());
            binding.textViewDate.setText(dateString);
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();

            binding.buttonRsvp.setOnClickListener(v -> {
                viewModel.rsvpEvent(event, userEmail);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_navigation_event_details_to_navigation_events);
            });
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