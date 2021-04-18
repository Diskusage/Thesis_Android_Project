package com.example.sqliteapp.ui.gallery;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sqliteapp.DatabaseHelper;
import com.example.sqliteapp.Logged_in_2nd;
import com.example.sqliteapp.PersonModel;
import com.example.sqliteapp.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GalleryFragment extends Fragment {
    String idnp;
    private GalleryViewModel galleryViewModel;
    DatabaseHelper databaseHelper;
    ListView historyLView;
    ArrayAdapter vaccArrayAdapter;
    PopupWindow popupWindow;
    LinearLayout layout, mainLayout;
    ImageView imageView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logged_in_2nd activity = (Logged_in_2nd) getActivity();
        assert activity != null;
        Bundle extras = activity.getExtras();
        idnp = extras.getString("IDNP");
        imageView = new ImageView(getActivity());
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        historyLView = root.findViewById(R.id.history_id);
        popupWindow = new PopupWindow(getActivity());
        layout = new LinearLayout(getActivity());
        mainLayout = new LinearLayout(getActivity());
        historyLView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonModel clickedPerson = (PersonModel) parent.getItemAtPosition(position);
                String idnp = clickedPerson.getIDNP();
                String vacc_type = clickedPerson.getType().toString();
                String vacc_date = clickedPerson.getVacc_date();
                String testQr = idnp + " " + vacc_type + " " + vacc_date;
                try{
                    MultiFormatWriter writer = new MultiFormatWriter();
                    BitMatrix bm = writer.encode(testQr, BarcodeFormat.QR_CODE, 350, 350);
                    BarcodeEncoder bce = new BarcodeEncoder();
                    Bitmap bitmap = bce.createBitmap(bm);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                popupWindow.showAtLocation(layout, Gravity.BOTTOM, 40, 40);
                popupWindow.update(50, 300, 400, 400);

            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(imageView, params);
        popupWindow.setContentView(layout);
        showHistory();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        popupWindow.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        showHistory();
    }

    public void showHistory(){
        databaseHelper = new DatabaseHelper(getActivity());
        vaccArrayAdapter = new ArrayAdapter<PersonModel>(getActivity(), android.R.layout.simple_list_item_1, databaseHelper.getAll1Person(idnp));
        historyLView.setAdapter(vaccArrayAdapter);
    }
    public void generateQRCodeForChoseItem(){

    }
}