package ch.beerpro.presentation.profile.mynotes;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;

public interface OnNoteListItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onWishClickedListener(Beer beer);
}
