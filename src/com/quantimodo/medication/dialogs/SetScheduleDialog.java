package com.quantimodo.medication.dialogs;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.ViewFlipper;
import com.quantimodo.medication.R;
import com.quantimodo.medication.Utils;
import com.quantimodo.medication.things.MedicationSchedule;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.*;

import java.util.Calendar;
import java.util.HashMap;

public class SetScheduleDialog
{
	private Context context;

	private View view;

	//private LinearLayout lnInterval;
	private ViewFlipper viewFlipper;
	private CheckBox[] cbDays = new CheckBox[8];
    private View[] btnCalendarDays = new View[31];
    private HashMap<Integer, Boolean> selectedCalendarDays;
	private Spinner spScheduleType;
    private Spinner spStartDay;

	private boolean ignoreCheckBoxChange = false;

	public void show(final Context context)
	{
		this.context = context;
		this.view = LayoutInflater.from(context).inflate(R.layout.dialog_setschedule, null);
		this.viewFlipper = (ViewFlipper) view.findViewById(R.id.viewFlipper);
		this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left));
		this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right));
		//this.lnInterval = (LinearLayout) view.findViewById(R.id.lnInterval);

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

		initScheduleSpinner();
		initCheckBoxes();
        initCalendar();
        initDaySpinner();
        initNumberPicker();

		AlertDialog alert = builder.create();
        //alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alert.show();
	}

	private void initScheduleSpinner()
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

    private void initCalendar()
    {
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        selectedCalendarDays = new HashMap<Integer, Boolean>();
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.vwCalendar);
        int totalViews = 0;
        for(int rowNum = 0; rowNum < 5; rowNum++)
        {
            ViewGroup currentGroup = (ViewGroup) tableLayout.getChildAt(rowNum);
            for(int viewNum = 0; viewNum < 7; viewNum++)
            {
                try
                {
                    View view = currentGroup.getChildAt(viewNum);
                    btnCalendarDays[totalViews] = view;
                    if(totalViews + 1 == dayOfMonth)
                    {
                        view.setBackgroundResource(R.drawable.selector_reddotbg);
                        ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
                        selectedCalendarDays.put(dayOfMonth, true);
                    }
                    btnCalendarDays[totalViews].setOnClickListener(onCalendarDayClickedListener);
                    totalViews++;
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    break;
                }
            }
        }
    }

    private void initDaySpinner()
    {
        StartDaySpinnerAdapter spinnerAdapter = new StartDaySpinnerAdapter(context);

        spStartDay = (Spinner) view.findViewById(R.id.spStartDay);
        spStartDay.setAdapter(spinnerAdapter);
    }

    private void initNumberPicker()
    {
        String[] nums = new String[31];

        for(int i=0; i<nums.length; i++)
        {
            nums[i] = Integer.toString(i+1);
        }
        NumberPicker np = (NumberPicker) view.findViewById(R.id.np);
        np.setMaxValue(nums.length);
        np.setMinValue(1);
	    np.setValue(2);
        np.setWrapSelectorWheel(false);
        np.setDisplayedValues(nums);

    }

	AdapterView.OnItemSelectedListener onScheduleTypeSelectedListener = new AdapterView.OnItemSelectedListener()
	{
		@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
		{
			ignoreCheckBoxChange = true;
			switch(position)
			{
			case MedicationSchedule.TYPE_DAILY:
				if(viewFlipper.getDisplayedChild() != 3)
				{
					viewFlipper.setDisplayedChild(3);
				}
				break;
			case MedicationSchedule.TYPE_WEEKLY:
				for(int i = 0; i < cbDays.length; i++)
				{
					if(i == 0)
					{
						cbDays[i].setChecked(false);
					}
					else
					{
						cbDays[i].setEnabled(true);
					}
				}
				if(viewFlipper.getDisplayedChild() != 0)
				{
					viewFlipper.setDisplayedChild(0);
				}
				break;
			case MedicationSchedule.TYPE_MONTHLY:
                if(viewFlipper.getDisplayedChild() != 1)
                {
                    viewFlipper.setDisplayedChild(1);
                }
				break;
            case MedicationSchedule.TYPE_X_DAYS:
                if(viewFlipper.getDisplayedChild() != 2)
                {
                    viewFlipper.setDisplayedChild(2);
                }
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

    View.OnClickListener onCalendarDayClickedListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int buttonNumber = Integer.parseInt(button.getText().toString());
            if(selectedCalendarDays.get(buttonNumber) == null)
            {
                view.setBackgroundResource(R.drawable.selector_reddotbg);
                ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
                selectedCalendarDays.put(buttonNumber, true);
            }
            else if(selectedCalendarDays.get(buttonNumber) == true)
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
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
            view.setTextColor(view.getResources().getColor(R.color.card_title));
			view.setText(strings[position]);

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			View view = inflater.inflate(R.layout.dialog_setschedule_types, null);
			TextView tvScheduleType = (TextView) view.findViewById(R.id.tvScheduleType);

			tvScheduleType.setText(strings[position]);

			return view;
		}
	}

    class StartDaySpinnerAdapter extends ArrayAdapter<String>
    {
        LayoutInflater inflater;
        int preferredHeight;

        String[] strings;

        public StartDaySpinnerAdapter(Context context)
        {
            super(context, R.layout.sherlock_spinner_dropdown_item);

            this.inflater = LayoutInflater.from(context);
            this.preferredHeight = Utils.convertDpToPixel(48, context.getResources());
            this.strings = context.getResources().getStringArray(R.array.startDays);
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
            //view.setTextColor(view.getResources().getColor(R.color.card_title));
            view.setText(strings[position]);

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            View view = inflater.inflate(R.layout.dialog_setschedule_types, null);
            TextView tvStartDay = (TextView) view.findViewById(R.id.tvScheduleType);

            tvStartDay.setText(strings[position]);

            return view;
        }
    }
}
