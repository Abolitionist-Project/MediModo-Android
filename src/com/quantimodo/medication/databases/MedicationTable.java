package com.quantimodo.medication.databases;

import android.content.Context;
import android.database.Cursor;
import com.quantimodo.medication.things.Medication;

import java.util.ArrayList;

/*
CREATE TABLE Medication (
	id TEXT PRIMARY KEY,
	name TEXT,
	reason TEXT,
	description TEXT,
	instructions TEXT,
	sideFx TEXT
);
*/

public class MedicationTable
{
	public void insert(String packageName, Medication entry)
	{
		/*database.execSQL("insert into \"" + packageName
				+ "\" values (\""
				+ entry.launchTime + "\", \"" + entry.runTime + "\");");*/
	}


	public ArrayList<Medication> getAll(Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context);

		Cursor cursor = helper.rawQuery("");

		ArrayList<Medication> medications = new ArrayList<Medication>(cursor.getCount());
		cursor.moveToFirst();
		cursor.close();

		helper.close();
		return medications;
	}


	public void update(Context context)
	{
		SQLiteHelper helper = new SQLiteHelper(context);

		Cursor cursor = helper.rawQuery("");
		cursor.moveToFirst();
		cursor.close();

		helper.close();
	}
}
