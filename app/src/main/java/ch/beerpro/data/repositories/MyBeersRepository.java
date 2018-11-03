package ch.beerpro.data.repositories;

import androidx.lifecycle.LiveData;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.Fridge;
import ch.beerpro.domain.models.MyBeerFromFridge;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.domain.models.MyBeer;
import ch.beerpro.domain.models.MyBeerFromRating;
import ch.beerpro.domain.models.MyBeerFromWishlist;
import ch.beerpro.domain.utils.LiveDataExtensions;

import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyBeersRepository {

    private static List<MyBeer> getMyBeers(LiveDataExtensions.Holder<List<Wish>, List<Rating>, List<Fridge>, HashMap<String, Beer>> input) {
        List<Wish> wishlist = input.a;
        List<Rating> ratings = input.b;
        List<Fridge> fridge = input.c;
        HashMap<String, Beer> beers = input.d;

        ArrayList<MyBeer> result = new ArrayList<>();
        Set<String> beersAlreadyOnTheList = new HashSet<>();
        for (Wish wish : wishlist) {
            String beerId = wish.getBeerId();
            result.add(new MyBeerFromWishlist(wish, beers.get(beerId)));
            beersAlreadyOnTheList.add(beerId);
        }

        for (Rating rating : ratings) {
            String beerId = rating.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromRating(rating, beers.get(beerId)));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }

        for (Fridge f : fridge) {
            String beerId = f.getBeerId();
            if (beersAlreadyOnTheList.contains(beerId)) {
                // if the beer is already on the wish list, don't add it again
            } else {
                result.add(new MyBeerFromFridge(beers.get(beerId), f));
                // we also don't want to see a rated beer twice
                beersAlreadyOnTheList.add(beerId);
            }
        }

        Collections.sort(result, (r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        return result;
    }


    public LiveData<List<MyBeer>> getMyBeers(LiveData<List<Beer>> allBeers, LiveData<List<Wish>> myWishlist,
                                             LiveData<List<Rating>> myRatings, LiveData<List<Fridge>> myFridge) {
        return map(combineLatest(myWishlist, myRatings, myFridge, map(allBeers, Entity::entitiesById)),
                MyBeersRepository::getMyBeers);
    }

}
