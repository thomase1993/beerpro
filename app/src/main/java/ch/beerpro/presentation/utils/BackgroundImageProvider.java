package ch.beerpro.presentation.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import ch.beerpro.R;

import java.util.Arrays;

public class BackgroundImageProvider {

    static int[] backgrounds = {R.drawable.bg_beers_card_1, R.drawable.bg_beers_card_2, R.drawable.bg_beers_card_3,
            R.drawable.bg_beers_card_4, R.drawable.bg_beers_card_5, R.drawable.bg_beers_card_6,
            R.drawable.bg_beers_card_7, R.drawable.bg_beers_card_8, R.drawable.bg_beers_card_9,
            R.drawable.bg_beers_card_10, R.drawable.bg_beers_card_11, R.drawable.bg_beers_card_12,
            R.drawable.bg_beers_card_13, R.drawable.bg_beers_card_14};

    static {
        Arrays.sort(backgrounds);
    }

    public static Drawable getBackgroundImage(Context res, int position) {
        return res.getDrawable(backgrounds[position % backgrounds.length]);
    }
}
