package com.quantimodo.medication.things;

import java.util.UUID;

public class ScheduleReminder
{
	public final String id;     // Primary

	final int day;
	final int hour;
	final int minute;

	public ScheduleReminder(int hour, int minute)
	{
		this.id = UUID.randomUUID().toString();
		this.day = -1;
		this.hour = hour;
		this.minute = minute;
	}

	public ScheduleReminder(int day, int hour, int minute)
	{
		this.id = UUID.randomUUID().toString();
		this.day = day;
		this.hour = hour;
		this.minute = minute;
	}
}
