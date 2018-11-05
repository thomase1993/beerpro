package ch.beerpro.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.beerpro.domain.models.PrivateNote;

public class PrivateNoteRepository {
    public void UpdatePrivateNote(PrivateNote note) {

        note.setId(note.generateId(note.getUserId(), note.getBeerId()));
        DocumentReference document = FirebaseFirestore.getInstance().collection(PrivateNote.COLLECTION)
                .document(note.getId());
        document.set(note);
    }
}

