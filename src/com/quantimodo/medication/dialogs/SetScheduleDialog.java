package com.quantimodo.medication.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ViewFlipper;
import com.quantimodo.medication.R;
import com.quantimodo.medication.Utils;
import com.quantimodo.medication.things.schedule.MedicationSchedule;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.*;

import java.util.Calendar;
import java.util.HashMap;

public class SetScheduleDialog implements DialogInterface.OnDismissListener
{
	private Context context;
	private MedicationSchedule currentSchedule;
	private OnScheduleEditedListener listener;

	private View view;

	String[] scheduleTypes;
	String[] scheduleTypesPlural;

	private ViewFlipper viewFlipper;
	private CheckBox[] cbDays = new CheckBox[7];
	private TextView spScheduleTypeSelected;
	private Spinner spScheduleType;
	private Spinner spScheduleInterval;

	private HashMap<Integer, Boolean> selectedCalendarDays;


	public interface OnScheduleEditedListener
	{
		public void onEdited();
	}

	public void show(final Context context, MedicationSchedule currentSchedule, OnScheduleEditedListener listener)
	{
		this.context = context;
		this.currentSchedule = currentSchedule;
		this.listener = listener;

		this.view = LayoutInflater.from(context).inflate(R.layout.dialog_setschedule, null);
		this.viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		this.viewFlipper.setDisplayedChild(1);
		this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
		this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right));
		initScheduleSpinners();
		initCheckBoxes();
		initCalendar();

		this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
		this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right));

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(true);
		builder.setInverseBackgroundForced(true);
		builder.setTitle(R.string.setschedule_title);

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

		AlertDialog alert = builder.create();
		//alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.setOnDismissListener(this);
		alert.show();
	}

	@Override
	public void onDismiss(DialogInterface dialogInterface)
	{
		MedicationSchedule schedule;

		switch (spScheduleType.getSelectedItemPosition())
		{
		case MedicationSchedule.TYPE_DAILY:
			break;
		case MedicationSchedule.TYPE_WEEKLY:
			break;
		case MedicationSchedule.TYPE_MONTHLY:
			break;
		}
	}

	private void initScheduleSpinners()
	{
		ScheduleTypeSpinnerAdapter spScheduleTypeAdapter = new ScheduleTypeSpinnerAdapter(context);
		spScheduleType = (Spinner) view.findViewById(R.id.spScheduleType);
		spScheduleType.setAdapter(spScheduleTypeAdapter);
		spScheduleType.setSelection(1);
		spScheduleType.setOnItemSelectedListener(onScheduleTypeSelectedListener);

		String[] intervals = new String[99];
		for (int i = 1; i < 100; i++)
		{
			intervals[i - 1] = String.valueOf(i);
		}
		ScheduleIntervalSpinnerAdapter spScheduleIntervalAdapter = new ScheduleIntervalSpinnerAdapter(context, intervals);
		spScheduleInterval = (Spinner) view.findViewById(R.id.spScheduleInterval);
		spScheduleInterval.setAdapter(spScheduleIntervalAdapter);
		spScheduleInterval.setOnItemSelectedListener(onScheduleIntervalSelectedListener);
	}

	private void initCheckBoxes()
	{
		cbDays[0] = (CheckBox) view.findViewById(R.id.cbMonday);
		cbDays[1] = (CheckBox) view.findViewById(R.id.cbTuesday);
		cbDays[2] = (CheckBox) view.findViewById(R.id.cbWednesday);
		cbDays[3] = (CheckBox) view.findViewById(R.id.cbThursday);
		cbDays[4] = (CheckBox) view.findViewById(R.id.cbFriday);
		cbDays[5] = (CheckBox) view.findViewById(R.id.cbSaturday);
		cbDays[6] = (CheckBox) view.findViewById(R.id.cbSunday);
		/*cbDays[0].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[1].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[2].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[3].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[4].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[5].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);
		cbDays[6].setOnCheckedChangeListener(onCbDaysCheckedChangeListener);*/
	}

	private void initCalendar()
	{
		Calendar calendar = Calendar.getInstance();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		selectedCalendarDays = new HashMap<Integer, Boolean>();
		TableLayout tableLayout = (TableLayout) view.findViewById(R.id.vwMonthly);
		int totalViews = 0;

		int buttonDimens = Utils.convertDpToPixel(40, context.getResources());
		TableRow.LayoutParams buttonLayoutParams = new TableRow.LayoutParams(buttonDimens, buttonDimens);


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
						TableRow.LayoutParams params = new TableRow.LayoutParams(buttonDimens, buttonDimens);
						params.column = viewNum;

						newButton.setLayoutParams(params);
						newButton.setLayoutParams(buttonLayoutParams);
						newButton.setText(String.valueOf(totalViews));
						newButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
						newButton.setOnClickListener(onCalendarDayClickedListener);

						if (dayOfMonth == totalViews)
						{
							newButton.setBackgroundResource(R.drawable.selector_reddotbg);
							newButton.setTextColor(context.getResources().getColor(android.R.color.white));
							selectedCalendarDays.put(totalViews, true);
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
					TableRow.LayoutParams params = new TableRow.LayoutParams(0, 1);
					params.column = viewNum;
					params.weight = 1;
					view.setLayoutParams(params);
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
					spScheduleTypeSelected.setText(scheduleTypes[spScheduleType.getSelectedItemPosition()]);
				}
				else
				{
					spScheduleTypeSelected.setText(scheduleTypesPlural[spScheduleType.getSelectedItemPosition()]);
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

	View.OnClickListener onCalendarDayClickedListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			Button button = (Button) view;
			int buttonNumber = Integer.parseInt(button.getText().toString());
			if (selectedCalendarDays.get(buttonNumber) == null)
			{
				view.setBackgroundResource(R.drawable.selector_reddotbg);
				((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
				selectedCalendarDays.put(buttonNumber, true);
			}
			else if (selectedCalendarDays.get(buttonNumber))
			{
				view.setBackgroundResource(R.drawable.selector);
				((Button) view).setTextColor(Color.parseColor("#000000"));
				selectedCalendarDays.remove(buttonNumber);
			}
		}

	};

	class ScheduleTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;
		int preferredHeight;

		public ScheduleTypeSpinnerAdapter(Context context)
		{
			super(context, R.layout.sherlock_spinner_dropdown_item);

			this.inflater = LayoutInflater.from(context);
			this.preferredHeight = Utils.convertDpToPixel(48, context.getResources());

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
			spScheduleTypeSelected.setPadding(Utils.convertDpToPixel(8, context.getResources()), 0, 0, 0);
			spScheduleTypeSelected.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			spScheduleTypeSelected.setTextColor(res.getColor(R.color.card_title));
		}

		@Override public int getCount()
		{
			return 3;
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
			view.setPadding(Utils.convertDpToPixel(8, context.getResources()), 0, 0, 0);
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
