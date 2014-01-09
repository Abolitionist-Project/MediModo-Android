package com.quantimodo.medication.activities;

import android.os.Bundle;
import com.quantimodo.medication.R;
import com.quantimodo.medication.things.Medication;
import org.holoeverywhere.app.Activity;

public class ManageMedicationActivity extends Activity
{
	private static Medication medication;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editmedication);
	}

/*	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putString("medicationName", tvMedicationName.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		tvMedicationName.setText(savedInstanceState.getString("medicationName"));
	}*/

}
