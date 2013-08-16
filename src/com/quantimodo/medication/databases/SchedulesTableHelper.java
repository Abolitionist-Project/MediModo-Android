package com.quantimodo.medication.databases;

import com.quantimodo.medication.Log;
import com.quantimodo.medication.things.Medication;
import com.quantimodo.medication.things.MedicationSchedule;

public class SchedulesTableHelper
{
	/*
		database.execSQL("CREATE TABLE schedules (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT" +
				"type INTEGER," +
				"interval INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES medications(id)" +
				");");
	 */
	public static final String TABLE_NAME = "schedules";

	public static void insert(SQLiteHelper helper, String medicationId, MedicationSchedule schedule)
	{
		Log.i("RUN SQL: " + "INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + schedule.id + "\",\"" + medicationId + "\",\"" + schedule.type + "\",\"" + schedule.interval + "\")");
		helper.execSQL("INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + schedule.id + "\",\"" + medicationId + "\",\"" + schedule.type + "\",\"" + schedule.interval + "\")");
}

	public static void update(SQLiteHelper helper, Medication medication)
	{
	}
}
