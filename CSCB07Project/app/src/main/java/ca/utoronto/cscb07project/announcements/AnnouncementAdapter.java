package ca.utoronto.cscb07project.announcements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ca.utoronto.cscb07project.R;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcements;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Announcement announcement);
    }

    public AnnouncementAdapter(Context context, int announcement_item, List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcement_item, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement currentAnnouncement = announcements.get(position);
        holder.bind(currentAnnouncement, listener);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView descriptionTextView;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.AnnouncementTitle);
            dateTextView = itemView.findViewById(R.id.AnnouncementDate);
            descriptionTextView = itemView.findViewById(R.id.AnnouncementDescription);
        }

        public void bind(final Announcement announcement, final OnItemClickListener listener) {
            titleTextView.setText(announcement.getTitle());
            dateTextView.setText(announcement.getDate());
            descriptionTextView.setText(announcement.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(announcement);
                    }
                }
            });
        }
    }
}