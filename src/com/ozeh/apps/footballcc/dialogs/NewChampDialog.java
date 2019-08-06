package com.ozeh.apps.footballcc.dialogs;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.extras.Tools;
import com.ozeh.apps.footballcc.interfaces.IDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class NewChampDialog extends DialogFragment {
	IDialogListener mListener;
	RadioGroup radioChampType;
	RadioButton radio;
	// et_newchamp_name
	EditText etNewChampName;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {

			mListener = (IDialogListener) activity;

		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View v = factory.inflate(R.layout.dialog_new_champ, null);

		initializeViews(v);

		addOnCheckedChangeListener(v);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.create_champ)
				.setView(v)
				.setPositiveButton(R.string.next, null)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mListener
										.onDialogNegativeClick(NewChampDialog.this);
							}
						});

		return builder.create();

	}

	private void initializeViews(View v) {
		etNewChampName = (EditText) v.findViewById(R.id.et_newchamp_name);

	}

	private void addOnCheckedChangeListener(final View v) {
		radioChampType = (RadioGroup) v.findViewById(R.id.radio_champtype);
		radioChampType
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						radio = (RadioButton) v.findViewById(checkedId);
						
						if (radio.getText().toString().equals("Groups")) {
							
						} else if (radio.getText().toString()
								.equals("Playoffs")) {
							
						}

					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		AlertDialog alertDialog = (AlertDialog) getDialog();
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etNewChampName.getText().toString().equals("")) {
					Tools.showToast("Please enter a Championship Name",
							getDialog().getContext());

				} else {
					dismiss();
					mListener.onDialogPositiveClick(NewChampDialog.this,
							etNewChampName.getText().toString());
				}
			}
		});
	}

}
