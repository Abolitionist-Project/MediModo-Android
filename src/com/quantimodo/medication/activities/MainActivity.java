package com.quantimodo.medication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.quantimodo.medication.Global;
import com.quantimodo.medication.R;
import com.quantimodo.medication.fragments.MedicationOverviewFragment;
import com.quantimodo.medication.things.Medication;
import org.holoeverywhere.widget.Toast;

import java.util.List;

public class MainActivity extends SherlockFragmentActivity
{
	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Global.init(this);

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.mainFragment);
		if (fragment == null)
		{
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.mainFragment, new MedicationOverviewFragment());
			fragmentTransaction.commit();
		}

		Medication.loadAll(this, new Medication.OnMedicationLoadedListener()
		{
			@Override public void onMedicationLoaded(List<Medication> loadedMedication)
			{
				Toast.makeText(MainActivity.this, "Loaded " + loadedMedication.size() + " medication(s)", Toast.LENGTH_SHORT).show();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_settings:
			//Intent openPrefsIntent = new Intent(this, PreferencesActivity.class);
			//startActivity(openPrefsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}