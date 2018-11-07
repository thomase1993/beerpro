package ch.beerpro.presentation.details.privateNote;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import ch.beerpro.R;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.domain.models.PrivateNote;
import ch.beerpro.presentation.details.DetailsViewModel;

public class PrivateNoteFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_private_note);
        builder.setMessage("Private Notiz bearbeiten")
                .setPositiveButton("Speichern", (dialog, id) -> {
                    EditText et = getDialog().findViewById(R.id.PrivateNoteET);
                    String note = et.getText().toString();
                    ((DetailsActivity)getActivity()).updatePrivateNote(note);
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void onStart() {
        super.onStart();
        LiveData<PrivateNote> note = ((DetailsActivity)getActivity()).getNoteText();
        note.observe(this,this::updateNote);

    }
    public void updateNote(PrivateNote note) {
        EditText et = getDialog().findViewById(R.id.PrivateNoteET);
        et.setText(note.getNote());

    }
}
