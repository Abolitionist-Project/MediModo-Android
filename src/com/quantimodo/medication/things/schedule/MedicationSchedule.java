package com.quantimodo.medication.things.schedule;

import com.quantimodo.medication.things.MedicationReminder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

abstract public class MedicationSchedule
{
	public static final int TYPE_DAILY = 0;
	public static final int TYPE_WEEKLY = 1;
	public static final int TYPE_MONTHLY = 2;

	public final String id;
	public List<MedicationReminder> reminders;

	public MedicationSchedule()
	{
		this.id = UUID.randomUUID().toString();

		reminders = new ArrayList<MedicationReminder>();
	}

	abstract Date getNextReminder();
}
