package ch.beerpro.presentation.profile.myfridge;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Fridge;
import ch.beerpro.domain.models.MyBeerFromFridge;

public interface OnFridgelistItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onSaveClickedListener(Fridge fridge, String amount);
}
