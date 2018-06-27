package com.abc.example.com.tab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.abc.example.com.tab.R.id.l1;

public class MainActivity extends AppCompatActivity {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
     RequestQueue queue;
     datalist datalist;
    DATABASEHANDLER db;
    boolean fla = true;
 static SwipeMenuListView l1 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DATABASEHANDLER(MainActivity.this);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void UpdateDetails()
    {

        if (queue == null) {
            queue = Volley.newRequestQueue(MainActivity.this);
        }

        String url1 = "http://192.168.43.142/updatedetails.php";
        Log.e("UpdateDetails" , url1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ar = new JSONArray(response);
                            String sender_num[] = new String[ar.length()];
                            String receiver_num[] = new String[ar.length()];
                            String Date[] = new String[ar.length()];
                            String Mes[] = new String[ar.length()];
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject out = ar.getJSONObject(i);
                                sender_num[i] = out.getString("sender_number");
                                receiver_num[i] = out.getString("receiver_number");
                                Date[i] = out.getString("Date");
                                Mes[i] = out.getString("message");
                                Log.e("Date Display",Date[i]);
                                Log.e("Mes" , Mes[i]);

                                if(sender_num[i].equals("9909856507"))
                                {
                                    DATABASEHANDLER db = new DATABASEHANDLER(MainActivity.this);
                                    db.update1(receiver_num[i] ,  Mes[i] , Date[i]);
                                }
                                else if(receiver_num[i].equals("9909856507"))
                                {
                                    DATABASEHANDLER db = new DATABASEHANDLER(MainActivity.this);
                                    db.update1(sender_num[i] ,  Mes[i], Date[i]);
                                }
                            }
                        } catch (JSONException ex) {
                            Log.e("result", "Error:=" + ex.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }


        });
        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,//time to wait for it in this case 20s
                        20,//tryies in case of error
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        queue.add(stringRequest);

    }


    private  void friends(){

        if (queue == null) {
            queue = Volley.newRequestQueue(MainActivity.this);
        }
        final String num = "9909856507";
        String url = "http://192.168.43.142/friend.php?uname="+num;
        Log.e("url",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ar = new JSONArray(response);
                            String number[] = new String[ar.length()];
                            String notification[] = new String[ar.length()];
                            String flag[] = new String[ar.length()];
                            Log.e("Checking", "Test");
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject out = ar.getJSONObject(i);
                                number[i] = out.getString("name");
                                notification[i] = out.getString("total");
                                flag[i] = out.getString("flag").trim();

                                Log.e("nameee", number[i]);
                                Log.e("notification", notification[i]);
                                DATABASEHANDLER db = new DATABASEHANDLER(MainActivity.this);
                                Registration obj = new Registration(number[i], "0" , "0" , "0" , "0");
                                db.addDATA(obj);

                                if (Integer.parseInt(flag[i]) == 0) {
                                    Log.e("sss", "sahil");
                                    db.update(number[i], notification[i]);
                                    db.updateflag0(number[i]);

                                } else {
                                    Toast.makeText(MainActivity.this, "NO NOTIFICATIONS", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException ex) {
                            Log.e("result", "Error:=" + ex.getMessage());
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }


        });
        queue.add(stringRequest);

    }

    public  void display() {

        db = new DATABASEHANDLER(MainActivity.this);

        try {
            if (fla) {
                List<Registration> contacts = db.getAllData();
                String[] mes = new String[contacts.size()];
                String[] notification = new String[contacts.size()];
                String[] date = new String[contacts.size()];
                int i = 0;
                for (Registration cn : contacts) {
                    mes[i] = cn.getPhone();
                    Log.e("Mess", cn.getPhone());
                    notification[i] = cn.getCreated_date();
                    Log.e("dadd", cn.getCreated_date());
                    i++;
//                if(i==contacts.size())break;
                    DATABASEHANDLER db = new DATABASEHANDLER(MainActivity.this);
                    db.updateflag1(cn.getPhone());
                }
                Log.e("DIsplay", "Helloo");

                datalist = new datalist(MainActivity.this, mes , notification);
                //l1.setItemsCanFocus(true);

                fla = false;
            } else {
                List<Registration> contacts = db.getAllbyData();
                int i = 0;
                for (Registration cn : contacts) {
                    //datalist.addItem(cn.getPhone() , cn.getCreated_date());
                    Log.e("LOG", cn.getPhone());
                    DATABASEHANDLER db = new DATABASEHANDLER(MainActivity.this);
                    db.updateflag1(cn.getPhone());
                    fla = true;
                }
                //       datalist.notifyDataSetChanged();

            }
        } catch (Exception e) {

        }
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
        SwipeMenuListView l1;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            Log.e("section" , String.valueOf(sectionNumber));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView=null;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1)
           {
               rootView = inflater.inflate(R.layout.fragment_main, container, false);

               l1 = (SwipeMenuListView)rootView.findViewById(R.id.l1);
                Log.e("ssdd","wdwd");

               final View finalRootView = rootView;
               final View finalRootView1 = rootView;
               final View finalRootView2 = rootView;
               SwipeMenuCreator creator = new SwipeMenuCreator() {

                   @Override
                   public void create(SwipeMenu menu) {
                       // create "open" item

                       l1 = (SwipeMenuListView) finalRootView2.findViewById(R.id.l1);

                       Log.e("dddd","sdfsfs");
                       SwipeMenuItem openItem = new SwipeMenuItem(finalRootView.getContext());
                       // set item background
                       openItem.setBackground(R.drawable.button_bg_round);

                       // set item width
                       openItem.setWidth(120);

                       // set item title

                       openItem.setTitle("Open");
                       // set item title fontsize
                       openItem.setIcon(R.drawable.ic_home_black_24dp);
                       // set item title font color
                       openItem.setTitleColor(Color.WHITE);

                       // add to menu
                       menu.addMenuItem(openItem);


                       // create "delete" item
                       SwipeMenuItem deleteItem = new SwipeMenuItem(finalRootView1.getContext());
                       // set item background
                       // set item width
                       deleteItem.setWidth(120);
                       // set a icon
                       deleteItem.setBackground(R.drawable.button_bg_round);

                       deleteItem.setIcon(R.drawable.ic_notifications_black_24dp);
                       // add to menu
                       menu.addMenuItem(deleteItem);


                       // Space.setWidth(20);
                       // Space.setTitle("ff");
                       //  Space.setTitleColor(Color.WHITE);
                       // Space.setBackground(R.drawable.button_space);
                       //  menu.addMenuItem(Space);


                   }
               };

               l1.setMenuCreator(creator);

               ArrayList<String> list = new ArrayList<>();
               list.add("Sahil");
               list.add("Dhara");list.add("Aayush");list.add("Mithil");list.add("Patel");list.add("Urvi");list.add("Sillu");list.add("Komal");
               list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");list.add("Mithil");
               list.add("Sahil");

               ArrayAdapter adapter = new ArrayAdapter(rootView.getContext() , android.R.layout.simple_list_item_1,list);
               l1.setAdapter(adapter);


               final Handler handler = new Handler();
               Runnable runable = new Runnable() {

                   @Override
                   public void run() {

                       MainActivity obj = new MainActivity();
            //           obj.UpdateDetails();

          //             obj.friends();
        //               obj.display();
                       //call the function
                       //also call the same runnable
                       handler.postDelayed(this, 1000);
                   }
               };
               handler.postDelayed(runable, 10);
                // set creator
                // TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //   textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
           }
            else
            {
                rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FRIENDS";
                case 1:
                    return "CONTACTS";
            }
            return null;
        }
    }
}
