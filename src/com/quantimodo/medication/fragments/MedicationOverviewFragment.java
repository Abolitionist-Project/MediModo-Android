package com.quantimodo.medication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.quantimodo.medication.ManageMedicationActivity;
import com.quantimodo.medication.R;

import java.util.ArrayList;

public class MedicationOverviewFragment extends SherlockFragment
{
	private ListView listView;

	@Override public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		if (container == null)
		{
			return null;
		}

		View view = inflater.inflate(R.layout.fragment_medicationoverview, container, false);
		View headerView = inflater.inflate(R.layout.fragment_medicationoverview_today, null);
		//TODO Fill header
		listView = (ListView) view.findViewById(android.R.id.list);
		listView.addHeaderView(headerView);
		listView.setAdapter(new Adapter());

		initTodayHeader();

		return view;
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.medication, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_add:
			Intent intent = new Intent(this.getActivity(), ManageMedicationActivity.class);
			this.getActivity().startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initTodayHeader()
	{

	}

	static class ViewHolder
	{

	}

	public class Adapter extends ArrayAdapter<String>
	{
		private LayoutInflater inflater;

		public Adapter()
		{
			super(getActivity(), R.layout.abs__list_menu_item_checkbox, new ArrayList<String>());
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			if (convertView == null)
			{
				holder = new ViewHolder();

				convertView = new View(getContext());
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			convertView = new View(getContext());
			return convertView;
		}
	}
}
