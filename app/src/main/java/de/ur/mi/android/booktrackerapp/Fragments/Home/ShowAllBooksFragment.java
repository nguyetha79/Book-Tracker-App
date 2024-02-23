package de.ur.mi.android.booktrackerapp.Fragments.Home;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import de.ur.mi.android.booktrackerapp.Adapters.ShowAllBooksAdapter;
import de.ur.mi.android.booktrackerapp.Model.BookItemModel;
import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class ShowAllBooksFragment extends Fragment implements View.OnLongClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MyDatabaseHelper myDB;
    private ArrayList<BookItemModel> bookItemsList, selectionList;
    private ArrayList<String> ids, titles, authors, covers, ratings,
            numPages, languages, status, currPages, notes;
    private ShowAllBooksAdapter adapter;
    private BookItemModel bookItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_show_all_books, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_show_books);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_show_books);
        setUpSwipeRefresherLayout();

        return view;
    }

    private void setUpSwipeRefresherLayout() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            myDB = new MyDatabaseHelper(getContext());
            setUpArrayList();
            setUpBookItemsList();
            setUpAdapterShowAllBooks();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setUpArrayList() {
        bookItemsList = new ArrayList<>();
        selectionList = new ArrayList<>();
        ids = new ArrayList<>();
        titles = new ArrayList<>();
        authors = new ArrayList<>();
        covers = new ArrayList<>();
        ratings = new ArrayList<>();
        numPages = new ArrayList<>();
        languages = new ArrayList<>();
        status = new ArrayList<>();
        currPages = new ArrayList<>();
        notes = new ArrayList<>();
    }

    private void setUpBookItemsList() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                ids.add(cursor.getString(0));
                titles.add(cursor.getString(1));
                authors.add(cursor.getString(2));
                covers.add(cursor.getString(3));
                ratings.add(cursor.getString(4));
                numPages.add(cursor.getString(5));
                languages.add(cursor.getString(6));
                status.add(cursor.getString(7));
                currPages.add(cursor.getString(8));
                notes.add(cursor.getString(9));
            }
        }

        for (int i = 0; i < titles.size() ; i++) {
            bookItem = new BookItemModel(
                    Integer.parseInt(ids.get(i)),
                    titles.get(i),
                    authors.get(i),
                    covers.get(i),
                    Double.parseDouble(ratings.get(i)),
                    Integer.parseInt(numPages.get(i)),
                    languages.get(i),
                    status.get(i),
                    Integer.parseInt(currPages.get(i)),
                    notes.get(i));
            bookItemsList.add(bookItem);
            Log.d(String.valueOf(bookItem.getCurrPage()), "currPageShowAllBooks");
        }
    }

    private void setUpAdapterShowAllBooks() {
        int numCols = 2;
        adapter = new ShowAllBooksAdapter(this, bookItemsList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numCols));
    }

    public void setUpSelectionList(View view, int adapterPosition) {
        if (((CheckBox)view).isChecked()){
            selectionList.add(bookItemsList.get(adapterPosition));
        } else {
            selectionList.remove(bookItemsList.get(adapterPosition));
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder mb = (MenuBuilder) menu;
            mb.setOptionalIconsVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.search_filter){
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        if (item.getItemId() == R.id.delete_all){
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            myDB.deleteAll();
        }

        if (item.getItemId() == R.id.delete_multi){
            for (int i = 0; i < selectionList.size(); i++) {
                myDB.deleteData(selectionList.get(i));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View view) {
        adapter.notifyDataSetChanged();
        return true;
    }

}