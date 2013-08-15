package com.quantimodo.medication.things;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Medication
{
	String id;

	String name;
	String reason;
	String description;
	String[] instructions;
	String[] sideFx;

	public List<MedicationDose> dosages;       // Available dosages (also contains inventory)
	public MedicationSchedule schedule;        // Schedule (also contains reminders)

	public Medication(String name)
	{
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.dosages = new ArrayList<MedicationDose>();
		this.schedule = new MedicationSchedule(MedicationSchedule.TYPE_DAILY);
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
