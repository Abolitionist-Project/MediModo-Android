package com.quantimodo.medication.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ViewFlipper;
import com.quantimodo.medication.R;
import com.quantimodo.medication.things.MedicationSchedule;
import com.quantimodo.medication.util.ConvertUtils;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

public class SetScheduleDialog
{
	private Context context;

	private MedicationSchedule currentSchedule; // Do -not- modify this from here unless saving
	private OnScheduleEditedListener listener;  // Listener that'll report back on save

	private View view;
	private ViewFlipper viewFlipper;
	private TextView spScheduleTypeSelected;
	private Spinner spScheduleType;
	private Spinner spScheduleInterval;

	String[] scheduleTypes;
	String[] scheduleTypesPlural;

	private ArrayList<Integer> selectedCalendarDays;
	private ArrayList<Integer> selectedWeekDays;

	public interface OnScheduleEditedListener
	{
		public void onEdited(MedicationSchedule newSchedule);
	}

	public void show(final Context context, MedicationSchedule originalSchedule, OnScheduleEditedListener listener)
	{
		this.context = context;
		this.listener = listener;
		this.currentSchedule = originalSchedule;

		if(currentSchedule.type == MedicationSchedule.TYPE_WEEKLY)
		{
			selectedWeekDays = new ArrayList<Integer>(originalSchedule.enabledDays.size());
			for(int enabledDay : originalSchedule.enabledDays)
			{
				selectedWeekDays.add(enabledDay);
			}
		}
		else if(currentSchedule.type == MedicationSchedule.TYPE_MONTHLY)
		{
			selectedCalendarDays = new ArrayList<Integer>(originalSchedule.enabledDays.size());
			for(int enabledDay : originalSchedule.enabledDays)
			{
				selectedCalendarDays.add(enabledDay);
			}
		}

		this.view = LayoutInflater.from(context).inflate(R.layout.dialog_setschedule, null);
		this.viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		this.viewFlipper.setDisplayedChild(1);
		this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
		this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right));

		initScheduleSpinners();
		initCheckBoxes();
		initCalendar();

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(false);
		builder.setInverseBackgroundForced(true);
		builder.setTitle(R.string.setschedule_title);

		builder.setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				currentSchedule.type = spScheduleType.getSelectedItemPosition();
				currentSchedule.interval = spScheduleInterval.getSelectedItemPosition() + 1;

				currentSchedule.enabledDays.clear();
				switch(currentSchedule.type)
				{
				case MedicationSchedule.TYPE_DAILY:
					break;
				case MedicationSchedule.TYPE_WEEKLY:
					currentSchedule.enabledDays = new ArrayList<Integer>(selectedWeekDays.size());
					for(int enabledDay : selectedWeekDays)
					{
						currentSchedule.enabledDays.add(enabledDay);
					}
					break;
				case MedicationSchedule.TYPE_MONTHLY:
					currentSchedule.enabledDays = new ArrayList<Integer>(selectedCalendarDays.size());
					for(int enabledDay : selectedCalendarDays)
					{
						currentSchedule.enabledDays.add(enabledDay);
					}
					break;
				}

				SetScheduleDialog.this.listener.onEdited(SetScheduleDialog.this.currentSchedule);
			}
		});

		builder.setNegativeButton(R.string.action_discard, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});

		AlertDialog alert = builder.create();
		//alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.show();
	}

	private void initScheduleSpinners()
	{
		String[] intervals = new String[99];
		for (int i = 1; i < 100; i++)
		{
			intervals[i - 1] = String.valueOf(i);
		}
		ScheduleIntervalSpinnerAdapter spScheduleIntervalAdapter = new ScheduleIntervalSpinnerAdapter(context, intervals);
		spScheduleInterval = (Spinner) view.findViewById(R.id.spScheduleInterval);
		spScheduleInterval.setAdapter(spScheduleIntervalAdapter);
		if(currentSchedule.interval < 1)
		{
			spScheduleInterval.setSelection(0);
		}
		else
		{
			spScheduleInterval.setSelection(currentSchedule.interval - 1);
		}
		spScheduleInterval.setOnItemSelectedListener(onScheduleIntervalSelectedListener);

		ScheduleTypeSpinnerAdapter spScheduleTypeAdapter = new ScheduleTypeSpinnerAdapter(context, currentSchedule.type);
		spScheduleType = (Spinner) view.findViewById(R.id.spScheduleType);
		spScheduleType.setAdapter(spScheduleTypeAdapter);
		spScheduleType.setSelection(currentSchedule.type);
		spScheduleType.setOnItemSelectedListener(onScheduleTypeSelectedListener);
	}

	private void initCheckBoxes()
	{
		CheckBox[] cbDays = new CheckBox[7];

		cbDays[0] = (CheckBox) view.findViewById(R.id.cbMonday);
		cbDays[1] = (CheckBox) view.findViewById(R.id.cbTuesday);
		cbDays[2] = (CheckBox) view.findViewById(R.id.cbWednesday);
		cbDays[3] = (CheckBox) view.findViewById(R.id.cbThursday);
		cbDays[4] = (CheckBox) view.findViewById(R.id.cbFriday);
		cbDays[5] = (CheckBox) view.findViewById(R.id.cbSaturday);
		cbDays[6] = (CheckBox) view.findViewById(R.id.cbSunday);

		if(selectedWeekDays == null)
		{
			selectedWeekDays = new ArrayList<Integer>(7);
			selectedWeekDays.add(Calendar.MONDAY);
			selectedWeekDays.add(Calendar.TUESDAY);
			selectedWeekDays.add(Calendar.WEDNESDAY);
			selectedWeekDays.add(Calendar.THURSDAY);
			selectedWeekDays.add(Calendar.FRIDAY);
			selectedWeekDays.add(Calendar.SATURDAY);
			selectedWeekDays.add(Calendar.SUNDAY);
		}

		cbDays[0].setChecked(selectedWeekDays.contains(Calendar.MONDAY));
		cbDays[1].setChecked(selectedWeekDays.contains(Calendar.TUESDAY));
		cbDays[2].setChecked(selectedWeekDays.contains(Calendar.WEDNESDAY));
		cbDays[3].setChecked(selectedWeekDays.contains(Calendar.THURSDAY));
		cbDays[4].setChecked(selectedWeekDays.contains(Calendar.FRIDAY));
		cbDays[5].setChecked(selectedWeekDays.contains(Calendar.SATURDAY));
		cbDays[6].setChecked(selectedWeekDays.contains(Calendar.SUNDAY));

		cbDays[0].setTag(Calendar.MONDAY);
		cbDays[1].setTag(Calendar.TUESDAY);
		cbDays[2].setTag(Calendar.WEDNESDAY);
		cbDays[3].setTag(Calendar.THURSDAY);
		cbDays[4].setTag(Calendar.FRIDAY);
		cbDays[5].setTag(Calendar.SATURDAY);
		cbDays[6].setTag(Calendar.SUNDAY);

		cbDays[0].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[1].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[2].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[3].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[4].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[5].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[6].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
	}

	private void initCalendar()
	{
		if(selectedCalendarDays == null)
		{
			selectedCalendarDays = new ArrayList<Integer>();
		}


		TableLayout tableLayout = (TableLayout) view.findViewById(R.id.vwMonthly);
		int totalViews = 0;

		int buttonDimens = ConvertUtils.dpToPx(40, context.getResources());
		TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(buttonDimens, buttonDimens);
		TableRow.LayoutParams spacerViewParams = new TableRow.LayoutParams(0, 1, 1);

		for (int rowNum = 0; rowNum < 5; rowNum++)
		{
			ViewGroup currentGroup = (ViewGroup) tableLayout.getChildAt(rowNum);
			for (int viewNum = 0; viewNum < 13; viewNum++)
			{
				if (viewNum % 2 == 0)
				{
					totalViews++;
					if (totalViews > 31)
					{
						View view = new View(context);
						TableRow.LayoutParams params = new TableRow.LayoutParams(buttonDimens, buttonDimens);
						params.column = viewNum;
						currentGroup.addView(view);
					}
					else
					{
						Button newButton = new Button(context);
						/*TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(buttonDimens, buttonDimens);
						params.column = viewNum;*/

						//newButton.setLayoutParams(params);
						newButton.setLayoutParams(buttonLayoutParams);
						newButton.setText(String.valueOf(totalViews));
						newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
						newButton.setOnClickListener(onCalendarDayClickedListener);

						if (selectedCalendarDays.contains(totalViews))
						{
							newButton.setTextColor(context.getResources().getColor(android.R.color.white));
							newButton.setBackgroundResource(R.drawable.selector_reddotbg);
						}
						else
						{
							newButton.setTextColor(context.getResources().getColor(android.R.color.black));
							newButton.setBackgroundResource(R.drawable.selector);
						}

						currentGroup.addView(newButton);
					}
				}
				else
				{
					View view = new View(context);
					view.setLayoutParams(spacerViewParams);
					currentGroup.addView(view);
				}
			}
		}
	}

	AdapterView.OnItemSelectedListener onScheduleTypeSelectedListener = new AdapterView.OnItemSelectedListener()
	{
		@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			if (viewFlipper.getDisplayedChild() != position)
			{
				viewFlipper.setDisplayedChild(position);

				if (spScheduleInterval.getSelectedItemPosition() == 0)
				{
					spScheduleTypeSelected.setText(scheduleTypes[position]);
				}
				else
				{
					spScheduleTypeSelected.setText(scheduleTypesPlural[position]);
				}
				new Handler().postDelayed(new Runnable()
				{
					@Override public void run()
					{
						System.gc();
					}
				}, 1000);
			}
		}
		@Override public void onNothingSelected(AdapterView<?> parent)
		{
		}
	};

	AdapterView.OnItemSelectedListener onScheduleIntervalSelectedListener = new AdapterView.OnItemSelectedListener()
	{
		@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			if (position == 0)
			{
				spScheduleTypeSelected.setText(scheduleTypes[spScheduleType.getSelectedItemPosition()]);
			}
			else
			{
				spScheduleTypeSelected.setText(scheduleTypesPlural[spScheduleType.getSelectedItemPosition()]);
			}
		}

		@Override public void onNothingSelected(AdapterView<?> parent)
		{
		}
	};

	private CompoundButton.OnCheckedChangeListener onCbDaysCheckedChangeListener = new CompoundButton.OnCheckedChangeListener()
	{
		@SuppressWarnings("SuspiciousMethodCalls")  // Hide warning about Object cast required for removal of specific day
		@Override public void onCheckedChanged(CompoundButton compoundButton, boolean b)
		{
			int dayOfWeek = (Integer) compoundButton.getTag();
			if(selectedWeekDays.contains(dayOfWeek))
			{
				selectedWeekDays.remove((Object) dayOfWeek);
			}
			else
			{
				selectedWeekDays.add(dayOfWeek);
			}
		}
	};

	View.OnClickListener onCalendarDayClickedListener = new View.OnClickListener()
	{
		@SuppressWarnings("SuspiciousMethodCalls")
		@Override
		public void onClick(View view)
		{
			Button button = (Button) view;
			int date = Integer.parseInt(button.getText().toString());
			if (selectedCalendarDays.contains(date))
			{
				view.setBackgroundResource(R.drawable.selector);
				((Button) view).setTextColor(context.getResources().getColor(android.R.color.black));
				selectedCalendarDays.remove((Object) date);
			}
			else
			{
				view.setBackgroundResource(R.drawable.selector_reddotbg);
				((Button) view).setTextColor(context.getResources().getColor(android.R.color.white));
				selectedCalendarDays.add(date);
			}
		}

	};

	class ScheduleTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;
		int preferredHeight;

		public ScheduleTypeSpinnerAdapter(Context context, int position)
		{
			super(context, R.layout.sherlock_spinner_dropdown_item);

			this.inflater = LayoutInflater.from(context);
			this.preferredHeight = ConvertUtils.dpToPx(48, context.getResources());

			Resources res = context.getResources();
			if (scheduleTypes == null || scheduleTypesPlural == null)    // Cache strings to prevent choppy animations
			{
				scheduleTypes = new String[3];
				scheduleTypesPlural = new String[3];
				scheduleTypes[0] = res.getQuantityString(R.plurals.scheduletype_daily, 1);
				scheduleTypesPlural[0] = res.getQuantityString(R.plurals.scheduletype_daily, 2);
				scheduleTypes[1] = res.getQuantityString(R.plurals.scheduletype_weekly, 1);
				scheduleTypesPlural[1] = res.getQuantityString(R.plurals.scheduletype_weekly, 2);
				scheduleTypes[2] = res.getQuantityString(R.plurals.scheduletype_monthly, 1);
				scheduleTypesPlural[2] = res.getQuantityString(R.plurals.scheduletype_monthly, 2);
			}
			spScheduleTypeSelected = new TextView(context);
			spScheduleTypeSelected.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			spScheduleTypeSelected.setPadding(ConvertUtils.dpToPx(8, context.getResources()), 0, 0, 0);
			spScheduleTypeSelected.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			spScheduleTypeSelected.setTextColor(res.getColor(R.color.card_title));
			if (spScheduleInterval.getSelectedItemPosition() == 0)
			{
				spScheduleTypeSelected.setText(scheduleTypes[position]);
			}
			else
			{
				spScheduleTypeSelected.setText(scheduleTypesPlural[position]);
			}
		}

		@Override public int getCount()
		{
			return scheduleTypes.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return spScheduleTypeSelected;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			View view = inflater.inflate(R.layout.dialog_setschedule_types, null);
			TextView tvScheduleType = (TextView) view.findViewById(R.id.tvScheduleType);
			if (spScheduleInterval.getSelectedItemPosition() == 0)
			{
				tvScheduleType.setText(scheduleTypes[position]);
			}
			else
			{
				tvScheduleType.setText(scheduleTypesPlural[position]);
			}
			return view;
		}
	}


	class ScheduleIntervalSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;

		public ScheduleIntervalSpinnerAdapter(Context context, String[] strings)
		{
			super(context, R.layout.sherlock_spinner_dropdown_item, strings);

			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView view = new TextView(context);
			view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			view.setTextColor(context.getResources().getColor(R.color.card_title));
			view.setPadding(ConvertUtils.dpToPx(8, context.getResources()), 0, 0, 0);
			if (position == 0)
			{
				view.setText("Every");
			}
			else
			{
				view.setText("Every " + getItem(position));
			}

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			View view = inflater.inflate(R.layout.dialog_setschedule_types, null);
			TextView tvScheduleType = (TextView) view.findViewById(R.id.tvScheduleType);
			tvScheduleType.setText(getItem(position));

			return view;
		}
	}
}
