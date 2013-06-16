package com.quantimodo.medication.things;

import android.content.res.Resources;
import com.quantimodo.medication.Global;

import java.util.UUID;

public class MedicationDose
{
	public static final String UNIT_MILLIGRAM = "unit_milligram";

	public final String id;             // Primary

	public final String medicationId;
	public int value;
	public String unit;
	public int inventory;

	public MedicationDose(String medicationId, int value, String unit, int inventory)
	{
		this.id = UUID.randomUUID().toString();
		this.medicationId = medicationId;
		this.value = value;
		this.unit = unit;
		this.inventory = inventory;
	}

	public String getHumanReadableDose()
	{
		return null;
	}

	public String getHumanReadableUnit(Resources res)
	{
		return res.getString(res.getIdentifier(unit, "string", Global.packageName));
	}
}
