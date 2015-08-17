package com.example.ashish.honeysha_tech;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private boolean playState = false;

    public static Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private NavDrawerListAdapter navigationDrawerAdapter;
    MediaPlayer mPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nitView();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initDrawer();
        seekUpdation();

        ((SeekBar)findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SeekBar seekbar = (SeekBar) (findViewById(R.id.seekBar));
                int mediaMax_new = mPlayer.getDuration();
                //int currentPosition = utils.progressToTimer();
                mPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

        navMenuIcons.recycle();

        leftDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        navigationDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        leftDrawerList.setAdapter(navigationDrawerAdapter);

    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            displayView(position);
        }
    }
    int[] images = {R.drawable.durga,R.drawable.hanuman,R.drawable.ram,R.drawable.hanuman,R.drawable.hanuman,R.drawable.hanuman};
    int[] songs = {R.raw.moodindia,R.raw.lonely,R.raw.moodindia,R.raw.lonely,R.raw.moodindia,R.raw.lonely};
    int currentPlaying = -1;
    private void displayView(int position) {
        if(currentPlaying!=position)
        {
            currentPlaying = position;
            ((SeekBar) findViewById(R.id.seekBar)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.curTimeBox)).setVisibility(View.VISIBLE);
            if(mPlayer!=null) mPlayer.stop();
            mPlayer = MediaPlayer.create(this, songs[position]);
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ((ImageView)(findViewById(R.id.imageButton))).setImageResource(R.drawable.play);
                    playState = false;
                    setTitle("Finished");
                }
            });
            int min = mPlayer.getDuration() / 60000;
            int sec = (mPlayer.getDuration()/1000)%60;
            ((TextView) findViewById(R.id.endTimeBox)).setText(formatIntToTime(min,sec));
            ((TextView) findViewById(R.id.textView)).setText("Now Playing : " + getResources().getStringArray(R.array.nav_drawer_items)[position]);
            ((ImageView) (findViewById(R.id.imageView))).setImageResource(images[position]);
            ((ImageView) (findViewById(R.id.imageButton))).setImageResource(R.drawable.pause);
            mPlayer.start();
            playState = true;
        }
        setTitle("Playing");
        leftDrawerList.setItemChecked(position, true);
        leftDrawerList.setSelection(position);
        drawerLayout.closeDrawer(leftDrawerList);
    }
    String formatIntToTime(int min,int sec)
    {
        String s = sec+",";
        if(s.length()==2)
                return (min + ":0" + sec);
        return (min + ":" + sec);
    }
    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    Handler handler = new Handler();
    private Runnable moveSeekBarThread = new Runnable() {
        public void run() {
            seekUpdation();
        }

    };
    public void seekUpdation() {
        if(mPlayer!=null) {
            SeekBar seekbar = (SeekBar) (findViewById(R.id.seekBar));
            int mediaPos_new = mPlayer.getCurrentPosition();
            int mediaMax_new = mPlayer.getDuration();
            int min = mediaPos_new / 60000;
            int sec = (mediaPos_new/1000)%60;
            ((TextView) findViewById(R.id.curTimeBox)).setText(formatIntToTime(min, sec));
            seekbar.setMax(mediaMax_new);
            seekbar.setProgress(mediaPos_new);
        }
        handler.postDelayed(moveSeekBarThread, 1000);
    }
    public void play_click(View v)
    {
        if(playState==false)
        {
            ((ImageView)(findViewById(R.id.imageButton))).setImageResource(R.drawable.pause);
            mPlayer.start();
            playState = true;
            setTitle("Playing");
        }
        else
        {
            ((ImageView)(findViewById(R.id.imageButton))).setImageResource(R.drawable.play);
            mPlayer.pause();
            playState = false;
            setTitle("Paused");
        }
    }
}
