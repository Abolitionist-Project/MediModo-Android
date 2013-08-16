package com.quantimodo.medication.databases;

import com.quantimodo.medication.Log;
import com.quantimodo.medication.things.Medication;
import com.quantimodo.medication.things.MedicationDose;

import java.util.List;

public class DosagesTableHelper
{
	/*
		database.execSQL("CREATE TABLE dosages (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"value INTEGER," +
				"unit TEXT," +
				"inventory INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES medication(id)" +
				");");
	 */

	public static final String TABLE_NAME = "dosages";

	public static void insert(SQLiteHelper helper, String medicationId, MedicationDose dose)
	{
		Log.i("RUN SQL: " + "INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + dose.id + "\",\"" + medicationId + "\",\"" + dose.value + "\",\"" + dose.unit + "\",\"" + dose.inventory + "\")");
		helper.execSQL("INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + dose.id + "\",\"" + medicationId + "\",\"" + dose.value + "\",\"" + dose.unit + "\",\"" + dose.inventory + "\")");
	}

	public static void insert(SQLiteHelper helper, String medicationId, List<MedicationDose> dosages)
	{
		for(MedicationDose dose : dosages)
		{
			insert(helper, medicationId, dose);
		}
	}

	public static void update(SQLiteHelper helper, Medication medication)
	{
		throw new IllegalArgumentException("Not yet implemented");
	}
}
