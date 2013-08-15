package com.quantimodo.medication.things;

import java.util.*;

public class MedicationSchedule
{
	public static final int TYPE_DAILY = 0;
	public static final int TYPE_WEEKLY = 1;
	public static final int TYPE_MONTHLY = 2;

	public final String id;
	public int type;
	public int interval;
	public ArrayList<Integer> enabledDays;      //Corresponds to enabled days of week or days of month depending on schedule type

	public List<MedicationReminder> reminders;

	public MedicationSchedule(int type)
	{
		this.id = UUID.randomUUID().toString();
		this.type = type;
		this.enabledDays = new ArrayList<Integer>();
		this.reminders = new ArrayList<MedicationReminder>();
		this.interval = 1;
	}

	public MedicationSchedule(String id, int type, int interval, List<MedicationReminder> reminders, ArrayList<Integer> enabledDays)
	{
		this.id = id;
		this.type = type;
		this.reminders = reminders;
		this.enabledDays = new ArrayList<Integer>();
		this.enabledDays.addAll(enabledDays);

		this.interval = interval;
	}

	public Date getNextReminder()
	{
		return new Date();
	}

	public String getHumanReadableDescription()
	{
		String description = "";

		switch(type)
		{
		case TYPE_DAILY:
			description = description.concat("EVERY " + interval + "DAY(S)");
			break;
		case TYPE_WEEKLY:
			description = description.concat("EVERY " + interval + " WEEK(S) ON: " + enabledDays);
			break;
		case TYPE_MONTHLY:
			description = description.concat("EVERY " + interval + " MONTH(S) ON " + enabledDays);
			break;
		}

		return description;
	}
}
