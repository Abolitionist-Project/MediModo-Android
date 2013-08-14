package com.quantimodo.medication;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import com.actionbarsherlock.app.ActionBar;
import com.google.gson.stream.JsonReader;
import com.google.zxing.client.android.CaptureActivity;
import com.quantimodo.medication.dialogs.SetScheduleDialog;
import com.quantimodo.medication.things.MedicationDose;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Activity;
import org.holoeverywhere.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ManageMedicationActivity extends Activity
{
	private LinearLayout linearLayout;
	AutoCompleteTextView tvMedicationName;
	LinearLayout lnDosageCard;
	LinearLayout lnScheduleCard;
	EditText btAddNewSchedule;
	ArrayList<DosageSelectSpinnerAdapter> dosageSelectAdapters;

	private ArrayAdapter<String> autoCompleteAdapter;

	ArrayList<MedicationDose> medicationDoses;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_managemedication);

		initActionBarButtons();

		linearLayout = (LinearLayout) findViewById(R.id.lnMain);
		ImageButton btBarcodeScanner = (ImageButton) findViewById(R.id.btBarcodeScanner);
		btBarcodeScanner.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				startBarcodeScanner();
			}
		});

		if(savedInstanceState == null)
		{
			medicationDoses = new ArrayList<MedicationDose>();
			medicationDoses.add(new MedicationDose("unknown", -1, MedicationDose.UNIT_MILLIGRAM));
		}

		initAutoComplete();
		initDosagesCard();
		initScheduleCard();
		//initInfoCard();
	}

	private void initAutoComplete()
	{
		tvMedicationName = (AutoCompleteTextView) findViewById(R.id.tvMedicationName);
		autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
		tvMedicationName.setAdapter(autoCompleteAdapter);
		tvMedicationName.addTextChangedListener(onMedicationNameChanged);
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

	private void initDosagesCard()
	{
		LayoutInflater inflater = getLayoutInflater();
		lnDosageCard = (LinearLayout) inflater.inflate(R.layout.activity_managemedication_dosages, null);

		final EditText btAddNew = (EditText) lnDosageCard.findViewById(R.id.btAddNew);
		btAddNew.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				MedicationDose newDose = new MedicationDose("unknown", -1, MedicationDose.UNIT_MILLIGRAM);
				medicationDoses.add(newDose);
				addDosageLine(newDose, false);
			}
		});
		for(MedicationDose dose : medicationDoses)
		{
			addDosageLine(dose, true);
		}

		linearLayout.addView(lnDosageCard);
	}

	private void initScheduleCard()
	{
		dosageSelectAdapters = new ArrayList<DosageSelectSpinnerAdapter>();

		View paddingView = new View(getApplicationContext());
		paddingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPixel(12, getResources())));
		linearLayout.addView(paddingView);

		lnScheduleCard = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_managemedication_schedule, null);

		final EditText btSetSchedule = (EditText) lnScheduleCard.findViewById(R.id.btSetSchedule);
		btSetSchedule.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				new SetScheduleDialog().show(ManageMedicationActivity.this);
			}
		});

		btAddNewSchedule = (EditText) lnScheduleCard.findViewById(R.id.btAddNew);
		btAddNewSchedule.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				addScheduleLine();
			}
		});
		linearLayout.addView(lnScheduleCard);
	}

	private void initInfoCard()
	{
		View paddingView = new View(getApplicationContext());
		paddingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.convertDpToPixel(12, getResources())));
		linearLayout.addView(paddingView);

		View view = getLayoutInflater().inflate(R.layout.activity_managemedication_info, null);
		linearLayout.addView(view);
	}

	private void addDosageLine(final MedicationDose dose, boolean isFirst)
	{
		final View newDosageLine = getLayoutInflater().inflate(R.layout.activity_managemedication_dosages_line, null);

		final EditText etDosageStrength = (EditText) newDosageLine.findViewById(R.id.etDosageStrength);
		final Spinner spDosageUnit = (Spinner) newDosageLine.findViewById(R.id.spDosageUnit);
		final ImageButton btRemoveDose = (ImageButton) newDosageLine.findViewById(R.id.btRemoveDose);

		if(dose.value != -1)
		{
			etDosageStrength.setText(String.valueOf(dose.value));
		}

		DosageTypeSpinnerAdapter adapter = new DosageTypeSpinnerAdapter(this, R.layout.sherlock_spinner_dropdown_item);
		spDosageUnit.setAdapter(adapter);

		btRemoveDose.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				Utils.collapseView(newDosageLine, new Animation.AnimationListener()
				{
					@Override public void onAnimationStart(Animation animation)
					{
					}

					@Override public void onAnimationEnd(Animation animation)
					{
						lnDosageCard.removeView(newDosageLine);
						medicationDoses.remove(dose);
						updateDosageSelectAdapters(dose);
					}

					@Override public void onAnimationRepeat(Animation animation)
					{
					}
				});
			}
		});

		etDosageStrength.addTextChangedListener(new TextWatcher()
		{
			@Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
			{
			}
			@Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
			{
			}
			@Override public void afterTextChanged(Editable editable)
			{
				if(editable.length() > 0)
				{
					try
					{
						dose.value = Integer.valueOf(editable.toString());
					}
					catch(NumberFormatException e)
					{
						Toast.makeText(ManageMedicationActivity.this, "Invalid number", Toast.LENGTH_SHORT).show();
					}
					updateDosageSelectAdapters(dose);
				}
				else
				{
					dose.value = -1;
				}
			}
		});

		spDosageUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				dose.unit = MedicationDose.positionToUnit(position);
				Log.i("ITEM SELECTED");
				updateDosageSelectAdapters(dose);
				etDosageStrength.requestFocus();
			}
			@Override public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});



		lnDosageCard.addView(newDosageLine, lnDosageCard.getChildCount() - 2);
		if(!isFirst)
		{
			Utils.expandView(newDosageLine, null);
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
			etDosageStrength.requestFocus();
		}
	}

	private void addScheduleLine()
	{
		final View newTimeLine = getLayoutInflater().inflate(R.layout.activity_managemedication_schedule_line, null);

		//final EditText etDosageStrength = (EditText) newDosageLine.findViewById(R.id.etDosageStrength);
		final Spinner spDosage = (Spinner) newTimeLine.findViewById(R.id.spDosage);
		final ImageButton btRemoveTime = (ImageButton) newTimeLine.findViewById(R.id.btRemoveTime);

		final DosageSelectSpinnerAdapter adapter = new DosageSelectSpinnerAdapter(this, R.layout.sherlock_spinner_dropdown_item);
		spDosage.setAdapter(adapter);
		dosageSelectAdapters.add(adapter);

		btRemoveTime.setOnClickListener(new View.OnClickListener()
		{
			@Override public void onClick(View view)
			{
				Utils.collapseView(newTimeLine, new Animation.AnimationListener()
				{
					@Override public void onAnimationStart(Animation animation)
					{
					}

					@Override public void onAnimationEnd(Animation animation)
					{
						lnScheduleCard.removeView(newTimeLine);
						dosageSelectAdapters.remove(adapter);
					}

					@Override public void onAnimationRepeat(Animation animation)
					{
					}
				});
			}
		});

		spDosage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				MedicationDose currentDose = medicationDoses.get(position);
				adapter.selectedView.setText(currentDose.value + " " + currentDose.getHumanReadableUnit(getResources()));
			}
			@Override public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});

		lnScheduleCard.addView(newTimeLine, lnScheduleCard.getChildCount() - 2);
		Utils.expandView(newTimeLine, null);
	}

	private void updateDosageSelectAdapters(MedicationDose updatedDose)
	{
		for(DosageSelectSpinnerAdapter adapter : dosageSelectAdapters)
		{
			adapter.selectedView.setText(updatedDose.value + " " + updatedDose.getHumanReadableUnit(getResources()));
			adapter.notifyDataSetChanged();
		}
	}

	class DosageTypeSpinnerAdapter extends ArrayAdapter<String>
	{
		String[] unitsReadable;
		String[] units;

		LayoutInflater inflater;
		int preferredHeight;

		public DosageTypeSpinnerAdapter(Context context, int textViewResourceId)
		{
			super(context, 0);
			this.inflater = LayoutInflater.from(context);
			this.preferredHeight = Utils.convertDpToPixel(48, getResources());
			this.unitsReadable = context.getResources().getStringArray(R.array.units_readable);
			this.units = context.getResources().getStringArray(R.array.units);
		}

		@Override
		public int getCount()
		{
			return unitsReadable.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);
			view.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
			view.setText(unitsReadable[position]);

			return view;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_managemedication_dosages_units, null);

			TextView tvUnitReadable = (TextView) view.findViewById(R.id.tvUnitReadable);
			tvUnitReadable.setText(unitsReadable[position]);

			TextView tvUnit = (TextView) view.findViewById(R.id.tvUnit);
			View vwSeparator = view.findViewById(R.id.vwSeparator);
			if(units[position].equals("."))
			{
				tvUnit.setVisibility(View.GONE);
				vwSeparator.setVisibility(View.GONE);
			}
			else
			{
				vwSeparator.setVisibility(View.VISIBLE);
				tvUnit.setVisibility(View.VISIBLE);
				tvUnit.setText(units[position]);
			}

			return view;
		}
	}

	class DosageSelectSpinnerAdapter extends ArrayAdapter<MedicationDose>
	{
		LayoutInflater inflater;
		int preferredHeight;

		public TextView selectedView;

		public DosageSelectSpinnerAdapter(Context context, int textViewResourceId)
		{
			super(context, 0, 0, medicationDoses);

			this.inflater = LayoutInflater.from(context);
			this.preferredHeight = Utils.convertDpToPixel(48, getResources());

			selectedView = (TextView) inflater.inflate(R.layout.sherlock_spinner_dropdown_item, null);
		}

		@Override
		public int getCount()
		{
			return medicationDoses.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			return selectedView;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_managemedication_schedule_dosages, null);

			MedicationDose currentDose = getItem(position);

			TextView tvDosage = (TextView) view.findViewById(R.id.tvDosage);
			tvDosage.setText(currentDose.value + " " + currentDose.getHumanReadableUnit(getResources()));

			return view;
		}
	}

	private TextWatcher onMedicationNameChanged = new TextWatcher()
	{
		@Override public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
		{
		}

		@Override public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
		{
		}

		@Override public void afterTextChanged(final Editable editable)
		{
			if (editable.length() != 2)
			{
				return;
			}
			updateAutoCompleteAdapter();
		}
	};

	private void startBarcodeScanner()
	{
		Intent intent = new Intent(this, CaptureActivity.class);
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE, PRODUCT_MODE");
		startActivityForResult(intent, 0);
	}

	HttpGet autoCompleteGetRequest;
	private void updateAutoCompleteAdapter()
	{
		final Handler handler = new Handler();
		final String currentValue = tvMedicationName.getText().toString();
		Runnable run = new Runnable()
		{
			@Override public void run()
			{
				HttpClient client = new DefaultHttpClient();
				if(autoCompleteGetRequest != null)
				{
					autoCompleteGetRequest.abort();
				}
				try
				{
					autoCompleteGetRequest = new HttpGet("http://www.healthline.com/v2/druginputautocomplete?query=" + URLEncoder.encode(currentValue, "utf-8"));
					HttpResponse response = client.execute(autoCompleteGetRequest);
					final BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					JsonReader gsonReader = new JsonReader(reader);

					gsonReader.beginObject();

					final ArrayList<String> strings = new ArrayList<String>();

					while (gsonReader.hasNext())
					{
						if (gsonReader.nextName().equals("Drugs"))
						{
							gsonReader.beginArray();

							boolean inObject = false;
							while (gsonReader.hasNext())
							{
								if (!inObject)
								{
									gsonReader.beginObject();
									inObject = true;
								}

								String name = gsonReader.nextName();
								if (name.equals("Term"))
								{
									String newMed = gsonReader.nextString();
									boolean contains = false;
									for(String currentString : strings)
									{
										if(currentString.equalsIgnoreCase(newMed))
										{
											contains = true;
											break;
										}
									}
									if(!contains)
									{
										strings.add(newMed);
									}
									gsonReader.endObject();
									inObject = false;
								}
								else
								{
									gsonReader.skipValue();
								}
							}
							gsonReader.endArray();

						}
						else
						{
							gsonReader.skipValue();
						}
					}

					handler.post(new Runnable()
					{
						@Override public void run()
						{
							autoCompleteAdapter.clear();
							if(Build.VERSION.SDK_INT >= 11)
							{
								autoCompleteAdapter.addAll(strings);
							}
							else
							{
								autoCompleteAdapter.setNotifyOnChange(false);
								for(String string : strings)
								{
									autoCompleteAdapter.add(string);
								}
							}
							tvMedicationName.setAdapter(autoCompleteAdapter);
						}
					});
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		new Thread(run).start();
	}
}
