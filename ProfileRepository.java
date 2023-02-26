package com.example.wagbaapplication;

import android.app.Application;
import android.os.AsyncTask;

public class ProfileRepository {

    private ProfileDao mProfileDao;
    private String mid;
    private String mname;
    private String mphonenb;

    ProfileRepository(Application application) {
        ProfileRoomDatabase db = ProfileRoomDatabase.getDatabase(application);
        mProfileDao = db.profileDao();
        mid = mProfileDao.getMid();
        mname = mProfileDao.getMname();
        mphonenb = mProfileDao.getMphonenb();
    }

    String getProfile() {
        return mid;
    }

    public void insert (Profile profile) {
        new  insertAsyncTask(mProfileDao).execute(profile);
    }

    private static class insertAsyncTask extends AsyncTask<Profile, Void, Void> {

        private ProfileDao mAsyncTaskDao;

        insertAsyncTask(ProfileDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            mAsyncTaskDao.insert(profiles[0]);
            return null;
        }
    }

}
