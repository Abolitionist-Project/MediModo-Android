package com.quantimodo.medication.things;

import android.content.res.Resources;
import com.quantimodo.medication.Global;

import java.util.UUID;

public class MedicationDose
{
	public static final String UNIT_MILLIGRAM = "unit_milligram";
	public static final String UNIT_MICROGRAM = "unit_microgram";
	public static final String UNIT_GRAM = "unit_gram";
	public static final String UNIT_DROP = "unit_drop";
	public static final String UNIT_STAB = "unit_stab";
	public static final String UNIT_THING = "unit_thing";

	public static final int UNIT_MILLIGRAM_POS = 0;
	public static final int UNIT_MICROGRAM_POS = 1;
	public static final int UNIT_GRAM_POS = 2;
	public static final int UNIT_DROP_POS = 3;
	public static final int UNIT_STAB_POS = 4;
	public static final int UNIT_THING_POS = 5;

	public final String id;             // Primary

	public final String medicationId;
	public int value;
	public String unit;
	public int inventory;

	public MedicationDose(String medicationId, int value, String unit)
	{
		this.id = UUID.randomUUID().toString();
		this.medicationId = medicationId;
		this.value = value;
		this.unit = unit;
		this.inventory = -1;
	}

	public String getHumanReadableDose()
	{
		return null;
	}

	public String getHumanReadableUnit(Resources res)
	{
		return res.getString(res.getIdentifier(unit, "string", Global.packageName));
	}

	public static String positionToUnit(int position)
	{
		switch(position)
		{
		case UNIT_MILLIGRAM_POS:
			return UNIT_MILLIGRAM;
		case UNIT_MICROGRAM_POS:
			return UNIT_MICROGRAM;
		case UNIT_GRAM_POS:
			return UNIT_GRAM;
		case UNIT_DROP_POS:
			return UNIT_DROP;
		case UNIT_STAB_POS:
			return UNIT_STAB;
		case UNIT_THING_POS:
			return UNIT_THING;
		}
		return "unknown";
	}
}
