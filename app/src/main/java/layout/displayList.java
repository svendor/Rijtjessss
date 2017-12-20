package layout;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.OnCustomTouchListener;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.editWordListACTIVITY;
import com.absontwikkeling.rijtjes.question;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class displayList extends Fragment {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;
    private GestureDetector gDetector;
    View view;
    LinearLayout linearLayoutList;
    public static int radioState;
    // TextView showList;

    public displayList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_list, container, false);

        // setScrollView();

        // Debugging for displayQuery()
        // showList = (TextView) findViewById(R.id.showList);
        // showList.setText(displayQuery(DLdbAdapter.getAllRowsMain()));

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                switch(i) {
                    case R.id.questionTheList:
                        radioState = 0;
                        break;
                    case R.id.editList:
                        radioState = 1;
                        break;
                    case R.id.deleteList:
                        radioState = 2;
                        break;
                }
            }
        });

        // Opens database
        openDB();
        dbAdapterMain.createTableMain();

        // Creates list of buttons
        linearLayoutList = view.findViewById(R.id.displayListLinearLayout);
        if (dbAdapterMain.getAllRowsMain() != null) {
            createButtonListInLayout(dbAdapterMain.getAllRowsMain());
        } else {
            createList fragment = new createList();
            FragmentManager fragMan = getFragmentManager();
            fragMan.beginTransaction().replace(R.id.relativelayout_fragment, fragment).commit();
            Toast.makeText(getContext(), "Maak eerst een lijst", Toast.LENGTH_SHORT).show();
        }

        // Defines function of Floating Action Button that add a new list
        // TODO Zorg ervoor dat deze knop ook het menu switched naar het andere framgent.
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createList createList = new createList();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_fragment, createList).commit();
            }
        });

        return view;
    }

    //  Haalt het aantal pixels van de breedte het scherm van het apparaat
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    //  Haalt het aantal pixels van de hoogte het scherm van het apparaat
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void setScrollView() {
        // Neemt de scrollView
        ScrollView scrollView = view.findViewById(R.id.scrollView2);
        // Geeft de parameters die de mogelijkheid geven om de lay-out aan te passen
        ViewGroup.LayoutParams params = scrollView.getLayoutParams();
        // Verandert in dit geval de hoogte tot 75% van het scherm van het apparaat
        params.height = (int) (getScreenHeight()*0.75);
        scrollView.setLayoutParams(params);

    }

    public void onDetach() {
        super.onDetach();
        closeDB();
    }

    private void openDB() {
        dbAdapter = new DBAdapter(getContext(), DBAdapter.DATABASE_NAME, DBAdapter.DATABASE_VERSION);
        dbAdapter.open();
        dbAdapterMain = new DBAdapter(getContext(), DBAdapter.DATABASE_MAIN_NAME, DBAdapter.DATABASE_MAIN_VERSION);
        dbAdapterMain.open();
    }

    private void closeDB() {
        dbAdapter.close();
        dbAdapterMain.close();
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    // https://stackoverflow.com/questions/1714297/android-view-setidint-id-programmatically-how-to-avoid-id-conflicts
    private static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private void createButtonListInLayout(Cursor c) {
        if (c.moveToFirst()) {
            do {
                // Definieer Button
                Button button = new Button(getContext());
                // Layout button
                // Geeft naam button
                final String tableName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
                // Tekst lay-out van de button
                button.setText(tableName);
                button.setTextColor(Color.parseColor("#454545"));
                // Vorm en kleur van de button
                button.setBackgroundResource(R.drawable.button);
                //Zorgen ervoor dat er margers tussen de buttons zijn
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                params.setMargins(0, 0, 0, 8);
                button.setLayoutParams(params);
                //Zorgen ervoor dat de tekst op een goede plek staat
                button.setGravity(10);
                button.setPadding(50,40,0,0);
                // Maakt backgroundresource doorzichtig
                button.setBackgroundResource(0);
                // Function
                button.setId(generateViewId());
                final int id = sNextGeneratedId.get();
                button.setOnTouchListener(new OnCustomTouchListener(getContext()) {
                    @Override
                    public void onSingleTap() {
                        if (dbAdapter.getAllRows(tableName).moveToFirst()) {
                            Intent i = new Intent(getActivity(), question.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);
                            Toast.makeText(getContext(), "Er moeten wel woorden in jouw lijst staan!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDoubleTouch() {
                        Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                        i.putExtra("tableName", tableName);
                        startActivity(i);
                    }

                    public void onSwipeRight() {
                        view.findViewById(id);
                        view.setVisibility(View.GONE);
                        dbAdapter.deleteTable(tableName);
                        dbAdapterMain.deleteRowMain(tableName);
                        displayList fragment = (displayList) getFragmentManager().findFragmentById(R.id.relativelayout_fragment);
                        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                });

                // Voeg button toe aan activity
                linearLayoutList.addView(button);

            } while(c.moveToNext());
        }

    }

    /*
    ################### Function to check if main table works ###########################

    private String displayQuery(Cursor cursor) {
        String message = "";
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                String listName = cursor.getString(DBAdapter.COL_TABLE_NAME_MAIN);

                // Append data to the message:
                message += listName +"\n";
            } while(cursor.moveToNext());
        }

        cursor.close();
        return message;
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if  // definieert functie radioButton
                                (radioState == 0) {
                            if (dbAdapter.getAllRows(tableName).moveToFirst()) {
                                Intent i = new Intent(getActivity(), question.class);
                                i.putExtra("tableName", tableName);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                                i.putExtra("tableName", tableName);
                                startActivity(i);
                                Toast.makeText(getContext(), "Er moeten wel woorden in jouw lijst staan!", Toast.LENGTH_SHORT).show();
                            }

                        } else if // definieert functie radioButton editList
                                (radioState == 1) {
                            Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                            i.putExtra("tableName", tableName);
                            startActivity(i);

                        } else if // definieert functie radioButton deleteList
                                (radioState == 2) {
                            dbAdapter.deleteTable(tableName);
                            dbAdapterMain.deleteRowMain(tableName);
                            displayList fragment = (displayList) getFragmentManager().findFragmentById(R.id.relativelayout_fragment);
                            getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                        }

                    }
                };
                button.setOnClickListener(buttonListener);
                button.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
                        i.putExtra("tableName", tableName);
                        startActivity(i);
                        return true;
                    }
                });
    */
}
