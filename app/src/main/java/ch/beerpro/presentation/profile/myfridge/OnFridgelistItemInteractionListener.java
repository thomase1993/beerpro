package ch.beerpro.presentation.profile.myfridge;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;

public interface OnFridgelistItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onSaveClickedListener(Beer beer);
}
