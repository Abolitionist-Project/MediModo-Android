package com.quantimodo.medication.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{
	private SQLiteDatabase database;
	private static final String DATABASE_NAME = "Medication";
	private static final int DATABASE_VERSION = 1;

	public SQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase database)
	{
		this.database = database;
		database.execSQL("CREATE TABLE medications (" +
				"id TEXT PRIMARY KEY," +
				"name TEXT" +
				//"reason TEXT," +
				//"description TEXT," +
				//"instructions TEXT," +
				//"sideFx TEXT" +
				");");

/*		database.execSQL("CREATE TABLE MedicationReaction (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"reaction TEXT," +
				"date INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES Medication(id)" +
				");");*/

		database.execSQL("CREATE TABLE dosages (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"value INTEGER," +
				"unit TEXT," +
				"inventory INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES medications(id)" +
				");");

		//TODO store medication name in case it's deleted later?
		database.execSQL("CREATE TABLE consumptions (" +
				"timestamp INTEGER PRIMARY KEY," +
				"medicationId TEXT," +
				"doseId TEXT," +
				"deviation INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES medications(id)," +
				"FOREIGN KEY(doseId) REFERENCES dosages(id)" +
				");");

		//Table Schedule
		//ScheduleID + medicationID = PK?
		database.execSQL("CREATE TABLE schedules (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"type INTEGER," +
				"interval INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES medications(id)" +
				");");

		//Table Reminder
		//ReminderId + scheduleId + doseId = PK?
		database.execSQL("CREATE TABLE reminders (" +
				"id TEXT PRIMARY KEY," +
				"scheduleId TEXT," +
				"doseId TEXT," +
				"hour INTEGER," +
				"minute INTEGER," +
				"FOREIGN KEY(doseId) REFERENCES dosages(id)," +
				"FOREIGN KEY(scheduleId) REFERENCES schedules(id)" +
				");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
	}

	public void execSQL(String sql)
	{
		database.execSQL(sql);
	}

	public Cursor rawQuery(String sql)
	{
		return database.rawQuery(sql, null);
	}
}