package com.medcentre.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.medcentre.app.com.medcentre.app.http.HttpRequest;
import com.medcentre.app.com.medcentre.app.http.HttpRequestTask;
import com.medcentre.app.com.medcentre.app.http.HttpResponse;
import com.medcentre.app.com.medcentre.app.http.HttpResponseEvent;
import com.medcentre.app.com.medcentre.app.http.HttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DoctorListFragment extends ListFragment implements HttpResponseListener {
    private final String API_URL = "http://172.17.0.31/~n00134696/MedCentreWebApp/api";
    private final String TAG = "MedCentreAndroidApp";

    private List<Doctor> mDoctors;
    private DoctorListAdapter mListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String urlString = null;
        URI uri;
        HttpRequest request;
        HttpRequestTask t;

        try {
            urlString = API_URL + "/doctor";
            uri = new URI(urlString);

            request = new HttpRequest("GET", uri);
            t = new HttpRequestTask();
            t.setHttpResponseListener(this);
            t.execute(request);
        }
        catch (URISyntaxException e) {
            String errorMessage = "Error parsing uri (" + urlString + "): " + e.getMessage();
            Log.d(TAG, "HttpClient: " + errorMessage);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListAdapter != null) {
            mListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search: {
                Toast.makeText(getActivity(), "Search selected", Toast.LENGTH_LONG).show();
                return false;
            }
            case R.id.action_new: {
                Toast.makeText(getActivity(), "Add Doctor", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), DoctorActivity.class);
                startActivity(i);
                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Doctor d = (Doctor)(getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), DoctorActivity.class);
        i.putExtra(DoctorActivity.EXTRA_DOCTOR_ID, d.getId());
        startActivity(i);
    }

    public void onHttpResponse(HttpResponseEvent event) {
        JSONArray jsonArray;
        JSONObject jsonObject;
        Doctor doctor;
        HttpResponse response;

        response = event.getResponse();
        if (response != null) {
            if (response.getStatus() == 200) {
                String body = response.getBody();
                try {
                    jsonArray = new JSONArray(body);
                    mDoctors = new ArrayList<Doctor>();
                    for (int i = 0; i != jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("doctorID");
                        String firstName = jsonObject.getString("fName");
                        String lastName = jsonObject.getString("lName");
                        String phoneNumber = jsonObject.getString("phoneNumber");
                        String email = jsonObject.getString("email");
                        String area = jsonObject.getString("area");

                        doctor = new Doctor(id, firstName, lastName, phoneNumber, email, area);

                        mDoctors.add(doctor);
                    }
                }
                catch (JSONException e) {
                    String message = "Error retrieving doctors: " + e.getMessage();
                    Toast.makeText(this.getActivity(), message, Toast.LENGTH_LONG).show();
                }

                MedCentre medCentre = MedCentre.getInstance();
                medCentre.setDoctors(mDoctors);

                mListAdapter = new DoctorListAdapter(getActivity(), mDoctors);

                setListAdapter(mListAdapter);
            }
            else {
                Log.d(TAG, "Http Response: " + response.getStatus() + " " + response.getDescription());
            }
        }
    }
}
