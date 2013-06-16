package com.quantimodo.medication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.internal.widget.IcsSpinner;

public class ManageMedicationActivity extends SherlockActivity
{
	LinearLayout linearLayout;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managemedication);

		initActionBarButtons();

		linearLayout = (LinearLayout) findViewById(R.id.lnMain);

		initDosageCard();
		initScheduleCard();
	}

	private void initActionBarButtons()
	{
		ActionBar actionBar = getSupportActionBar();
		View actionBarView = getLayoutInflater().inflate(R.layout.actionbar_donediscard, null);
		View discardButton = actionBarView.findViewById(R.id.actionbar_discard);
		discardButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});
		View doneButton = actionBarView.findViewById(R.id.actionbar_done);
		doneButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				finish();
			}
		});
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.getDisplayOptions();
		actionBar.setCustomView(actionBarView);
	}

	private void initDosageCard()
	{
		View view = getLayoutInflater().inflate(R.layout.activiy_managemedication_dosages, null);

		IcsSpinner dosageSpinner = (IcsSpinner) view.findViewById(R.id.spDosage);
		final DosageTypeSpinnerAdapter adapter = new DosageTypeSpinnerAdapter(this, R.layout.sherlock_spinner_dropdown_item);
		dosageSpinner.setAdapter(adapter);
		linearLayout.addView(view);
	}

	private void initScheduleCard()
	{
		View paddingView = new View(getApplicationContext());
		paddingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPixel(12, getResources())));
		linearLayout.addView(paddingView);

		View view = getLayoutInflater().inflate(R.layout.activity_managemedication_schedule, null);

		linearLayout.addView(view);
	}

	class DosageTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;

		int preferredHeight;

		public DosageTypeSpinnerAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);

			this.inflater = LayoutInflater.from(context);

			this.preferredHeight = Utils.convertDpToPixel(48, getResources());
		}

		@Override
		public int getCount()
		{
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);

			view.setText("milligrams");

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);
			view.setBackgroundResource(R.color.card_background);
			view.setHeight(preferredHeight);

			view.setText("mg");

			return view;
		}
	}

	class MedTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		LayoutInflater inflater;

		int preferredHeight;

		public MedTypeSpinnerAdapter(Context context, int textViewResourceId)
		{
			super(context, textViewResourceId);

			this.inflater = LayoutInflater.from(context);

			this.preferredHeight = Utils.convertDpToPixel(48, getResources());
		}

		@Override
		public int getCount()
		{
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView view = new ImageView(getContext());
			view.setImageResource(R.drawable.ic_pill);

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			ImageView view = new ImageView(getContext());
			view.setImageResource(R.drawable.ic_pill);

			return view;
		}
	}
}
