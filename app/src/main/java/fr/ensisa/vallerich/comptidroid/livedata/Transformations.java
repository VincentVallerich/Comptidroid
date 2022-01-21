package fr.ensisa.vallerich.comptidroid.livedata;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class Transformations {
    public Transformations() {}

    public static <X, Y> MediatorLiveData<Y> map(@NonNull LiveData<X> source, @NonNull final Function<X, Y> mapFunction) {
        return (MediatorLiveData<Y>) androidx.lifecycle.Transformations.map(source, mapFunction);
    }

    public static <X, Y> MediatorLiveData<Y> switchMap(@NonNull LiveData<X> source, @NonNull final Function<X, LiveData<Y>> switchMapFunction) {
        return (MediatorLiveData<Y>) androidx.lifecycle.Transformations.switchMap(source, switchMapFunction);
    }
}
