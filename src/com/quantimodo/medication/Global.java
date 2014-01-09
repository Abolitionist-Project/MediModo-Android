package com.quantimodo.medication;

import android.content.Context;
import com.quantimodo.medication.things.Medication;

import java.util.ArrayList;

public class Global
{
	public static String packageName;

	// Preferences

	public static ArrayList<Medication> medication;

	public static void init(Context context)
	{
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		Global.packageName = context.getPackageName();
	}
}