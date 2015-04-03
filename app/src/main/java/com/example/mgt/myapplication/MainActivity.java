package com.example.mgt.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private FrameLayout container;
    private LinearLayout page1;
    private LinearLayout page2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (FrameLayout)findViewById(R.id.container);
        page1 = (LinearLayout)getLayoutInflater().inflate(R.layout.view_page1, null);
        page2 = (LinearLayout)getLayoutInflater().inflate(R.layout.view_page2, null);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /**
         * ExpandableListViewの生成
         */
        final String KEY1 = "TITLE";
        final String KEY2 = "SUMMARY";

        final int PARENT_DATA_SIZE = 3;
        // グループの親項目用のリスト
        List<Map<String, String>>parentList =  new ArrayList<Map<String, String>>();
        // 親リストに表示する内容を生成
        for (int i = 0; i < PARENT_DATA_SIZE; i++) {
            Map<String, String> parentDataMap = new HashMap<String, String>();
            parentDataMap.put(KEY1, "タイトル" + (i + 1));
            // グループの親項目用のリストに内容を格納
            parentList.add(parentDataMap);
        }

        final int CHILD_DATA_SIZE = 3;
        // 子要素全体のリスト
        List<List<Map<String, String>>> allChildList = new ArrayList<List<Map<String, String>>>();

        // 子要素として表示する文字を生成
        for (int i = 0; i < CHILD_DATA_SIZE; i++) {
            // 各グループ別のリスト項目用のリスト
            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();

            // リスト項目用データ格納
            for (int j = 0; j < CHILD_DATA_SIZE; j++) {
                Map<String, String> childDataMap = new HashMap<String, String>();
                childDataMap.put(KEY1, "子要素" + (j + 1));
                childDataMap.put(KEY2, "概要" + (j + 1));
                // リストに文字を格納
                childList.add(childDataMap);
            }
            // 子要素全体のリストに各グループごとのデータを格納
            allChildList.add(childList);
        }

        // ExpandableListView用のアダプタの生成
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                parentList,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { KEY1 },
                new int[] { android.R.id.text1, android.R.id.text2 },
                allChildList,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { KEY1, KEY2 },
                new int[] {android.R.id.text1, android.R.id.text2 }
        );
        // 生成した情報をセット
        ExpandableListView expandableListView = (ExpandableListView)page2.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(adapter);

        // リスト項目がクリックされた時の処理
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                // クリックされた場所の内容情報を取得
                Map<String, String> itemMap = (Map<String, String>)adapter.getChild(groupPosition, childPosition);
                // アラート表示
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("子要素がクリックされました")
                        .setMessage(itemMap.get(KEY1))
                        .setPositiveButton("OK", null)
                        .show();

                return false;
            }
        });

        // グループの親項目がクリックされた時の処理
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                // クリックされた場所の内容情報を取得
                Map<String, String> itemMap = (Map<String, String>)adapter.getGroup(groupPosition);
                // アラート表示
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("リストがクリックされました")
                        .setMessage(itemMap.get(KEY1))
                        .setPositiveButton("OK", null)
                        .show();

                return false;
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        LinearLayout[] page = {
                page1,
                page2,
                (LinearLayout) getLayoutInflater().inflate(R.layout.view_page3, null)
        };

        if (container.getChildCount() > 0) {
            container.removeViewAt(0);
        }
        container.addView(page[number - 1]);

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
