package io.realm.examples.realmgridview;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Tudor Luca on 27/08/15.
 */
@Mapper
public abstract class RealmMapper {

    public static RealmMapper INSTANCE = Mappers.getMapper(RealmMapper.class);

    public abstract Person detachPerson(final Person person);

    @SuppressWarnings("unchecked")
    public <T extends RealmObject> RealmList<T> detachList(final RealmList<T> list) {
        final T[] array = (T[]) new RealmObject[0];
        return new RealmList<>(list.toArray(array));
    }

    public <T extends RealmObject> List<T> detachResults(final RealmResults<T> results) {
        return new CopyOnWriteArrayList<>(results);
    }
}
