package com.quantimodo.medication.things;

import java.util.List;
import java.util.UUID;

public class Medication
{
	String id;                          // Primary

	String name;
	String reason;
	String description;
	String[] instructions;
	String[] sideFx;

	List<MedicationDose> dosages;       // Available dosages (also contains inventory)
	List<MedicationSchedule> schedule;  // Schedule (also contains reminders)
	List<MedicationReaction> reactions; // Reactions the user has on this medication

	public Medication(String name, List<MedicationDose> dosages)
	{
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.dosages = dosages;
	}

	/*
	 * Convert a string to a string[] containing instructions for this medication
	 * @param   instructions    A comma separated list of instructions
	 */
	public void setInstructions(String instructions)
	{
		this.instructions = instructions.split(",");
	}

	/*
	 * Convert a string to a string[] containing side effects for this medication
	 * @param   instructions    A comma separated list of side effects
     */
	public void setSideFx(String sideFx)
	{
		this.sideFx = sideFx.split(",");
	}
}
