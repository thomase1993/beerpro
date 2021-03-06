package ch.beerpro.presentation.details;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Rating;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.presentation.details.createrating.CreateRatingActivity;
import ch.beerpro.presentation.settings.ThemeChange;
import ch.beerpro.presentation.details.price.PriceFragment;
import ch.beerpro.presentation.details.privateNote.PrivateNoteFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ch.beerpro.presentation.utils.DrawableHelpers.setDrawableTint;

public class DetailsActivity extends AppCompatActivity implements OnRatingLikedListener {

    public static final String ITEM_ID = "item_id";
    private static final String TAG = "DetailsActivity";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.photo)
    ImageView photo;

    @BindView(R.id.avgRating)
    TextView avgRating;

    @BindView(R.id.numRatings)
    TextView numRatings;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.avgPrice)
    TextView avgPrice;

    @BindView(R.id.wishlist)
    ToggleButton wishlist;

    @BindView(R.id.manufacturer)
    TextView manufacturer;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.reportError)
    Button reportError;

    private RatingsRecyclerViewAdapter adapter;

    private DetailsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeChange.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        toolbar.setTitleTextColor(Color.alpha(0));

        String beerId = getIntent().getExtras().getString(ITEM_ID);

        model = ViewModelProviders.of(this).get(DetailsViewModel.class);
        model.setBeerId(beerId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RatingsRecyclerViewAdapter(this, model.getCurrentUser());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));

        model.getBeer().observe(this, this::updateBeer);
        model.getRatings().observe(this, this::updateRatings);
        model.getWish().observe(this, this::toggleWishlistView);

        recyclerView.setAdapter(adapter);
        addRatingBar.setOnRatingBarChangeListener(this::addNewRating);
    }

    private void addNewRating(RatingBar ratingBar, float v, boolean b) {
        Intent intent = new Intent(this, CreateRatingActivity.class);
        intent.putExtra(CreateRatingActivity.ITEM, model.getBeer().getValue());
        intent.putExtra(CreateRatingActivity.RATING, v);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, addRatingBar, "rating");
        startActivity(intent, options.toBundle());
    }

    @OnClick(R.id.actionsButton)
    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.single_bottom_sheet_dialog, null);
        view.findViewById(R.id.addPrice).setOnClickListener(v -> {
            DialogFragment newFragment = new PriceFragment();
            newFragment.show(getSupportFragmentManager(), "missiles");

        });

        view.findViewById(R.id.addPrivateNote).setOnClickListener(v -> {
            DialogFragment newFragment = new PrivateNoteFragment();
            newFragment.show(getSupportFragmentManager(), "missiles");
        });

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        view.findViewById(R.id.addToFridge).setOnClickListener((x) -> {
            model.addBeerToFridge(x);
            dialog.dismiss();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void updateBeer(Beer item) {
        name.setText(item.getName());
        manufacturer.setText(item.getManufacturer());
        category.setText(item.getCategory());
        name.setText(item.getName());
        GlideApp.with(this).load(item.getPhoto()).apply(new RequestOptions().override(120, 160).centerInside())
                .into(photo);
        ratingBar.setNumStars(5);
        ratingBar.setRating(item.getAvgRating());
        avgRating.setText(getResources().getString(R.string.fmt_avg_rating, item.getAvgRating()));
        numRatings.setText(getResources().getString(R.string.fmt_ratings, item.getNumRatings()));
        toolbar.setTitle(item.getName());
        if (item.getNumPrices() == 0) { avgPrice.setText("Kein Preis Vorhanden");}
        else {
            avgPrice.setText("Preis(CHF): " + CHF(item.getMinPrice()) + " - " + CHF(item.getMaxPrice()) +
                    ", Durchschnitt: " + CHF(item.getAvgPrice()) + " aus " + item.getNumPrices());
        }
    }

    private String CHF(float input) {
        Locale ch_de = new Locale("de","ch");
        return String.format(ch_de,"%.2f",input);
    }

    private void updateRatings(List<Rating> ratings) {
        adapter.submitList(new ArrayList<>(ratings));
    }

    @Override
    public void onRatingLikedListener(Rating rating) {
        model.toggleLike(rating);
    }

    @OnClick(R.id.wishlist)
    public void onWishClickedListener(View view) {
        model.toggleItemInWishlist(model.getBeer().getValue().getId());
        /*
         * We won't get an update from firestore when the wish is removed, so we need to reset the UI state ourselves.
         * */
        if (!wishlist.isChecked()) {
            toggleWishlistView(null);
        }
    }

    private void toggleWishlistView(Wish wish) {
        if (wish != null) {
            int color = getResources().getColor(R.color.colorPrimary);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(true);
        } else {
            int color = getResources().getColor(android.R.color.darker_gray);
            setDrawableTint(wishlist, color);
            wishlist.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.reportError)
    public void sendMail() {
        composeEmail("admin@beerpro.ch", "Es hat sich ein Fehler eingeschlichen beim Bier " + name.getText(), " (Bitte Mail anpassen) \n \n"
                + "Hallo Admin, \n \n"
                + "Es ist fehlerhaft: \n"
                + "Name: " + name.getText() + "\n"
                + "Manufaktur: " + manufacturer.getText() + "\n"
                + "Kategorie: " + category.getText() + "\n"
                + "Id: " + model.getBeer().getValue().getId()
        );
    }

    public void composeEmail(String address, String subject, String mailtext) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:admin@beerpro.ch"));
        intent.putExtra(Intent.EXTRA_EMAIL, address);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, mailtext);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void updatePrice(float priceInput) {
        model.updateBeerPrice(priceInput);
    }

    public void updatePrivateNote(String note) {
        model.updatePrivateNote(note);
    }
    public LiveData<PrivateNote> getNoteText() {return model.getNoteText();}
}
