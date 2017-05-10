package com.medcentre.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DoctorActivity extends FragmentActivity {

	public static final String EXTRA_DOCTOR_ID = "doctorID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if (fragment == null) {
			Intent i = getIntent();
			int doctorId = i.getIntExtra(EXTRA_DOCTOR_ID, -1);

			fragment = DoctorFragment.newInstance(doctorId);

			fm.beginTransaction()
			  .add(R.id.fragmentContainer, fragment)
			  .commit();
		}
	}
}
