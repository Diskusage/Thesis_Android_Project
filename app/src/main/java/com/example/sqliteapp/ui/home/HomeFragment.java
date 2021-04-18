package com.example.sqliteapp.ui.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sqliteapp.Logged_in_2nd;
import com.example.sqliteapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ImageView imageView = root.findViewById(R.id.qr_code);
        final TextView textView = root.findViewById(R.id.text_home);
        Logged_in_2nd activity = (Logged_in_2nd) getActivity();
        assert activity != null;
        Bundle extras = activity.getExtras();
        String vacc_type = extras.getString("VaccinationType");
        String vacc_date = extras.getString("VaccinationDate");
        String idnp = extras.getString("IDNP");
        String testQr = idnp + " " + vacc_type + " " + vacc_date;
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bm = writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder bce = new BarcodeEncoder();
            Bitmap bitmap = bce.createBitmap(bm);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e){

        }
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        return root;
    }
}