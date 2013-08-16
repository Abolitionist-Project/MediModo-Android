package com.quantimodo.medication.databases;

import com.quantimodo.medication.Log;
import com.quantimodo.medication.things.MedicationReminder;

import java.util.List;

public class RemindersTableHelper
{
	/*
		database.execSQL("CREATE TABLE reminders (" +
				"id TEXT PRIMARY KEY," +
				"scheduleId TEXT" +
				"doseId TEXT" +
				"hour INTEGER," +
				"minute INTEGER," +
				"FOREIGN KEY(doseId) REFERENCES dosages(id)" +
				"FOREIGN KEY(scheduleId) REFERENCES schedules(id)" +
				");");
	 */

	public static final String TABLE_NAME = "reminders";

	public static void insert(SQLiteHelper helper, String scheduleId, MedicationReminder reminder)
	{
		Log.i("RUN SQL: " + "INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + reminder.id + "\",\"" + scheduleId + "\",\"" + reminder.dose.id + "\",\"" + reminder.hour + "\",\"" + reminder.minute + "\")");
		helper.execSQL("INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES (\"" + reminder.id + "\",\"" + scheduleId + "\",\"" + reminder.dose.id + "\",\"" + reminder.hour + "\",\"" + reminder.minute + "\")");
	}

	public static void insert(SQLiteHelper helper, String scheduleId, List<MedicationReminder> reminders)
	{
		for(MedicationReminder reminder : reminders)
		{
			insert(helper, scheduleId, reminder);
		}
	}
}
