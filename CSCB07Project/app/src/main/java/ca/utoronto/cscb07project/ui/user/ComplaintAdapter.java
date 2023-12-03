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

public class ComplaintAdapter extends ArrayAdapter<complaints> {
    private OnComplaintClickListener onComplaintClickListener;
    public ComplaintAdapter(Context context, List<complaints> complaints) {
        super(context, 0, complaints);
    }

    public interface OnComplaintClickListener{
        void onComplaintClick(complaints complaint);
    }

    public void setOnComplaintClickListener(OnComplaintClickListener onComplaintClickListener){
        this.onComplaintClickListener = onComplaintClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.complaint_item, parent, false);
        }

        complaints complaint = getItem(position);

        TextView textViewTitle = convertView.findViewById(R.id.textViewTitle);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewComplaintId = convertView.findViewById(R.id.textViewComplaintId);
        TextView textViewUserEmail = convertView.findViewById(R.id.textViewUserEmail);

        if (complaint != null) {
            textViewTitle.setText("Complaint: " + complaint.getTitle());
            textViewDate.setText("Date: " + complaint.getDate());
            textViewComplaintId.setText("Complaint ID: " + complaint.getComplaintId());
            textViewUserEmail.setText("User Email: " + complaint.getUserEmail());

            convertView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(onComplaintClickListener != null){
                        onComplaintClickListener.onComplaintClick(complaint);
                    }
                }
            });

        }

        return convertView;
    }
}