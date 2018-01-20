package layout;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.absontwikkeling.rijtjes.DBAdapter;
import com.absontwikkeling.rijtjes.NavMenu;
import com.absontwikkeling.rijtjes.R;
import com.absontwikkeling.rijtjes.question;
import editWordList.editWordList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class displayList extends Fragment {

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    public List<item_data> data;
    public boolean deleteConfirmed = false;
    View view;
    Toolbar toolbar;
    Menu menu;
    // TextView showList;

    public displayList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_list, container, false);

        toolbar = (Toolbar) ((NavMenu)getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.display_list_page_name);
        ((NavMenu) getActivity()).setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDL);

        // Opens database
        openDB();
        // Ensures there's no NullPointerException
        dbAdapterMain.createTableMain();
        // Gets data from database
        final List<item_data> data = getData(dbAdapterMain.getAllRowsMain());

        // Populates recyclerView with items
        if (dbAdapterMain.getAllRowsMain() != null && data.size() != 0) {
            rvAdapter = new RVAdapter(getContext(), data, this);
            recyclerView.setAdapter(rvAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            ItemTouchHelper ith = new ItemTouchHelper(createCallBack());
            ith.attachToRecyclerView(recyclerView);
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        // menu.findItem(R.id.app_bar_search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case(R.id.action_settings):
                layout.settings settings = new layout.settings();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.relativelayout_fragment, settings).commit();
                return true;
            case(R.id.app_bar_search):
                Toast.makeText(getContext(), "Gezocht", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                showUndoSnackbar((long) viewHolder.itemView.getTag(), position, rvAdapter.data.get(position));
            }
        };
    }

    private void showUndoSnackbar(final long id, final int position, final item_data item) {
        Snackbar.make(view, "Weet je zeker dat je deze lijst wilt verwijderen?", Snackbar.LENGTH_LONG).setAction("Ja", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeItem(id, position);
                    }
                }
        ).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                rvAdapter.data.remove(position);
                rvAdapter.notifyItemRemoved(position);
                if (!deleteConfirmed) {
                    rvAdapter.data.add(position, item);
                    rvAdapter.notifyItemInserted(position);
                }
            }
        }).show();
    }

    private void removeItem(long id, int position) {
        Cursor c = dbAdapterMain.getRowMainByID(id);
        String tableName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
        dbAdapterMain.deleteRowMainByID(id);
        dbAdapter.deleteTable(tableName);
        deleteConfirmed = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        if (c.moveToLast()) {
            do {
                item_data row = new item_data();
                row.listName = c.getString(DBAdapter.COL_TABLE_NAME_MAIN);
                row.language1 = c.getString(DBAdapter.COL_MAIN_LANGUAGE_1);
                row.language2 = c.getString(DBAdapter.COL_MAIN_LANGUAGE_2);
                row.id = c.getInt(DBAdapter.COL_ROWID_MAIN);
                data.add(row);
            } while (c.moveToPrevious());
        }
        return data;
    }

    public void editList(int pos, List<item_data> data) {
        editWordList ewl = new editWordList();
        Bundle args = new Bundle();
        String arg1 = data.get(pos).listName;
        args.putString(editWordList.TABLE_NAME, arg1);
        ewl.setArguments(args);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.relativelayout_fragment, ewl).commit();

        /*
        String table_name = data.get(pos).listName;
        String language1 = data.get(pos).language1;
        String language2 = data.get(pos).language2;
        Intent i = new Intent(getActivity(), editWordListACTIVITY.class);
        i.putExtra("tableName", table_name);
        i.putExtra("lan1", language1);
        i.putExtra("lan2", language2);
        startActivity(i);
         */
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
}
