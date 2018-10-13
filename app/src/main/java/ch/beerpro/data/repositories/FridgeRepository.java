package ch.beerpro.data.repositories;

import android.util.Pair;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.Fridge;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {

    public void addOrIncrementBeer(Beer beer, String userId) {
        DocumentReference document = FirebaseFirestore.getInstance().collection(Fridge.COLLECTION)
                .document(Fridge.generateId(userId, beer.getId()));

        document.get().continueWith((task) -> {
            DocumentSnapshot snapshot = task.getResult();
            if (task.isSuccessful() && snapshot.exists()) {
                return document.update(Fridge.FIELD_amount, snapshot.getLong(Fridge.FIELD_amount) + 1);
            } else if (task.isSuccessful()) {
                return document.set(new Fridge(userId, "1", beer.getId()));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Fridge>> getContent(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance()
                .collection(Fridge.COLLECTION).whereEqualTo(Fridge.FIELD_USER_ID, userId),
                Fridge.class);
    }

    public LiveData<List<Pair<Fridge, Beer>>> getContentWithBeer(String currentUserId, LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getContent(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<Fridge> fridges = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<Fridge, Beer>> result = new ArrayList<>();
            for (Fridge fridge : fridges) {
                Beer beer = beersById.get(fridge.getBeerId());
                result.add(Pair.create(fridge, beer));
            }
            return result;
        });
    }

    public void removeBeer(Beer beer, String userId) {
        DocumentReference document = FirebaseFirestore.getInstance().collection(Fridge.COLLECTION)
                .document(Fridge.generateId(userId, beer.getId()));

       document.delete().continueWith(x -> {
           if(!x.isSuccessful()) {
               throw x.getException();
           }
           return 0;
       });
    }

    public void setAmount(String userid, String beerid, String amount) {
        DocumentReference document = FirebaseFirestore.getInstance().collection(Fridge.COLLECTION)
                .document(Fridge.generateId(userid, beerid));

        document.update(Fridge.FIELD_amount, amount);
    }
}
