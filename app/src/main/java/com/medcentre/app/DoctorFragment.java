package com.medcentre.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.medcentre.app.com.medcentre.app.http.HttpRequest;
import com.medcentre.app.com.medcentre.app.http.HttpRequestTask;
import com.medcentre.app.com.medcentre.app.http.HttpResponse;
import com.medcentre.app.com.medcentre.app.http.HttpResponseEvent;
import com.medcentre.app.com.medcentre.app.http.HttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class DoctorFragment extends Fragment {

    private final String API_URL = "http://172.17.0.31/~n00134696/MedCentreWebApp/api";

    private static final String FRAGMENT_DOCTOR_ID = "doctorID";

    private int mDoctorId;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mPhoneNumberField;
    private EditText mEmailField;
    private EditText mAreaField;
    private Doctor mDoctor;
    private boolean mEditMode;

    public static DoctorFragment newInstance(int doctorID) {
        DoctorFragment fragment = new DoctorFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_DOCTOR_ID, doctorID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDoctorId = getArguments().getInt(FRAGMENT_DOCTOR_ID);
        }
        MedCentre store = MedCentre.getInstance();
        List<Doctor> doctors = store.getDoctors();

        mDoctor = null;
        if (mDoctorId != -1) {
            for (int i = 0; i != doctors.size(); i++) {
                Doctor d = doctors.get(i);
                if (d.getId() == mDoctorId) {
                    mDoctor = d;
                    break;
                }
            }
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_doctor, container, false);

        mFirstNameField = (EditText) fragment.findViewById(R.id.doctor_firstName_editText);
        mLastNameField = (EditText) fragment.findViewById(R.id.doctor_lastName_editText);
        mPhoneNumberField = (EditText) fragment.findViewById(R.id.doctor_phoneNumber_editText);
        mEmailField = (EditText) fragment.findViewById(R.id.doctor_email_editText);
        mAreaField = (EditText) fragment.findViewById(R.id.doctor_area_editText);

        populateForm();

        if (mDoctorId == -1 && mDoctor == null) {
            setEditMode(true);
        }
        else {
            setEditMode(false);
        }

        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.doctor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem edit = menu.findItem(R.id.action_edit);

        if (mEditMode) {
            edit.setIcon(R.drawable.ic_action_accept);
            edit.setTitle("Save");
        }
        else {
            edit.setIcon(R.drawable.ic_action_edit);
            edit.setTitle("Edit");
        }
        super.onPrepareOptionsMenu(menu);
    }

        @Override
    public  boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                if (mEditMode) {
                    Toast.makeText(getActivity(), "Save selected", Toast.LENGTH_LONG).show();

                    sendPutRequest(new PutResponseListener());
                }
                else {
                    Toast.makeText(getActivity(), "Edit selected", Toast.LENGTH_LONG).show();
                }
                setEditMode(!mEditMode);

                getActivity().invalidateOptionsMenu();
                return false;
            }
            case R.id.action_delete: {
                Toast.makeText(getActivity(), "Delete selected", Toast.LENGTH_LONG).show();

                sendDeleteRequest(new DeleteResponseListener());

                return false;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setEditMode(boolean editMode) {
        mEditMode = editMode;
        mFirstNameField.setEnabled(editMode);
        mLastNameField.setEnabled(editMode);
        mPhoneNumberField.setEnabled(editMode);
        mEmailField.setEnabled(editMode);
        mAreaField.setEnabled(editMode);
    }

    private void populateForm() {
        if (mDoctor != null) {
            mFirstNameField.setText(mDoctor.getFirstName());
            mLastNameField.setText(mDoctor.getLastName());
            mPhoneNumberField.setText(mDoctor.getPhoneNumber());
            mEmailField.setText(mDoctor.getEmail());
            mAreaField.setText(mDoctor.getArea());
        }
    }

    private JSONObject getDataAsJSONObject() throws JSONException {
        String firstName = mFirstNameField.getText().toString();
        String lastName = mLastNameField.getText().toString();
        String phoneNumber = mPhoneNumberField.getText().toString();
        String email = mEmailField.getText().toString();
        String area = mAreaField.getText().toString();

        JSONObject jsonObj = new JSONObject();
        if (mDoctorId != -1 && mDoctor != null) {
            jsonObj.put("doctorID", mDoctor.getId());
        }
        jsonObj.put("fName", firstName);
        jsonObj.put("lName", lastName);
        jsonObj.put("phoneNumber", phoneNumber);
        jsonObj.put("email", email);
        jsonObj.put("area", area);

        return jsonObj;
    }

    private void sendPutRequest(PutResponseListener listener) {
        try {
            JSONObject jsonObj = getDataAsJSONObject();
            String jsonString = jsonObj.toString();

            String urlString;
            String method;

            if (mDoctorId == -1 && mDoctor == null) {
                method = "POST";
                urlString = API_URL + "/doctor";
            }
            else {
                method = "PUT";
                urlString = API_URL + "/doctor/" + mDoctor.getId();
            }
            URI uri = new URI(urlString);
            HttpRequest request = new HttpRequest(method, uri);
            request.setBody(jsonString);

            HttpRequestTask task = new HttpRequestTask();
            task.setHttpResponseListener(listener);
            task.execute(request);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void sendDeleteRequest(DeleteResponseListener listener) {
        try {
            String urlString = API_URL + "/doctor/" +mDoctor.getId();
            URI uri = new URI(urlString);

            HttpRequest request = new HttpRequest("DELETE", uri);

            HttpRequestTask task = new HttpRequestTask();
            task.setHttpResponseListener(listener);
            task.execute(request);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private class PutResponseListener implements HttpResponseListener {
        @Override
        public void onHttpResponse(HttpResponseEvent event) {
            HttpResponse response = event.getResponse();
            String jsonString;

            try {
                jsonString = response.getBody();
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("fName")) {
                    int id = jsonObject.getInt("doctorID");
                    String firstName = jsonObject.getString("fName");
                    String lastName = jsonObject.getString("lName");
                    String phoneNumber = jsonObject.getString("phoneNumber");
                    String email = jsonObject.getString("email");
                    String area = jsonObject.getString("area");

                    if (mDoctorId == -1 && mDoctor == null) {
                        mDoctor = new Doctor(id, firstName, lastName, phoneNumber, email, area);
                        MedCentre medCentre = MedCentre.getInstance();
                        List<Doctor> doctors = medCentre.getDoctors();
                        doctors.add(mDoctor);
                    } else {
                        mDoctor.setFirstName(firstName);
                        mDoctor.setLastName(lastName);
                        mDoctor.setPhoneNumber(phoneNumber);
                        mDoctor.setEmail(email);
                        mDoctor.setArea(area);
                    }

                    populateForm();

                    Toast.makeText(getActivity(), "Doctor updated", Toast.LENGTH_LONG).show();
                }
                else if (jsonObject.has("error")) {
                    String error = jsonObject.getJSONObject("error").getString("text");
                    Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DeleteResponseListener implements HttpResponseListener {
        @Override
        public void onHttpResponse(HttpResponseEvent event) {
            HttpResponse response = event.getResponse();

            MedCentre medCentre = MedCentre.getInstance();
            List<Doctor> doctors = medCentre.getDoctors();

            doctors.remove(mDoctor);

            Toast.makeText(getActivity(), "Doctor deleted", Toast.LENGTH_LONG).show();

            getActivity().finish();
        }
    }
}
