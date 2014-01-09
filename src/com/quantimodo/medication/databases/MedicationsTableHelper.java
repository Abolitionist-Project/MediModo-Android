package com.quantimodo.medication.databases;

import android.database.Cursor;
import com.quantimodo.medication.util.Log;
import com.quantimodo.medication.things.Medication;

import java.util.ArrayList;

public class MedicationsTableHelper
{
	/*
		database.execSQL("CREATE TABLE medication (" +
				"id TEXT PRIMARY KEY," +
				"name TEXT" +
				//"reason TEXT," +
				//"description TEXT," +
				//"instructions TEXT," +
				//"sideFx TEXT" +
				");");
	 */
	public static final String TABLE_NAME = "medications";

	public static ArrayList<Medication> getAll(SQLiteHelper helper)
	{
		Cursor cursor = helper.rawQuery("SELECT * FROM " + TABLE_NAME);

		ArrayList<Medication> medications = new ArrayList<Medication>(cursor.getCount());
		while(cursor.moveToNext())
		{
			medications.add(new Medication(cursor.getString(0), cursor.getString(1)));
		}
		return medications;
	}
	public static void insert(SQLiteHelper helper, Medication medication)
	{
		Log.i("RUN SQL: " + "INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES  (\"" + medication.id + "\",\"" + medication.name + "\")");
		helper.execSQL("INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + medication.id + "\",\"" + medication.name + "\")");
	}
}
