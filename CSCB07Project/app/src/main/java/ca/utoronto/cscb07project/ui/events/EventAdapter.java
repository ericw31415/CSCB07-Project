package ca.utoronto.cscb07project.ui.events;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.databinding.EventItemBinding;
import androidx.fragment.app.Fragment;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public Fragment fragment;
    // Define the OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    // Define the EventViewHolder class
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        private final EventItemBinding binding;
        private Event event;

        public EventViewHolder(EventItemBinding binding, OnItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null && event != null) {
                    listener.onItemClick(event);
                }
            });
        }

        public void bind(Event event) {
            this.event = event;
            binding.textViewTitle.setText(event.getTitle());
            binding.textViewMaxUsers.setText(String.valueOf(event.getMaxUsers()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateString = format.format(event.getDate().getTime());
            binding.textViewDate.setText(dateString);
            if (event.getUsersAttending() != null) {
                String usersAttending = TextUtils.join(", ", event.getUsersAttending());
                binding.textViewUsersAttending.setText(usersAttending);
            } else {
                // Handle the case where usersAttending is null, e.g. by using an empty string
                String usersAttending = "";
                binding.textViewUsersAttending.setText(usersAttending);
            }
        }
    }

    private List<Event> eventList;
    private OnItemClickListener onItemClickListener;

    public EventAdapter(List<Event> eventList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = fragment;
    }
    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventItemBinding binding = EventItemBinding.inflate(layoutInflater, parent, false);
        return new EventViewHolder(binding, onItemClickListener);
    }
    public void showEventDetails(Event event) {
        Dialog dialog = new Dialog(fragment.getContext(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.fragment_event_details);

        // Set dialog window attributes
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.dimAmount = 0.7f; // Dim amount. 0.0 - no dim, 1.0 - completely opaque
            window.setAttributes(layoutParams);
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }


        // Set event details
        TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
        textViewTitle.setText(event.getTitle());

        TextView textViewDate = dialog.findViewById(R.id.textViewDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateString = format.format(event.getDate().getTime());
        textViewDate.setText(dateString);

        Button buttonRsvp = dialog.findViewById(R.id.buttonRsvp);
        buttonRsvp.setOnClickListener(v -> {
            // Handle RSVP click
        });

        dialog.show();
    }


    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        if (event != null) {
            holder.bind(event);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEventDetails(event);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }
}







