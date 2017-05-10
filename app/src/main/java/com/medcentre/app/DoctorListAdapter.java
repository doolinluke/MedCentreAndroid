package com.medcentre.app;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.medcentre.app.R;

public class DoctorListAdapter extends ArrayAdapter<Doctor>
                             implements ListAdapter {

	private Activity mContext;
	private List<Doctor> mDoctors;
	
	public DoctorListAdapter(Context context, List<Doctor> doctors) {
		super(context, R.layout.list_item_doctor, doctors);
		mContext = (Activity)context;
		mDoctors = doctors;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// If we weren't given a view, inflate one
        if (convertView == null) {
        	LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_doctor, null);
        }

        // Configure the view for this Crime
        Doctor dc = mDoctors.get(position);

        TextView titleTextView = (TextView)convertView.findViewById(
        		R.id.list_item_doctor_lastName_textView);
        titleTextView.setText(dc.getLastName());
        
        TextView yearTextView = (TextView)convertView.findViewById(
        		R.id.list_item_doctor_area_textView);
        yearTextView.setText("" + dc.getArea());
        
        TextView authorTextView = (TextView)convertView.findViewById(
        		R.id.list_item_doctor_email_textView);
        authorTextView.setText(dc.getFirstName() + " " + dc.getLastName());

        return convertView;
	}
}