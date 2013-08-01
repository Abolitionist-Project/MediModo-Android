package com.quantimodo.medication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.quantimodo.medication.fragments.MedicationOverviewFragment;

public class MainActivity extends SherlockFragmentActivity
{
	private Fragment medicationFragment;

	@Override protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Global.init(this);
		
		if (savedInstanceState != null)
		{
			medicationFragment = getSupportFragmentManager().getFragment(savedInstanceState, MedicationOverviewFragment.class.getName());
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.mainFragment);
		if (fragment == null)
		{
			if (medicationFragment == null)
			{
				medicationFragment = new MedicationOverviewFragment();
			}
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.replace(R.id.mainFragment, medicationFragment);
			fragmentTransaction.commit();
		}
	}

	@Override protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		getSupportFragmentManager().putFragment(outState, MedicationOverviewFragment.class.getName(), medicationFragment);
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