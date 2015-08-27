/*
 * Copyright 2014 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.realm.examples.realmgridview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class GridViewExampleActivity extends Activity implements AdapterView.OnItemClickListener {

    private GridView mGridView;
    private CityAdapter mAdapter;
    private RealmConfiguration mRealmConfiguration;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_example);

        mRealmConfiguration = new RealmConfiguration.Builder(this).build();

        // Clear the realm from last time
        Realm.deleteRealm(mRealmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(mRealmConfiguration);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load from file "cities.json" first time
        if (mAdapter == null) {
            List<City> cities = null;
            try {
                Person person = loadCities();
                cities = person.getCities();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //This is the GridView adapter
            mAdapter = new CityAdapter(this);
            mAdapter.setData(cities);

            //This is the GridView which will display the list of cities
            mGridView = (GridView) findViewById(R.id.cities_list);
            mGridView.setAdapter(mAdapter);
            mGridView.setOnItemClickListener(GridViewExampleActivity.this);
            mAdapter.notifyDataSetChanged();
            mGridView.invalidate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private Person loadCities() throws IOException {
        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream;
        try {
            stream = getAssets().open("cities.json");
        } catch (IOException e) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        RealmMapper realmMapper = RealmMapper.INSTANCE;

        Person person = mapper.readerFor(Person.class).readValue(stream);

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        realm.beginTransaction();
        person = realm.copyToRealm(person);
        realm.commitTransaction();


        return realmMapper.detachPerson(person);
    }

    public void updateCities() {
        // Pull all the cities from the realm
        RealmResults<City> cities = realm.where(City.class).findAll();

        // Put these items in the Adapter
        mAdapter.setData(cities);
        mAdapter.notifyDataSetChanged();
        mGridView.invalidate();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City modifiedCity = (City) mAdapter.getItem(position);

        // Update the realm object affected by the user
        Realm realm = Realm.getInstance(this);

        // Acquire the list of realm cities matching the name of the clicked City.
        City city = realm.where(City.class).equalTo("name", modifiedCity.getName()).findFirst();

        // Create a transaction to increment the vote count for the selected City in the realm
        realm.beginTransaction();
        city.setVotes(city.getVotes() + 1);
        realm.commitTransaction();

        realm.close();

        updateCities();
    }
}
