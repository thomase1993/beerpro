package ch.beerpro.presentation.details;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.*;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;

import com.google.android.gms.tasks.Task;

import java.util.List;

public class DetailsViewModel extends ViewModel implements CurrentUser {

    private final MutableLiveData<String> beerId = new MutableLiveData<>();
    private final LiveData<Beer> beer;
    private final LiveData<List<Rating>> ratings;
    private final LiveData<Wish> wish;
    private MutableLiveData<String> currentUserId;

    private final LikesRepository likesRepository;
    private final WishlistRepository wishlistRepository;
    private final FridgeRepository fridgeRepository;
    private final PrivateNoteRepository privateNoteRepository;

    public DetailsViewModel() {
        // TODO We should really be injecting these!
        BeersRepository beersRepository = new BeersRepository();
        RatingsRepository ratingsRepository = new RatingsRepository();
        likesRepository = new LikesRepository();
        wishlistRepository = new WishlistRepository();
        privateNoteRepository = new PrivateNoteRepository();

        currentUserId = new MutableLiveData<>();
        beer = beersRepository.getBeer(beerId);
        wish = wishlistRepository.getMyWishForBeer(currentUserId, getBeer());
        ratings = ratingsRepository.getRatingsForBeer(beerId);
        currentUserId.setValue(getCurrentUser().getUid());
        fridgeRepository = new FridgeRepository();
    }

    public LiveData<Beer> getBeer() {
        return beer;
    }

    public LiveData<Wish> getWish() {
        return wish;
    }

    public LiveData<List<Rating>> getRatings() {
        return ratings;
    }

    public void setBeerId(String beerId) {
        this.beerId.setValue(beerId);
    }

    public void toggleLike(Rating rating) {
        likesRepository.toggleLike(rating);
    }

    public Task<Void> toggleItemInWishlist(String itemId) {
        return wishlistRepository.toggleUserWishlistItem(getCurrentUser().getUid(), itemId);
    }

    public void addBeerToFridge(View v) {
        fridgeRepository.addOrIncrementBeer(beer.getValue(), getCurrentUser().getUid());
    }

    public void updateBeerPrice(float inputPrice) {
        if (inputPrice <= 0) {return;} // Throws away really nonsensical negative values
        Beer b = beer.getValue();

        int numPrices = b.getNumPrices();
        float averagePrice = b.getAvgPrice();
        if (numPrices == 0) {
            b.setAvgPrice(inputPrice);
            b.setNumPrices(1);
            b.setMinPrice(inputPrice);
            b.setMaxPrice(inputPrice);
        }
        else {
            float newPrice = averagePrice * ((float)numPrices / (numPrices + 1f));
            newPrice = newPrice + inputPrice * (1f / ((float)numPrices + 1f));
            b.setAvgPrice(newPrice);
            b.setNumPrices(numPrices + 1);

            if(b.getMaxPrice()<inputPrice) {b.setMaxPrice(inputPrice);}
            if(b.getMinPrice()>inputPrice) {b.setMinPrice(inputPrice);}
        }

        BeersRepository.updatePrice(b);
    }

    public void updatePrivateNote(String privateNote) {
        PrivateNote pn = new PrivateNote();
        pn.setBeerId(beer.getValue().getId());
        pn.setUserId(getCurrentUser().getUid());
        pn.setNote(privateNote);
        privateNoteRepository.UpdatePrivateNote(pn);
    }

    public LiveData<PrivateNote> getNoteText() {
        return privateNoteRepository.getMyNoteForBeer(currentUserId,beer);
    }
}
