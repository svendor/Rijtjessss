package editWordList;

// TODO: Verander andere fragments zodat ze ook de goeie dingen doen, zoals hier.

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import layout.displayList;

public class editWordList extends Fragment {
    public static final String TABLE_NAME = "param1";

    private String mParam1;

    private List<item_word_to_word> data;

    RecyclerView recyclerView;

    DBAdapter dbAdapter;
    DBAdapter dbAdapterMain;

    EWLRVAdapter ewlRVAdapter;

    View rootView;

    Toolbar toolbar;
    Menu menu;

    public editWordList() {
        // Required empty public constructor
    }

    public editWordList newInstance(String param1) {
        editWordList fragment = new editWordList();
        Bundle args = new Bundle();
        args.putString(TABLE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openDB();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TABLE_NAME);
            data = getData(dbAdapter.getAllRows(mParam1));
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_word_list, container, false);

        setupToolbar();
        setupRecyclerView();

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.findItem(R.id.action_save_list).setVisible(true);
        menu.findItem(R.id.action_add_row).setVisible(true);
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
            case(R.id.action_save_list):
                saveList();
                return true;
            case(R.id.action_add_row):
                addRow();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void setupRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recyclerViewEWL);
        ewlRVAdapter = new EWLRVAdapter(getContext(), data, this);
        recyclerView.setAdapter(ewlRVAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper ith = new ItemTouchHelper(createCallBack());
        ith.attachToRecyclerView(recyclerView);
    }

    private void setupToolbar() {
        toolbar = (Toolbar) ((NavMenu)getActivity()).findViewById(R.id.toolbar);
        toolbar.setTitle(mParam1);
        NavMenu navMenu = (NavMenu) getActivity();
        navMenu.setSupportActionBar(toolbar);
    }

    private static List<item_word_to_word> getData(Cursor c) {
        List<item_word_to_word> data = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                item_word_to_word row = new item_word_to_word("", "");
                row.wordLan1 = c.getString(DBAdapter.COL_ANSWER);
                row.wordLan2 = c.getString(DBAdapter.COL_QUESTION);
                data.add(row);
            } while (c.moveToNext());
        }
        c.close();
        return data;
    }

    private ItemTouchHelper.SimpleCallback createCallBack() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos = target.getAdapterPosition();
                Collections.swap(ewlRVAdapter.data, fromPos, toPos);
                ewlRVAdapter.notifyItemMoved(fromPos, toPos);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(position);
            }
        };
    }

    private void removeItem(int position) {
        ewlRVAdapter.data.remove(position);
        ewlRVAdapter.notifyItemRemoved(position);
    }

    private void addRow() {
        Toast.makeText(getContext(), "Rij toegevoegd", Toast.LENGTH_LONG).show();
        int lastPos = ewlRVAdapter.getItemCount();
        ewlRVAdapter.data.add(lastPos, new item_word_to_word("", ""));
        ewlRVAdapter.notifyItemInserted(lastPos);
    }

    private void saveList() {
        dbAdapter.deleteAll(mParam1);
        //dbAdapterMain.deleteRowMain(mParam1);
        List<item_word_to_word> data = ewlRVAdapter.data;

        if (ewlRVAdapter.getItemCount() == 0) {
            dbAdapterMain.deleteRowMain(mParam1);
            Toast.makeText(getContext(), "Lijst verwijderd", Toast.LENGTH_LONG).show();
            FragmentManager manager = getFragmentManager();
            manager.beginTransaction().replace(R.id.relativelayout_fragment, new displayList()).commit();
        } else {
            for (item_word_to_word row : ewlRVAdapter.data) {
                dbAdapter.insertRow(row.wordLan1, row.wordLan2, mParam1);
            }
        }

        Toast.makeText(getContext(), "Lijst opgeslagen", Toast.LENGTH_LONG).show();
        getFragmentManager().beginTransaction().replace(R.id.relativelayout_fragment, new displayList()).commit();
    }
}
