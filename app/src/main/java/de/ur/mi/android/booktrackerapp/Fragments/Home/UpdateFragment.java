package de.ur.mi.android.booktrackerapp.Fragments.Home;

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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;

public class UpdateFragment extends Fragment {

    private String title;
    private String statusUpdate;
    private String noteUpdate;
    private int currPageUpdate, numPages;

    private TextView tvTitleBookUpdate, tvStatusUpdate;
    private Spinner spinnerUpdate;
    private LinearLayout linearLayoutCurrPageUpdate;
    private EditText currPageInputUpdate;
    private EditText noteInputUpdate;
    private Button updateBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        getAndSetData(view);

        initSpinner();
        initBtnUpdate();
        return view;
    }

    private void getAndSetData(View view) {
        tvTitleBookUpdate = view.findViewById(R.id.tv_title_content_update);
        spinnerUpdate = view.findViewById(R.id.spinner_update);
        linearLayoutCurrPageUpdate = view.findViewById(R.id.Llayout_curr_page_update);
        currPageInputUpdate = view.findViewById(R.id.tv_num_curr_page_update);
        noteInputUpdate = view.findViewById(R.id.editText_note_content);
        updateBtn = view.findViewById(R.id.btn_update_book);

        Bundle arguments = getArguments();
        title = arguments.getString("title");
        numPages = arguments.getInt("numPages", 0);
        statusUpdate = arguments.getString("status");
        currPageUpdate = arguments.getInt("currPage", 0);

        tvTitleBookUpdate.setText(title);
        noteUpdate = noteInputUpdate.getText().toString();
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.status, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUpdate.setAdapter(spinnerAdapter);
        spinnerUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                statusUpdate = parent.getItemAtPosition(position).toString();

                tvStatusUpdate = ((TextView) parent.getChildAt(0));
                tvStatusUpdate.setTextColor(Color.parseColor("#8D4C2E"));

                Typeface typeface = getResources().getFont(R.font.poppins_medium);
                tvStatusUpdate.setTypeface(typeface);
                tvStatusUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f);

                setUpValueCurrPageUpdate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initBtnUpdate() {
        updateBtn.setOnClickListener(view -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());

            statusUpdate = tvStatusUpdate.getText().toString();
            setUpValueCurrPageUpdate();
            if (linearLayoutCurrPageUpdate.getVisibility() == View.VISIBLE){
                currPageUpdate = getValueFromEditText(currPageInputUpdate);
            }
            noteUpdate = noteInputUpdate.getText().toString();

            myDB.updateData(title, statusUpdate, currPageUpdate, noteUpdate);
        });
    }


    private void setUpValueCurrPageUpdate() {
        switch (statusUpdate) {
            case "To read":
                currPageUpdate = 0;
                linearLayoutCurrPageUpdate.setVisibility(View.GONE);
                break;
            case "Reading":
                linearLayoutCurrPageUpdate.setVisibility(View.VISIBLE);
                break;
            case "Read":
                currPageUpdate = numPages;
                linearLayoutCurrPageUpdate.setVisibility(View.GONE);
                break;
        }
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