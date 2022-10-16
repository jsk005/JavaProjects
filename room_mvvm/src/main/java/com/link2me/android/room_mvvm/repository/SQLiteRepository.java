package com.link2me.android.room_mvvm.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.link2me.android.room_mvvm.model.Contact_Item;
import com.link2me.android.room_mvvm.room.ContactDao;
import com.link2me.android.room_mvvm.room.ContactDatabase;

import java.util.List;

public class SQLiteRepository {
    private ContactDao contactDao;
    private LiveData<List<Contact_Item>> allContactInfo;

    public SQLiteRepository(Application application){
        ContactDatabase database = ContactDatabase.getInstance(application);
        contactDao = database.contactDao();
        allContactInfo = contactDao.getAllContactInfo();
    }

    public void insertAll(List<Contact_Item> items) {
        new InsertAllContactListAsyncTask(contactDao).execute(items);
    }

    public void update(Contact_Item item) {
        new UpdateContactInfoAsyncTask(contactDao).execute(item);
    }

    public void delete(Contact_Item item) {
        new DeleteContactInfoAsyncTask(contactDao).execute(item);
    }

    public void deleteByIdx(int idx){
        new DeleteByIdxAsyncTask(contactDao).execute(idx);
    }

    public void deleteAll() {
        new DeleteAllContactInfoAsyncTask(contactDao).execute();
    }

    public LiveData<List<Contact_Item>> getAllContactInfo() {
        return allContactInfo;
    }


    private static class InsertAllContactListAsyncTask extends AsyncTask<List<Contact_Item>, Void, Void> {
        private ContactDao contactDao;

        private InsertAllContactListAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(List<Contact_Item>... params) {
            contactDao.insertAll(params[0]);
            return null;
        }
    }

    private static class UpdateContactInfoAsyncTask extends AsyncTask<Contact_Item, Void, Void> {
        private ContactDao contactDao;

        private UpdateContactInfoAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact_Item... notes) {
            contactDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteContactInfoAsyncTask extends AsyncTask<Contact_Item, Void, Void> {
        private ContactDao contactDao;

        private DeleteContactInfoAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Contact_Item... notes) {
            contactDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteByIdxAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ContactDao contactDao;

        private DeleteByIdxAsyncTask(ContactDao contactDao){
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Integer... param) {
            contactDao.deleteByIdx(param[0]);
            return null;
        }
    }

    private static class DeleteAllContactInfoAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDao contactDao;

        private DeleteAllContactInfoAsyncTask(ContactDao contactDao) {
            this.contactDao = contactDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.deleteAll();
            return null;
        }
    }


}
