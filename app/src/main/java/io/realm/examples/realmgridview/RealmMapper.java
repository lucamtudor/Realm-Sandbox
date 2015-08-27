package io.realm.examples.realmgridview;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Tudor Luca on 27/08/15.
 */
@Mapper
public abstract class RealmMapper {

    public static RealmMapper INSTANCE = Mappers.getMapper(RealmMapper.class);

    public abstract Person detachPerson(final Person person);

    @SuppressWarnings("unchecked")
    public <T extends RealmObject> RealmList<T> detachList(final RealmList<T> list) {
        final T[] array = (T[]) new RealmObject[list.size()];
        return new RealmList<>(list.toArray(array));
    }
}
