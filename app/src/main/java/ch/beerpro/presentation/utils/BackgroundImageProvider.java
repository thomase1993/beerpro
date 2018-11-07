package ch.beerpro.presentation.utils;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackgroundImageProvider {

    /*static int[] backgrounds = {R.drawable.bg_beers_card_1, R.drawable.bg_beers_card_2, R.drawable.bg_beers_card_3,
            R.drawable.bg_beers_card_4, R.drawable.bg_beers_card_5, R.drawable.bg_beers_card_6,
            R.drawable.bg_beers_card_7, R.drawable.bg_beers_card_8, R.drawable.bg_beers_card_9,
            R.drawable.bg_beers_card_10, R.drawable.bg_beers_card_11, R.drawable.bg_beers_card_12,
            R.drawable.bg_beers_card_13, R.drawable.bg_beers_card_14};*/

    static List<Drawable> backgrounds = Collections.synchronizedList(new ArrayList<>());
    private Resources resources;

    public BackgroundImageProvider(Resources resources) {
        this.resources = resources;
    }

    public Task<Void> loadImages() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        List<Task<byte[]>> tasks = new ArrayList<>();

        for (int i = 1; i <= 14; ++i) {
            StorageReference gsReference = storage.getReferenceFromUrl("gs://android-bier.appspot.com/bg_beers_card_" + i + ".png");
            tasks.add(downloadDrawable(gsReference, resources));
        }

        return Tasks.whenAll(tasks);
    }

    public static Drawable getBackgroundImage(int position) {
        return backgrounds.get(position % backgrounds.size());
    }

    private Task<byte[]> downloadDrawable(StorageReference reference, Resources resources) {
        final long FIVE_MEGABYTE = 5 * 1024 * 1024;
        return reference.getBytes(FIVE_MEGABYTE)
                .addOnSuccessListener(bytes -> backgrounds.add(new BitmapDrawable(resources, new ByteArrayInputStream(bytes))))
                .addOnFailureListener(error -> error.printStackTrace());
    }
}
