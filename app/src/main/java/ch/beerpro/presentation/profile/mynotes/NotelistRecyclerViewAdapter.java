package ch.beerpro.presentation.profile.mynotes;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.domain.models.Wish;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;


public class NotelistRecyclerViewAdapter extends ListAdapter<Pair<PrivateNote, Beer>, NotelistRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "NotelistRecyclerViewAda";

    private static final DiffUtil.ItemCallback<Pair<PrivateNote, Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();

    private final OnNoteListItemInteractionListener listener;

    public NotelistRecyclerViewAdapter(OnNoteListItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_notelist_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Pair<PrivateNote, Beer> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.manufacturer)
        TextView manufacturer;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        @BindView(R.id.topText)
        TextView topText;

        @BindView(R.id.bottomText)
        TextView bottomText;

        @BindView(R.id.removeFromWishlist)
        Button remove;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(PrivateNote note, Beer item, OnNoteListItemInteractionListener listener) {
            name.setText(item.getName());
            manufacturer.setText(item.getManufacturer());
            category.setText(item.getCategory());
            name.setText(item.getName());
            GlideApp.with(itemView).load(item.getPhoto()).apply(new RequestOptions().override(240, 240).centerInside())
                    .into(photo);
            ratingBar.setNumStars(5);
            ratingBar.setRating(item.getAvgRating());
            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, item.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));
            String[] text = note.getNote().split("\\n");
            topText.setText(text[0]);
            if(text.length > 1) { bottomText.setText(text[1]);}
            else {bottomText.setText("");}
            remove.setOnClickListener(v -> listener.onWishClickedListener(item));
        }

    }
}
