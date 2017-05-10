package com.medcentre.app;

import java.util.List;

/**
 * Created by n00134696 on 19/01/2016.
 */
public class MedCentre {

    private static MedCentre instance = null;

    public static MedCentre getInstance() {
        if (instance == null) {
            instance = new MedCentre();
        }
        return instance;
    }

    private List<Doctor> mDoctors;

    private MedCentre() {}

    public List<Doctor> getDoctors() {
        return mDoctors;
    }

    public void setDoctors(List<Doctor> mDoctors) {
        this.mDoctors = mDoctors;
    }
}
