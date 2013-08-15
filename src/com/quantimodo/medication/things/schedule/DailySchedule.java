package com.quantimodo.medication.things.schedule;

import com.quantimodo.medication.Log;

import java.util.Date;

public class DailySchedule extends MedicationSchedule
{
	public DailySchedule()
	{
		super();

		Log.i("New dailyschedule");
	}

	@Override Date getNextReminder()
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
