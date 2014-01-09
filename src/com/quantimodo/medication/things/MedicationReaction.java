package com.quantimodo.medication.things;

import java.util.Date;
import java.util.UUID;

public class MedicationReaction
{
	public final String id;             // Primary

	public final String medicationId;
	public String reaction;
	public long date;

	public MedicationReaction(String medicationId, String reaction, long date)
	{
		this.id = UUID.randomUUID().toString();
		this.medicationId = medicationId;
		this.reaction = reaction;
		this.date = date;
	}

	public MedicationReaction(String medicationId, String reaction, Date date)
	{
		this.id = UUID.randomUUID().toString();
		this.medicationId = medicationId;
		this.reaction = reaction;
		this.date = date.getTime();
	}

	/*
	 * Get the date on which this reaction occurred
	 */
	public Date getDate()
	{
		return new Date(date);
	}
}
