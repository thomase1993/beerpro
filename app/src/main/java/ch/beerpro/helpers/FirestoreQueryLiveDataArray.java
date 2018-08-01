package ch.beerpro.helpers;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.beerpro.dummy.DummyContent;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.*;

import javax.annotation.Nullable;
import java.util.List;

public class FirestoreQueryLiveDataArray<T> extends LiveData<List<T>> implements ChangeEventListener {

    private static final String TAG = "FQueryLiveDataArray";

    private final ObservableSnapshotArray<T> mSnapshots;
    private final Handler handler = new Handler();
    private boolean listenerRemovePending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "removing listener to FirestoreArray");

            mSnapshots.removeChangeEventListener(FirestoreQueryLiveDataArray.this);
            listenerRemovePending = false;
        }
    };

    public FirestoreQueryLiveDataArray(Query query, Class<T> modelClass) {
        this.mSnapshots = new FirestoreArray<>(query, MetadataChanges.EXCLUDE, new ClassSnapshotParser<>(modelClass));
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else if (!mSnapshots.isListening(this)) {
            Log.i(TAG, "adding listener to FirestoreArray");
            mSnapshots.addChangeEventListener(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex,
                               int oldIndex) {

    }

    @Override
    public void onDataChanged() {
        Log.i(TAG, "data changed");
        setValue(mSnapshots);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        Log.e(TAG, "Error:", e);
    }
}