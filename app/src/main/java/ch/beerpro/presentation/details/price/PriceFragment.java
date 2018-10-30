package ch.beerpro.presentation.details.price;

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
import androidx.lifecycle.ViewModelProviders;
import ch.beerpro.R;
import ch.beerpro.presentation.details.DetailsActivity;
import ch.beerpro.presentation.details.DetailsViewModel;

public class PriceFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_price);
        builder.setMessage("")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailsViewModel model = ViewModelProviders.of(getActivity()).get(DetailsViewModel.class);
                        EditText et = getDialog().findViewById(R.id.PriceInput);
                        float price = Float.parseFloat(et.getText().toString());
                        ((DetailsActivity)getActivity()).updatePrice(price);
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
