package com.cyber.kinoost;

import java.util.Date;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.fragments.AppInfoFragment;
import com.cyber.kinoost.fragments.FilmsGridFragment;
import com.cyber.kinoost.fragments.TopFilmsGridFragment;

public class KinoostActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] menuTitles;
    
    public static final String APP_PREFERENCES = "com.cyber.kinoost";
	public static final String APP_PREFERENCES_UPDATE_DATETIME = "com.cyber.kinoost.update.datetime";
	public static final String APP_PREFERENCES_UPDATE_DATE = "com.cyber.kinoost.update.date";
	public static final long APP_PREFERENCES_DAYS_UPDATE = 1;
	
	DatabaseHelper dbHelper;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        
        //From MainActivity
        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		editor = prefs.edit();
        

        mTitle = mDrawerTitle = getTitle();
        menuTitles = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, menuTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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
		Date updDate = new Date(prefs.getLong(
				APP_PREFERENCES_UPDATE_DATE, 0));
		long diffDays = (newDate.getTime() - storedDate.getTime())
				/ (24 * 60 * 60 * 10);

		if (diffDays >= APP_PREFERENCES_DAYS_UPDATE) {
			editor.putLong(APP_PREFERENCES_UPDATE_DATETIME,
					newDate.getTime());
			editor.commit();
			ApiHelper.dbUpdate(getBaseContext(), updDate);
		}
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
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
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    	Fragment fragment;
    	switch(position) {
    		case 0: fragment = new FilmsGridFragment();  
    				break;
    		case 1: fragment = new TopFilmsGridFragment();  
					break;
    		case 5: fragment = new AppInfoFragment();
    				break;
    		default: fragment = new FilmsGridFragment();
    				break;
    	}

    	Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(menuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
  
}