package com.quantimodo.medication.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.quantimodo.medication.R;
import com.quantimodo.medication.Utils;
import com.quantimodo.medication.things.MedicationSchedule;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.*;

public class SetScheduleDialog
{
	private Context context;

	private AlertDialog alert;
	private View view;

	private CheckBox[] cbDays = new CheckBox[8];
	private Spinner spScheduleType;

	private boolean ignoreCheckBoxChange = false;

	public void show(final Context context)
	{
		this.context = context;
		this.view = LayoutInflater.from(context).inflate(R.layout.dialog_setschedule, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(true);
		builder.setTitle(R.string.managemedication_schedule);

		builder.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface dialog)
			{

			}
		});

		builder.setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});

		builder.setNegativeButton(R.string.action_discard, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});

		initSpinner();
		initCheckBoxes();

		alert = builder.create();
		alert.show();
	}

	private void initSpinner()
	{
		ScheduleTypeSpinnerAdapter spinnerAdapter = new ScheduleTypeSpinnerAdapter(context);

		spScheduleType = (Spinner) view.findViewById(R.id.spScheduleType);
		spScheduleType.setAdapter(spinnerAdapter);
		spScheduleType.setOnItemSelectedListener(onScheduleTypeSelectedListener);
	}

	private void initCheckBoxes()
	{
		cbDays[0] = (CheckBox) view.findViewById(R.id.cbEveryDay);
		cbDays[1] = (CheckBox) view.findViewById(R.id.cbMonday);
		cbDays[2] = (CheckBox) view.findViewById(R.id.cbTuesday);
		cbDays[3] = (CheckBox) view.findViewById(R.id.cbWednesday);
		cbDays[4] = (CheckBox) view.findViewById(R.id.cbThursday);
		cbDays[5] = (CheckBox) view.findViewById(R.id.cbFriday);
		cbDays[6] = (CheckBox) view.findViewById(R.id.cbSaturday);
		cbDays[7] = (CheckBox) view.findViewById(R.id.cbSunday);

		cbDays[0].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[1].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[2].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[3].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[4].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[5].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[6].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[7].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
	}

	AdapterView.OnItemSelectedListener onScheduleTypeSelectedListener = new AdapterView.OnItemSelectedListener()
	{
		@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			ignoreCheckBoxChange = true;
			switch(position)
			{
			case MedicationSchedule.TYPE_DAILY:
				for(CheckBox checkBox : cbDays)
				{
					checkBox.setChecked(true);
				}
				break;
			case MedicationSchedule.TYPE_WEEKLY:
				cbDays[0].setChecked(false);
				break;
			}

			ignoreCheckBoxChange = false;
		}

		@Override public void onNothingSelected(AdapterView<?> parent)
		{
		}
	};

	CompoundButton.OnCheckedChangeListener onCbDaysCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
	{
		@Override public void onCheckedChanged(CompoundButton compoundButton, boolean checked)
		{
			if(ignoreCheckBoxChange)
			{
				return;
			}

			if(compoundButton.equals(cbDays[0]))
			{
				if(checked)
				{
					spScheduleType.setSelection(MedicationSchedule.TYPE_DAILY);
				}
				else
				{
					spScheduleType.setSelection(MedicationSchedule.TYPE_WEEKLY);
				}
			}
		}
	};

	class ScheduleTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;
		int preferredHeight;

		String[] strings;

		public ScheduleTypeSpinnerAdapter(Context context)
		{
			super(context, R.layout.sherlock_spinner_dropdown_item);

			this.inflater = LayoutInflater.from(context);
			this.preferredHeight = Utils.convertDpToPixel(48, context.getResources());
			this.strings = context.getResources().getStringArray(R.array.scheduleTypes);
		}

		@Override
		public int getCount()
		{
			return strings.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);

			view.setText(strings[position]);

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);
			view.setBackgroundResource(R.color.card_background);
			view.setHeight(preferredHeight);

			view.setText(strings[position]);

			return view;
		}
	}
}
