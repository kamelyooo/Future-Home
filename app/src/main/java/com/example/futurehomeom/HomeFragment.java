package com.example.futurehomeom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.examples.marmoush.data.News;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.mateware.snacky.Snacky;


public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageView receptionsImage, corridorsImage, bedroomsImage, kitchensImage, bathroomsImage, UnitsAndLibraryImage, livingroomsImage;
    CarouselView carouselView;
    List<String> urls;
    HomeFragment homeFragment;
    ScrollView scrollViewHOme;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        receptionsImage = view.findViewById(R.id.ImageReceptions);
        corridorsImage = view.findViewById(R.id.imageCorridors);
        bedroomsImage = view.findViewById(R.id.imageBedrooms);
        kitchensImage = view.findViewById(R.id.imageKitchens);
        bathroomsImage = view.findViewById(R.id.imageBathrooms);
        UnitsAndLibraryImage = view.findViewById(R.id.imageUnitsAndLibrary);
        livingroomsImage = view.findViewById(R.id.imagelivingrooms);
        scrollViewHOme = view.findViewById(R.id.scrollViewHome);

        receptionsImage.setImageResource(R.drawable.reception);
        corridorsImage.setImageResource(R.drawable.coridor);
        bedroomsImage.setImageResource(R.drawable.bedroom);
        kitchensImage.setImageResource(R.drawable.kitchen);
        bathroomsImage.setImageResource(R.drawable.bathroom);
        UnitsAndLibraryImage.setImageResource(R.drawable.tvunits);
        livingroomsImage.setImageResource(R.drawable.living);
        carouselView = (CarouselView) view.findViewById(R.id.carouselView);

        receptionsImage.setOnClickListener(this);
        corridorsImage.setOnClickListener(this);
        bedroomsImage.setOnClickListener(this);
        kitchensImage.setOnClickListener(this);
        bathroomsImage.setOnClickListener(this);
        UnitsAndLibraryImage.setOnClickListener(this);
        livingroomsImage.setOnClickListener(this);


        carouselView.setImageListener(imageListener);


        Backendless.Data.of(News.class).find(new AsyncCallback<List<News>>() {
            @Override
            public void handleResponse(List<News> response) {
//                scrollViewHOme.setClickable(true);
                urls = response.stream().map(showitem -> showitem.getImageUrl()).collect(Collectors.toList());
                Collections.shuffle(urls);
                carouselView.setPageCount(urls.size());

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.i("xxxx", fault.getCode());
//                if (getActivity() != null){
                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .setActionText(R.string.Retry).setActionClickListener(v1 -> {
                        if (homeFragment == null)
                            homeFragment = new HomeFragment();
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.countaner, homeFragment, "hometag").commit();

                        }

                    })
                            .setText(R.string.pleaseCheckYourInternet)
                            .info().show();
//            }
            }
        });

        return view;
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
            Picasso.get().load(urls.get(position)).into(imageView);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ImageReceptions) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Receptions");
            in.putExtra("typeAr", "غرف استقبال");

            startActivity(in);
        } else if (v.getId() == R.id.imageCorridors) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Corridors");
            in.putExtra("typeAr", "طرقات");
            startActivity(in);
        } else if (v.getId() == R.id.imageBedrooms) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Bedrooms");
            in.putExtra("typeAr", "غرف نوم");
            startActivity(in);
        } else if (v.getId() == R.id.imageKitchens) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Kitchens");
            in.putExtra("typeAr", "مطابخ");
            startActivity(in);
        } else if (v.getId() == R.id.imageBathrooms) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Bathrooms");
            in.putExtra("typeAr", "حمامات");
            startActivity(in);
        } else if (v.getId() == R.id.imageUnitsAndLibrary) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "UnitsAndLibrarys");
            in.putExtra("typeAr", "مكتبات");
            startActivity(in);
        } else if (v.getId() == R.id.imagelivingrooms) {
            Intent in = new Intent(getActivity(), typeactivity.class);
            in.putExtra("type", "Livingrooms");
            in.putExtra("typeAr", "غرف معيشه");
            startActivity(in);
        }
    }


}//        receptionsImage.setOnClickListener(v -> {
//            Intent in=new Intent(getActivity(),typeactivity.class);
//            in.putExtra("type","Receptions");
//            startActivity(in);
//        });
//
//        bedroomsImage.setOnClickListener(v -> {
//            Intent in=new Intent(getActivity(),typeactivity.class);
//            in.putExtra("type","Bedrooms");
//            startActivity(in);
//        });
//
//        kitchensImage.setOnClickListener(v -> {
//            Intent in=new Intent(getActivity(),typeactivity.class);
//            in.putExtra("type","Kitchens");
//            startActivity(in);
//        });
//
//        bathroomsImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in=new Intent(getActivity(),typeactivity.class);
//                in.putExtra("type","Bathrooms");
//                startActivity(in);
//            }
//        });
//
//
//        UnitsAndLibraryImage.setOnClickListener(v -> {
//            Intent in=new Intent(getActivity(),typeactivity.class);
//            in.putExtra("type","UnitsAndLibrarys");
//            startActivity(in);
//        });
//
//        livingroomsImage.setOnClickListener(v -> {
//            Intent in=new Intent(getActivity(),typeactivity.class);
//            in.putExtra("type","Livingrooms");
//            startActivity(in);
//        });
//
//        corridorsImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in=new Intent(getActivity(),typeactivity.class);
//                in.putExtra("type","Corridors");
//                startActivity(in);
//            }
//        });