package ca.utoronto.cscb07project.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;

import ca.utoronto.cscb07project.R;
import ca.utoronto.cscb07project.ui.complaints.complaints;

public class AnnouncementAdapter extends ArrayAdapter<Announcement>{
    private OnAnnouncementClickListener onAnnouncementClickListener;

    public AnnouncementAdapter(Context context, List<Announcement> announcement){
        super(context, 0, announcement);
    }

    public interface OnAnnouncementClickListener{
        void onAnnouncementClick(Announcement announcement);
    }

    public void setOnAnnouncementClickListener(OnAnnouncementClickListener onAnnouncementClickListener){
        this.onAnnouncementClickListener = onAnnouncementClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_announcement_item, parent, false);
        }

        Announcement announcement = getItem(position);

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitleAnnouncement);
        TextView textViewDate = convertView.findViewById(R.id.textViewDateAnnouncement);
        TextView textViewComplaintId = convertView.findViewById(R.id.textViewAnnouncementId);

        if (announcement != null) {
            textViewTitle.setText("Announcement: " + announcement.getTitle());
            textViewDate.setText("Date: " + announcement.getDate());
            textViewComplaintId.setText("Announcement ID: " + announcement.getAnnouncementId());

            convertView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(onAnnouncementClickListener != null){
                        onAnnouncementClickListener.onAnnouncementClick(announcement);
                    }
                }
            });

        }
        return convertView;
    }
}
