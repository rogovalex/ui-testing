package com.example.uitesting.ui;

import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 * <p>
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 * <p>
 * Note that only one observer is going to be notified of changes.
 *
 * Copy of https://github.com/googlesamples/android-architecture/blob/dev-todo-mvvm-live/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/SingleLiveEvent.java
 */
public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private static final String TAG = "SingleLiveEvent";

    private final AtomicBoolean mPending = new AtomicBoolean(false);
    private final Map<Observer<? super T>, Observer<? super T>> mObservers = new HashMap<>();

    @Override
    @MainThread
    public void observe(
        @NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer)
    {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.");
        }

        // Observe the internal MutableLiveData
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @Override
    @MainThread
    public void observeForever(@NonNull Observer<? super T> observer) {
        if (hasActiveObservers()) {
            Log.w(
                TAG,
                "Multiple forever observers registered but only one will be notified of changes."
            );
        }

        if (mObservers.get(observer) != null) {
            throw new IllegalArgumentException("Cannot add the same observer");
        }

        Observer<T> wrapper = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        };
        mObservers.put(observer, wrapper);

        super.observeForever(wrapper);
    }

    @Override
    @MainThread
    public void removeObserver(@NonNull Observer<? super T> observer) {
        Observer<? super T> removed = mObservers.remove(observer);

        if (removed != null) {
            super.removeObserver(removed);
        }
    }

    @MainThread
    public void setValue(@Nullable T t) {
        mPending.set(true);
        super.setValue(t);
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    public void call() {
        setValue(null);
    }

    public void post() {
        postValue(null);
    }
}
