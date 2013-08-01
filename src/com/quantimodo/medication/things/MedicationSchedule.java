package com.quantimodo.medication.things;

import java.util.List;
import java.util.UUID;

public class MedicationSchedule
{
	public static final int TYPE_DAILY = 0;
	public static final int TYPE_WEEKLY = 1;
	public static final int TYPE_MONTHLY = 2;

	public final String id;             // Primary

	public final String medicationName;
	MedicationDose dose;    // What dose to take

	List<ScheduleReminder> reminders;

	public MedicationSchedule(String medicationName, MedicationDose dose)
	{
		this.id = UUID.randomUUID().toString();
		this.medicationName = medicationName;
		this.dose = dose;
	}
}
