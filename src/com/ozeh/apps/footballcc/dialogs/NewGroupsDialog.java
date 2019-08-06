package com.ozeh.apps.footballcc.dialogs;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ozeh.apps.footballcc.R;
import com.ozeh.apps.footballcc.contracts.ChampionshipsContract;
import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.extras.Tools;
import com.ozeh.apps.footballcc.interfaces.IDialogListener;

public class NewGroupsDialog extends DialogFragment {
	IDialogListener mListener;
	Button btnAddGroup;
	TableLayout table_groups;
	EditText etGroupName;
	String championshipName;
	ArrayList<Group> listGroups = new ArrayList<Group>();
	private static int count = 0;

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

		championshipName = getArguments().getString(ChampionshipsContract.CHAMPIONSHIP_NAME);
		
		LayoutInflater factory = LayoutInflater.from(getActivity());
		final View v = factory.inflate(R.layout.dialog_new_groups, null);

		initializeViews(v);

		addButtonOnClickListener(v);

		int style = DialogFragment.STYLE_NORMAL | DialogFragment.STYLE_NO_FRAME;
		setStyle(style, R.style.ASModaListDialogStyle);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.radio_groups)
				.setView(v)
				.setPositiveButton(R.string.next, null)
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								count = 0;
								mListener
										.onDialogNegativeClick(NewGroupsDialog.this);
							}
						});

		return builder.create();

	}

	private void initializeViews(View v) {
		table_groups = (TableLayout) v.findViewById(R.id.table_groups);
		table_groups.setVisibility(View.GONE);

		etGroupName = (EditText) v.findViewById(R.id.et_group_name);

		btnAddGroup = (Button) v.findViewById(R.id.btn_addgroup);
		
	}

	private void addButtonOnClickListener(final View v) {
		btnAddGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (!etGroupName.getText().toString().equals("")) {
					count++;
					table_groups.setVisibility(View.VISIBLE);

					TableRow tr = new TableRow(v.getContext());
					tr.setLayoutParams(new TableRow.LayoutParams(
							TableRow.LayoutParams.MATCH_PARENT, 50));

					tr.addView(Tools.createField(Integer.toString(count),
							v.getContext()));
					tr.addView(Tools.createField(etGroupName.getText().toString(),
							v.getContext()));

					table_groups.addView(tr, new TableLayout.LayoutParams(
							TableLayout.LayoutParams.MATCH_PARENT, 50));

					Group group = new Group(count, etGroupName.getText()
							.toString(), "");
					listGroups.add(group);

					etGroupName.setText("");
				} else {
					Tools.showToast("Please enter a Group Name", v.getContext());
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
				if (table_groups.getVisibility() == View.VISIBLE) {
					dismiss();
					count = 0;
					mListener.onDialogPositiveClick(NewGroupsDialog.this,
							listGroups,championshipName);
				} else {
					Tools.showToast(
							"Please add at least one group to move to the next stage.",
							getDialog().getContext());
				}
			}
		});
	}

}
