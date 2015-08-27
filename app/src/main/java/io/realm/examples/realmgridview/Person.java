package io.realm.examples.realmgridview;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Tudor Luca on 19/08/15.
 */
public class Person extends RealmObject {

    private String name;
    private RealmList<City> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<City> getCities() {
        return cities;
    }

    public void setCities(RealmList<City> cities) {
        this.cities = cities;
    }
}
