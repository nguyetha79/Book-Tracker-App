package de.ur.mi.android.booktrackerapp.Fragments.Search;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import androidx.fragment.app.Fragment;


import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;


public class AddFragment extends Fragment {

    private String title, author, cover, language, status;
    private int numPages, currPageAdd;
    private double rating;

    private TextView tvTitleAdd;
    private Spinner spinnerAdd;
    private EditText currPageInputAdd;
    private Button addButton;
    private LinearLayout linearLayoutCurrPageAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);;

        getAndSetData(view);

        initSpinner();

        initBtnAdd();

        return view;
    }


    private void getAndSetData(View view) {
        linearLayoutCurrPageAdd = view.findViewById(R.id.Llayout_curr_page_add);
        tvTitleAdd = view.findViewById(R.id.tv_title_content_add);
        spinnerAdd = view.findViewById(R.id.spinner_add);
        currPageInputAdd = view.findViewById(R.id.editText_curr_page_add);
        addButton = view.findViewById(R.id.btn_add);

        getIntentData();

        tvTitleAdd.setText(title);
    }

    private void getIntentData() {
        Bundle arguments = getArguments();
        title = arguments.getString("title");
        author = arguments.getString("author");
        cover = arguments.getString("cover");
        rating = arguments.getDouble("rating", 0.0);
        numPages = arguments.getInt("numPages", 0);
        language = arguments.getString("language");
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdd.setAdapter(spinnerAdapter);

        spinnerAdd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                status = parent.getItemAtPosition(position).toString();

                TextView tvStatus = ((TextView) parent.getChildAt(0));
                tvStatus.setTextColor(Color.parseColor("#8D4C2E"));

                Typeface typeface = getResources().getFont(R.font.poppins_medium);
                tvStatus.setTypeface(typeface);
                tvStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

                setUpValueCurrPageAdd();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpValueCurrPageAdd() {
        switch (status) {
            case "To read":
                currPageAdd = 0;
                linearLayoutCurrPageAdd.setVisibility(View.GONE);
                break;
            case "Reading":
                linearLayoutCurrPageAdd.setVisibility(View.VISIBLE);
                break;
            case "Read":
                currPageAdd = numPages;
                linearLayoutCurrPageAdd.setVisibility(View.GONE);
                break;
        }
    }

    private void initBtnAdd() {
        addButton.setOnClickListener(view -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());

            if (linearLayoutCurrPageAdd.getVisibility() == View.VISIBLE){
                currPageAdd = getValueFromEditText(currPageInputAdd);
            }
            myDB.addData(title, author, cover, rating, numPages, language, status, currPageAdd);
        });
    }

    private int getValueFromEditText(EditText editText) {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            Number value = format.parse(editText.getText().toString());
            if (value != null) {
                return value.intValue();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}