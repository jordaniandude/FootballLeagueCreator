package com.ozeh.apps.footballcc.extras;

import com.ozeh.apps.footballcc.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class Tools {
	public static void showToast(String message, Context context) {

		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.show();
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static TextView createField(String value, Context context) {
		TextView textView = new TextView(context);
		textView.setText(value);
		textView.setTextSize(15);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundResource(R.drawable.border);
		return textView;
	}

	public static TextView createFieldHeader(String value, Context context) {
		TextView textView = new TextView(context);
		textView.setText(value);
		textView.setTextSize(15);
		textView.setTypeface(null, Typeface.BOLD);
		textView.setTextColor(Color.BLACK);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundResource(R.drawable.border);
		return textView;
	}

	public static int calculatePoints(int win, int draw) {
		int winPoints = win * 3;
		int drawPoints = draw * 1;
		return winPoints + drawPoints;
	}

	public static int calculateGoalsPlusMinus(int goals_for, int goals_against) {

		return goals_for - goals_against;
	}

	public static String verifyResult(int value) {
		if (value == -1) {
			return "";
		} else {
			return Integer.toString(value);
		}
	}
}
