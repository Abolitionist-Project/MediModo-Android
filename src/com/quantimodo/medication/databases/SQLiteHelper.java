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
		database.execSQL("CREATE TABLE Medication (" +
				"id TEXT PRIMARY KEY," +
				"name TEXT," +
				"reason TEXT," +
				"description TEXT," +
				"instructions TEXT," +
				"sideFx TEXT" +
				");");

		database.execSQL("CREATE TABLE MedicationReaction (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"reaction TEXT," +
				"date INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES Medication(id)" +
				");");

		database.execSQL("CREATE TABLE MedicationDose (" +
				"id TEXT PRIMARY KEY," +
				"medicationId TEXT," +
				"value INTEGER," +
				"unit TEXT," +
				"inventory INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES Medication(id)" +
				");");

		//TODO store medication name in case it's deleted later?
		database.execSQL("CREATE TABLE MedicationIntake (" +
				"medicationIdid TEXT PRIMARY KEY," +
				"date INTEGER," +
				"deviation INTEGER," +
				"FOREIGN KEY(medicationId) REFERENCES Medication(id)" +
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