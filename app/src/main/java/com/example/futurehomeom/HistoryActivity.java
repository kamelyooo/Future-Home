package com.example.futurehomeom;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Flow;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.squareup.picasso.Picasso;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryActivity extends AppCompatActivity {
 ListView lv_history;
    TextView cm_HeightHIstory,cm_WidthHIstory,cm_TitleHistory,cm_DateHistory,cm_HistoryState;
    ImageView cm_imageHistory;
    List<Order> orders;
    ProgressBar progress;
    CubeGrid doubleBounce;
    SharedPreferences preferencess;
    int lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.MyHistory));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_im);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_history);
         lv_history=findViewById(R.id.lv_History);
         Flow noHistory =findViewById(R.id.noHistory);
        noHistory.setVisibility(View.INVISIBLE);
        progress = (ProgressBar)findViewById(R.id.spin_kit);
        preferencess = getSharedPreferences("lang", Context.MODE_PRIVATE);


        lang = preferencess.getInt("lang", 0);
        doubleBounce = new CubeGrid();
        progress.setIndeterminateDrawable(doubleBounce);

        progress.setVisibility(View.VISIBLE);

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setPageSize( 100 );
        Backendless.Data.of(Order.class).find ( queryBuilder, new AsyncCallback<List<Order>>() {
            @Override
            public void handleResponse(List<Order> response) {
                lv_history.setVisibility(View.VISIBLE);

                progress.setVisibility(View.GONE);
                orders = response.stream().sorted(Comparator.comparing(order -> order.created)).collect(Collectors.toList());
                historyadapter adapter=new historyadapter(HistoryActivity.this,orders);
                if (adapter.getCount()> 0 )
                    noHistory.setVisibility(View.GONE);
                else if(adapter.getCount()==0){
                    noHistory.setVisibility(View.VISIBLE);
                }

                lv_history.setAdapter(adapter);
            }
            @Override
            public void handleFault(BackendlessFault fault) {
                ImageView errorImage = findViewById(R.id.errorImage);
                noHistory.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                errorImage.setVisibility(View.VISIBLE);
            }
        });
    }
    class historyadapter extends ArrayAdapter<Order>  {
        public historyadapter(@NonNull Context context,  List<Order> orders) {
            super(context,0,orders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cm_historyactivty, parent, false);
            cm_DateHistory = convertView.findViewById(R.id.cm_dateHistory);
            cm_TitleHistory = convertView.findViewById(R.id.cm_titleHistory);
            cm_HeightHIstory = convertView.findViewById(R.id.cm_HeightHistory);
            cm_WidthHIstory = convertView.findViewById(R.id.cm_WidthHistory);
            cm_imageHistory = convertView.findViewById(R.id.cm_imageHistory);
            cm_HistoryState=convertView.findViewById(R.id.cm_HistoryState);


            if (lang==1){
                cm_TitleHistory.setText(getItem(position).titleAr);
                if(getItem(position).stateAr.equals("انتهى"))
                    cm_HistoryState.setTextColor(Color.parseColor("#1faa00"));
                else if (getItem(position).stateAr.equals("جارى التنفيذ"))
                    cm_HistoryState.setTextColor(Color.parseColor("#fdd835"));
                else cm_HistoryState.setTextColor(Color.parseColor("#9b0000"));
                cm_HistoryState.setText(getItem(position).stateAr);
            }else {

                if(getItem(position).state.equals("Done"))
                cm_HistoryState.setTextColor(Color.parseColor("#1faa00"));
                else if (getItem(position).state.equals("Processing"))
                    cm_HistoryState.setTextColor(Color.parseColor("#fdd835"));
                else cm_HistoryState.setTextColor(Color.parseColor("#9b0000"));


                cm_TitleHistory.setText(getItem(position).title);
                cm_HistoryState.setText(getItem(position).state);
            }

            cm_DateHistory.setText(getItem(position).created.substring(0,11));
            cm_HeightHIstory.setText(getItem(position).height+"");
            cm_WidthHIstory.setText(getItem(position).width+"");


            Picasso.get().load(getItem(position).url).resize(150,150).into(cm_imageHistory);
            cm_imageHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(HistoryActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                    PhotoView photoView = mView.findViewById(R.id.photo_view);
                    Picasso.get().load(getItem(position).url).into(photoView);
                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
            return convertView;

        }


    }
}