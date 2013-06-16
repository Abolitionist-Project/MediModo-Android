package com.quantimodo.medication.things;

import java.util.List;
import java.util.UUID;

public class MedicationSchedule
{
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
