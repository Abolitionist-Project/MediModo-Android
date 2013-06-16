package com.quantimodo.medication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.quantimodo.sdk.QuantimodoClient;

public class Global
{
	public static QuantimodoClient quantimodo;

	public static String packageName;

	// Preferences
	public static int moodInterval;

	public static void init(Context context)
	{
		quantimodo = QuantimodoClient.getInstance();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		Global.packageName = context.getPackageName();

		// Preferences
		Global.moodInterval = Integer.valueOf(prefs.getString("moodInterval", "1"));
	}
}