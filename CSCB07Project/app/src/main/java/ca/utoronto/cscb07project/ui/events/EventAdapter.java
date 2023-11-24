package ca.utoronto.cscb07project.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
                if (event != null && listener != null) {
                    listener.onItemClick(event);
                }
            });
        }

        public void bind(Event event) {
            this.event = event;
            binding.textViewTitle.setText(event.getTitle());
            binding.textViewDate.setText(event.getDate());
        }
    }

    private List<Event> eventList;
    private OnItemClickListener onItemClickListener;

    public EventAdapter(List<Event> eventList, OnItemClickListener onItemClickListener, Fragment fragment) {
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
        this.fragment = fragment;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventItemBinding binding = EventItemBinding.inflate(layoutInflater, parent, false);
        return new EventViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}

