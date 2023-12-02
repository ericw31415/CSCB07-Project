package ca.utoronto.cscb07project.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.utoronto.cscb07project.R;

public class RSVPEventAdapter extends RecyclerView.Adapter<RSVPEventAdapter.RSVPEventViewHolder> {

    private List<Event> rsvpEvents;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public RSVPEventAdapter(Context context, List<Event> rsvpEvents) {
        this.rsvpEvents = rsvpEvents;
    }

    @NonNull
    @Override
    public RSVPEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rsvp_event_item, parent, false);
        return new RSVPEventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RSVPEventViewHolder holder, int position) {
        Event currentEvent = rsvpEvents.get(position);
        holder.bind(currentEvent, listener);
    }

    @Override
    public int getItemCount() {
        return rsvpEvents.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class RSVPEventViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView locationTextView;
        private TextView dateTimeTextView;

        public RSVPEventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dateTimeTextView = itemView.findViewById(R.id.dateTimeTextView);
        }

        public void bind(final Event event, final OnItemClickListener listener) {
            titleTextView.setText(event.getTitle());
            locationTextView.setText(event.getLocation());
            dateTimeTextView.setText(event.getDateTime());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(event);
                    }
                }
            });
        }
    }
}
