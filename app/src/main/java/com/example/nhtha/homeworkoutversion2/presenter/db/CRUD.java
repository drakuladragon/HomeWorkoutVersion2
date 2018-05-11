package com.example.nhtha.homeworkoutversion2.presenter.db;

import android.content.Context;

import com.example.nhtha.homeworkoutversion2.model.Remin;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by nhtha on 05-Mar-18.
 */

public class CRUD {

    private Realm realm;

    public CRUD(Context context) {
        realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public RealmResults<Remin> getAll() {
        realm.beginTransaction();
        RealmResults<Remin> rs = realm.where(Remin.class).findAllSorted("id");
        realm.commitTransaction();
        return rs;
    }

    public void insert(Remin remin) {
        realm.beginTransaction();
        if (realm.where(Remin.class).findAll().size() > 0) {
            int id = realm.where(Remin.class).max("id").intValue() + 1;
            remin.setId(id);
        } else {
            remin.setId(0);
        }
        realm.insertOrUpdate(remin);
        realm.commitTransaction();
    }

    public void update(final Remin remin) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(remin);
            }
        });
    }

    public void delete(Remin remin) {
        realm.beginTransaction();
        remin.deleteFromRealm();
        realm.commitTransaction();
    }

    public Realm getRealm() {
        return realm;
    }
}
