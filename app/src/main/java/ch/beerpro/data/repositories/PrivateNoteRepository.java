package ch.beerpro.data.repositories;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import lombok.NonNull;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class PrivateNoteRepository {
    public void UpdatePrivateNote(PrivateNote note) {

        note.setId(note.generateId(note.getUserId(), note.getBeerId()));
        DocumentReference document = FirebaseFirestore.getInstance().collection(PrivateNote.COLLECTION)
                .document(note.getId());
        document.set(note);
    }

    private static LiveData<List<PrivateNote>> getNotesByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(PrivateNote.COLLECTION)
                .orderBy(PrivateNote.FIELD_CREATED, Query.Direction.DESCENDING).whereEqualTo(PrivateNote.FIELD_USER_ID, userId),
                PrivateNote.class);
    }

    private static LiveData<PrivateNote> getUserNoteListFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(PrivateNote.COLLECTION)
                .document(PrivateNote.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, PrivateNote.class);
    }

    public LiveData<List<Pair<PrivateNote, Beer>>> getMyNotelistWithBeers(LiveData<String> currentUserId,
                                                                   LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyNotelist(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<PrivateNote> notes = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<PrivateNote, Beer>> result = new ArrayList<>();
            for (PrivateNote note : notes) {
                Beer beer = beersById.get(note.getBeerId());
                result.add(Pair.create(note, beer));
            }
            return result;
        });
    }

    public LiveData<List<PrivateNote>> getMyNotelist(LiveData<String> currentUserId) {
        return switchMap(currentUserId, PrivateNoteRepository::getNotesByUser);
    }


    public LiveData<PrivateNote> getMyNoteForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {


        return switchMap(combineLatest(currentUserId, beer), PrivateNoteRepository::getUserNoteListFor);
    }


}

