package com.quantimodo.medication.things;

import android.content.Context;
import android.os.Handler;
import com.quantimodo.medication.databases.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Medication
{
	public String id;

	public String name;
	//String reason;
	//String description;
	//String[] instructions;
	//String[] sideFx;

	public List<MedicationDose> dosages;       // Available dosages (also contains inventory)
	public MedicationSchedule schedule;        // Schedule (also contains reminders)

	public interface OnMedicationLoadedListener
	{
		public void onMedicationLoaded(List<Medication> loadedMedication);
	}

	public Medication()
	{
		this.id = UUID.randomUUID().toString();
		this.name = "";
		this.dosages = new ArrayList<MedicationDose>();
		this.schedule = new MedicationSchedule(MedicationSchedule.TYPE_DAILY);
	}

	public Medication(String id, String name)
	{
		this.id = id;
		this.name = name;
		this.dosages = new ArrayList<MedicationDose>();
		this.schedule = new MedicationSchedule(MedicationSchedule.TYPE_DAILY);
	}

	/*
	 * Convert a string to a string[] containing instructions for this medication
	 * @param   instructions    A comma separated list of instructions
	 */
	/*public void setInstructions(String instructions)
	{
		this.instructions = instructions.split(",");
	}*/

	/*
	 * Convert a string to a string[] containing side effects for this medication
	 * @param   instructions    A comma separated list of side effects
     */
	/*public void setSideFx(String sideFx)
	{
		this.sideFx = sideFx.split(",");
	}*/


	/*
	 *  Save this particular medication
	 */
	public void save(final Context context)
	{
		Runnable run = new Runnable()
		{
			@Override public void run()
			{
				SQLiteHelper helper = new SQLiteHelper(context);

				MedicationsTableHelper.insert(helper, Medication.this);
				DosagesTableHelper.insert(helper, Medication.this.id, Medication.this.dosages);
				SchedulesTableHelper.insert(helper, Medication.this.id, Medication.this.schedule);
				RemindersTableHelper.insert(helper, Medication.this.schedule.id, Medication.this.schedule.reminders);

				helper.close();
			}
		};
		Thread thread = new Thread(run);
		thread.setPriority(Thread.NORM_PRIORITY - 1);
		thread.start();
	}

	/*
	 *  Load all medication stored in database "Medication"
	 *  @param listener  an OnMedicationLoadedListener that'll notify the caller with loaded medication
	 */
	public static void loadAll(final Context context, final OnMedicationLoadedListener listener)
	{
		final Handler handler = new Handler();
		Runnable run = new Runnable()
		{
			@Override public void run()
			{
				SQLiteHelper helper = new SQLiteHelper(context);

				final ArrayList<Medication> medications = MedicationsTableHelper.getAll(helper);

				helper.close();

				handler.post(new Runnable()
				{
					@Override public void run()
					{
						listener.onMedicationLoaded(medications);
					}
				});
			}
		};
		Thread thread = new Thread(run);
		thread.setPriority(Thread.NORM_PRIORITY - 1);
		thread.start();
	}
}
