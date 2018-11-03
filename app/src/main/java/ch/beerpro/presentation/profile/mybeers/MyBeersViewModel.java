package ch.beerpro.presentation.profile.mybeers;

import android.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.*;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Fridge;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.domain.models.MyBeer;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static androidx.lifecycle.Transformations.map;
import static ch.beerpro.domain.utils.LiveDataExtensions.zip;

public class MyBeersViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "MyBeersViewModel";
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();

    private final WishlistRepository wishlistRepository;
    private final LiveData<List<MyBeer>> myFilteredBeers;

    public MyBeersViewModel() {

        wishlistRepository = new WishlistRepository();
        BeersRepository beersRepository = new BeersRepository();
        MyBeersRepository myBeersRepository = new MyBeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        FridgeRepository fridgeRepository = new FridgeRepository();

        LiveData<List<Beer>> allBeers = beersRepository.getAllBeers();
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        LiveData<List<Wish>> myWishlist = wishlistRepository.getMyWishlist(currentUserId);
        LiveData<List<Rating>> myRatings = ratingsRepository.getMyRatings(currentUserId);
        LiveData<List<Fridge>> myFridge = fridgeRepository.getMyFridge(currentUserId);

        LiveData<List<MyBeer>> myBeers = myBeersRepository.getMyBeers(allBeers, myWishlist, myRatings, myFridge);

        myFilteredBeers = map(zip(searchTerm, myBeers), MyBeersViewModel::filter);

        currentUserId.setValue(getCurrentUser().getUid());
    }

    private static List<MyBeer> filter(Pair<String, List<MyBeer>> input) {
        String searchTerm1 = input.first;
        List<MyBeer> myBeers = input.second;
        if (Strings.isNullOrEmpty(searchTerm1)) {
            return myBeers;
        }
        if (myBeers == null) {
            return Collections.emptyList();
        }
        ArrayList<MyBeer> filtered = new ArrayList<>();
        for (MyBeer beer : myBeers) {
            if (beer.getBeer().getName().toLowerCase().contains(searchTerm1.toLowerCase())) {
                filtered.add(beer);
            }
        }
        return filtered;
    }

    public LiveData<List<MyBeer>> getMyFilteredBeers() {
        return myFilteredBeers;
    }

    public void toggleItemInWishlist(String beerId) {
        wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), beerId);
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm.setValue(searchTerm);
    }
}