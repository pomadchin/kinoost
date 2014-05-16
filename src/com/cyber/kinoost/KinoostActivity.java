package com.cyber.kinoost;

import java.util.Date;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.fragments.FilmsFragment;
import com.cyber.kinoost.fragments.InfoFragment;
import com.cyber.kinoost.fragments.OstFragment;
import com.cyber.kinoost.fragments.TopRatedFragment;

public class KinoostActivity extends FragmentActivity implements TabListener, OnQueryTextListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ActionBar actionBar;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] menuTitles;
	private SearchView searchView;

	public static final String APP_PREFERENCES = "com.cyber.kinoost";
	public static final String APP_PREFERENCES_UPDATE_DATETIME = "com.cyber.kinoost.update.datetime";
	public static final String APP_PREFERENCES_UPDATE_DATE = "com.cyber.kinoost.update.date";
	public static final long APP_PREFERENCES_DAYS_UPDATE = 1;

	private String[] tabs = { "ФИЛЬМЫ", "САУНДТРЕКИ", "ТОП 10" };

	DatabaseHelper dbHelper;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = prefs.edit();

		actionBar = getActionBar();
		mTitle = mDrawerTitle = getTitle();
		menuTitles = getResources().getStringArray(R.array.menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();

		// check data update on start
		Date storedDate = new Date(prefs.getLong(
				APP_PREFERENCES_UPDATE_DATETIME, 0));
		Date newDate = new Date();
		Date updDate = new Date(prefs.getLong(APP_PREFERENCES_UPDATE_DATE, 0));
		long diffDays = (newDate.getTime() - storedDate.getTime())
				/ (24 * 60 * 60 * 100);

		if (diffDays >= APP_PREFERENCES_DAYS_UPDATE) {
			editor.putLong(APP_PREFERENCES_UPDATE_DATETIME, newDate.getTime());
			editor.commit();
			ApiHelper.dbUpdate(getBaseContext(), updDate);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("Введите название...");

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);

	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	// <string-array name="menu_items">
	// <item>Главная</item>
	// <item>Избранное</item>
	// <item>Сохраненное</item>
	// <item>Рейтинг</item>
	// <item>Информация</item>
	// <item>Настройки</item>
	// </string-array>

	private void selectItem(int position) {
		Fragment fragment;
		switch (position) {
		case 0:
			fragment = new FilmsFragment();
			if (actionBar.getNavigationMode() != ActionBar.NAVIGATION_MODE_TABS)
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			break;
		case 1:
			fragment = new OstFragment();
			break;
		case 2:
			fragment = new TopRatedFragment();
			break;
		case 4:
			fragment = new InfoFragment();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			break;
		default:
			fragment = new FilmsFragment();
			break;
		}
		
		replaceFragment(fragment);

		mDrawerList.setItemChecked(position, true);
		if (position != 1 && position != 2)
			setTitle(menuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	public void replaceFragment(Fragment newFragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, newFragment);
		transaction.commit();		
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			this.finish();
		} else {
			getSupportFragmentManager().popBackStack();
		}
	}

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		int id = tab.getPosition();
		if (searchView != null && id == 2)
			searchView.setVisibility(View.INVISIBLE);
		else if (searchView != null)
			searchView.setVisibility(View.VISIBLE);
		selectItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public void onListItemClick(ListView l, View v, int position, long id) {
		// call detail activity for clicked entry
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO: add suggestions
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Fragment fragment;
		Bundle bundle = new Bundle();
		switch (actionBar.getSelectedTab().getPosition()) {
		case 0:
			fragment = new FilmsFragment();
			bundle.putString("filmName", query);
			break;
		case 1:
			fragment = new OstFragment();
			bundle.putString("songName", query);
			break;
		default: fragment = new FilmsFragment();
		}
		fragment.setArguments(bundle);
		replaceFragment(fragment);
		searchView.setQuery("", false);
		return false;
	}
	
}