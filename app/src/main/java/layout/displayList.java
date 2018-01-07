package layout;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.NavMenu;
import com.absontwikkeling.rijtjes.OnCustomTouchListener;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.editWordListACTIVITY;
import com.absontwikkeling.rijtjes.question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class displayList extends Fragment {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;
    private GestureDetector gDetector;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    public List<item_data> data;
    View view;
    // TextView showList;

    public displayList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_list, container, false);

        // Opens database
        openDB();
        dbAdapterMain.createTableMain();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDL);
        List<item_data> data = getData(dbAdapterMain.getAllRowsMain());

        // Populates recyclerView with items
        if (dbAdapterMain.getAllRowsMain() != null | data.size() != 0) {
            rvAdapter = new RVAdapter(getContext(), data, this);
            recyclerView.setAdapter(rvAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            ItemTouchHelper ith = new ItemTouchHelper(createCallBack());
            ith.attachToRecyclerView(recyclerView);
            // createButtonListInLayout(dbAdapterMain.getAllRowsMain());
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

    private ItemTouchHelper.SimpleCallback createCallBack() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem((long) viewHolder.itemView.getTag(), position);
            }
        };
    }

    private void removeItem(long id, int position) {
        Cursor c = dbAdapterMain.getRowMainByID(id);
        String tableName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
        dbAdapterMain.deleteRowMainByID(id);
        dbAdapter.deleteTable(tableName);
        rvAdapter.data.remove(position);
        rvAdapter.notifyItemRemoved(position);
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

    public static List<item_data> getData(Cursor c) {
        List<item_data> data = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                item_data row = new item_data();
                row.listName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
                row.language1 = c.getString(DBAdapter.COL_MAIN_LANGUAGE_1);
                row.language2 = c.getString(DBAdapter.COL_MAIN_LANGUAGE_2);
                row.id = c.getInt(DBAdapter.COL_ROWID_MAIN);
                data.add(row);
            } while (c.moveToNext());
        }
        return data;
    }

    public void editList(int pos, List<item_data> data) {
        String table_name = data.get(pos).listName;
        String language1 = data.get(pos).language1;
        String language2 = data.get(pos).language2;
        Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
        i.putExtra("tableName", table_name);
        i.putExtra("lan1", language1);
        i.putExtra("lan2", language2);
        startActivity(i);
    }

    public void questionTheList(int pos, List<item_data> data) {
        String table_name = data.get(pos).listName;
        if (dbAdapter.getRowCount(table_name) != 0) {
            Intent i = new Intent(getActivity(), question.class);
            i.putExtra("tableName", table_name);
            startActivity(i);
        } else {
            Toast.makeText(getContext(), "Er moeten wel woorden in jouw lijst staan opgeslagen!", Toast.LENGTH_SHORT).show();
        }
    }

    /*
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

    } */

}
