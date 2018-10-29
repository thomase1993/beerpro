package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.FridgeRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Fridge;
import ch.beerpro.domain.models.MyBeerFromFridge;

public class FridgelistViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "FridgelistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final FridgeRepository fridgelistRepository;
    private final BeersRepository beersRepository;

    public FridgelistViewModel() {
        fridgelistRepository = new FridgeRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<Fridge, Beer>>> getContentWithBeer() {
        return fridgelistRepository.getContentWithBeer(currentUserId, beersRepository.getAllBeers());
    }

    public void updateAmountBeer(Fridge fridge, String amount) {
        fridgelistRepository.setAmount(getCurrentUser().getUid(), fridge.getBeerId(), amount);
    }
}