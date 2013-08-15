package com.quantimodo.medication.things;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.UUID;

public class MedicationReminder
{
	public final String id;     // Primary

	public int hour;
	public int minute;

	public MedicationDose dose;

	public MedicationReminder(int hour, int minute, MedicationDose dose)
	{
		if(hour < 0 || hour > 23)
		{
			throw new IllegalArgumentException("\"hour\" must be positive and lower than 24");
		}
		if(minute < 0 || minute > 59)
		{
			throw new IllegalArgumentException("\"minute\" must be positive and lower than 60");
		}

		this.id = UUID.randomUUID().toString();
		this.hour = hour;
		this.minute = minute;
		this.dose = dose;
	}

	public String getHumanReadableTime(Context context)
	{
		String minuteString;
		if(minute < 10)
		{
			minuteString = "0" + minute;
		}
		else
		{
			minuteString = String.valueOf(minute);
		}
		if (!DateFormat.is24HourFormat(context))
		{
			if(hour == 12)
			{
				return hour + ":" + minuteString + " pm";
			}
			else if(hour > 12)
			{
				return (hour - 12) + ":" + minuteString + " pm";
			}
			else
			{
				return hour + ":" + minuteString + " am";
			}
		}
		else
		{
			if(hour < 10)
			{
				return "0" + hour + ":" + minuteString;
			}
			else
			{
				return hour + ":" + minuteString;
			}
		}
	}
}
