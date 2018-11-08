package ch.beerpro.presentation.profile.mynotes;

import android.util.Pair;

import com.google.android.gms.tasks.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.PrivateNoteRepository;
import ch.beerpro.data.repositories.WishlistRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.domain.models.Wish;

public class NotelistViewModel extends ViewModel implements CurrentUser {

    private static final String TAG = "WishlistViewModel";

    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final PrivateNoteRepository noteRepository;
    private final BeersRepository beersRepository;

    public NotelistViewModel() {
        noteRepository = new PrivateNoteRepository();
        beersRepository = new BeersRepository();

        currentUserId.setValue(getCurrentUser().getUid());
    }

    public LiveData<List<Pair<PrivateNote, Beer>>> getMyNotelistWithBeers() {
        return noteRepository.getMyNotelistWithBeers(currentUserId, beersRepository.getAllBeers());
    }

}