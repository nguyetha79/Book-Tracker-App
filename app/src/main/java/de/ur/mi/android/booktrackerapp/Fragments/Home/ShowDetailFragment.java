package de.ur.mi.android.booktrackerapp.Fragments.Home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.ur.mi.android.booktrackerapp.R;
import de.ur.mi.android.booktrackerapp.SQLite.MyDatabaseHelper;


public class ShowDetailFragment extends Fragment {

    private String title, author, coverLink, language, status, note;
    private int id, numPages, currPage, progress;
    private double rating, latitude, longtitude;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 1;

    private TextView tvTitleBookInfos, tvAuthorBookInfos, tvNoteContentBookInfos;
    private ImageView ivCoverBookInfos;
    private TextView tvNumRating, tvNumPages, tvLang, tvCurrStatus;
    private Button btnUpdate, btnDelete, btnLaunchMap;
    private Slider slider;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);

        getAndSetData(view);

        initSlider();
        initBtnLaunchMap();
        initBtnUpdate();
        initBtnDelete();

        return view;
    }

    private void getAndSetData(View view) {
        ivCoverBookInfos = view.findViewById(R.id.imageView_cover_book_infos);
        slider = view.findViewById(R.id.slider);

        tvTitleBookInfos = view.findViewById(R.id.tv_title_book_infos);
        tvAuthorBookInfos = view.findViewById(R.id.tv_author_book_infos);
        tvNumRating = view.findViewById(R.id.num_rating);
        tvNumPages = view.findViewById(R.id.num_pages);
        tvLang = view.findViewById(R.id.lang_content);
        tvCurrStatus = view.findViewById(R.id.tv_curr_status_book_infos);
        tvNoteContentBookInfos = view.findViewById(R.id.tv_note_content_book_infos);

        btnLaunchMap = view.findViewById(R.id.btn_launch_map);
        btnUpdate = view.findViewById(R.id.btn_update_book_infos);
        btnDelete = view.findViewById(R.id.btn_delete);

        getIntentData();
        setUpData();
    }

    private void getIntentData() {
        Bundle args = getArguments();
        id = args.getInt("id", 0);
        title = args.getString("title");
        author = args.getString("author");
        coverLink = args.getString("cover");
        rating = args.getDouble("rating", 0.0);
        numPages = args.getInt("numPages", 0);
        language = args.getString("language");
        status = args.getString("status");
        progress = args.getInt("progress", 0);
        currPage = args.getInt("currPage", 0);
        note = args.getString("note");
        Log.d("success", "getIntentData: ");
    }

    private void setUpData() {
        tvTitleBookInfos.setText(title);
        tvAuthorBookInfos.setText(author);

        if (coverLink != null && coverLink.equals("No cover")) {
            String uri = "@drawable/no_book_cover_available";
            int imageResource = getContext().getResources()
                    .getIdentifier(uri, null, getContext().getPackageName());
            Drawable res = getContext().getResources().getDrawable(imageResource);
            ivCoverBookInfos.setImageDrawable(res);
        } else {
            Picasso.get().load(coverLink).into(ivCoverBookInfos);
        }

        String strRating = Double.toString(rating);
        tvNumRating.setText(strRating);

        String strNumPages = Integer.toString(numPages);
        tvNumPages.setText(strNumPages);

        tvLang.setText(language);
        tvCurrStatus.setText(status);

        tvNoteContentBookInfos.setText(note);
        Log.d("success", "setUpData: ");

    }

    private void initSlider() {
        slider.setValue(progress);
    }

    private void initBtnLaunchMap() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        btnLaunchMap.setOnClickListener(view -> getLastLocation());

    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                latitude = addresses.get(0).getLatitude();
                                longtitude = addresses.get(0).getLongitude();
                                launchGoogleMap();
                                Log.d(String.valueOf(latitude), "getLastLocation");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            } else {
                Toast.makeText(getContext(), "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchGoogleMap() {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longtitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void initBtnUpdate() {
        btnUpdate.setOnClickListener(view -> {
            Bundle bundle = new Bundle();

            bundle.putString("title", title);
            bundle.putInt("numPages", numPages);
            bundle.putString("status", status);
            bundle.putInt("currPage", currPage);

            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            UpdateFragment updateFragment = new UpdateFragment();
            updateFragment.setArguments(bundle);

            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,updateFragment).commit();
        });
    }

    private void initBtnDelete() {
        btnDelete.setOnClickListener(view -> {
            confirmDialog();
        });
    }

    private void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete this book ?");
        builder.setMessage("Are you sure that you want to delete " + title + " ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            MyDatabaseHelper myDB = new MyDatabaseHelper(getContext());
            myDB.deleteData(id);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();
    }
}