package com.example.futurehomeom;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.Inflater;

import de.mateware.snacky.Snacky;
import es.dmoral.toasty.Toasty;


public class typeactivity extends AppCompatActivity {
//    ListView lv_types;
//    TextView cm_title, cm_desc;
//    ImageView cm_image;
    RecyclerView recyclerView;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    String type,typeAr;
//    Showitem showitem;
    private List<Showitem> responsee;
//    myadapter adapter;
recycleadapter adapter;
    List<Showitem> searchList;
    List<Showitem> searchListAr;

    String desc="";
    ProgressBar bar;
    CubeGrid doubleBounce;
    SharedPreferences preferencess;
    int lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeactivity);
        bar = (ProgressBar) findViewById(R.id.spin_kit);
        doubleBounce = new CubeGrid();
        bar.setIndeterminateDrawable(doubleBounce);
        recyclerView = findViewById(R.id.recyclerView);

        bar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        preferencess = getSharedPreferences("lang", Context.MODE_PRIVATE);

        lang = preferencess.getInt("lang", 0);
//        lv_types = findViewById(R.id.lv_types);
        type = getIntent().getStringExtra("type");
        typeAr = getIntent().getStringExtra("typeAr");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_im);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        if (lang==1)Objects.requireNonNull(getSupportActionBar()).setTitle(" "+typeAr);
        else Objects.requireNonNull(getSupportActionBar()).setTitle(" "+type);


        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause("title='" + type + "'");
        queryBuilder.setPageSize(100);
        Backendless.Data.of(Showitem.class).find(queryBuilder, new AsyncCallback<List<Showitem>>() {

            @Override
            public void handleResponse(List<Showitem> response) {
                bar.setVisibility(View.GONE);
                   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Collections.shuffle(response);
                responsee = response;
                recyclerView.setLayoutManager(new LinearLayoutManager(typeactivity.this));
                adapter = new recycleadapter(responsee);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                bar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toasty.error(typeactivity.this,getString( R.string.NetworkError), Toast.LENGTH_SHORT, true).show();
            }
        });

    }
    class recycleadapter extends RecyclerView.Adapter<recycleadapter.TypeViewHolder> {

        public List<Showitem>showitemArrayList;

        public recycleadapter(List<Showitem> s){

            showitemArrayList=s;
        }

        @NonNull
        @Override
        public recycleadapter.TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TypeViewHolder(LayoutInflater.from(typeactivity.this).inflate(R.layout.cm_type, parent, false));

        }

        @Override
        public void onBindViewHolder(@NonNull recycleadapter.TypeViewHolder holder, int position) {

            if (lang==1){
                holder.cm_title.setText(showitemArrayList.get(position).getTitleAr());
                desc = showitemArrayList.get(position).getDescAr();
                if (desc.length() > 50)
                    holder.cm_desc.setText(showitemArrayList.get(position).getDescAr().substring(0,50) + " ......");
                else
                    holder.cm_desc.setText(showitemArrayList.get(position).getDescAr());
            }else{
                holder.cm_title.setText(showitemArrayList.get(position).getTitle());
//            holder.cm_desc.setText(showitemArrayList.get(position).getDesc());

                desc = showitemArrayList.get(position).getDesc();
                if (desc.length() > 50)
                    holder.cm_desc.setText(showitemArrayList.get(position).getDesc().substring(0,50) + " ......");
                else
                    holder.cm_desc.setText(showitemArrayList.get(position).getDesc());
            }

            Picasso.get().load(showitemArrayList.get(position).getImageUrl()).into(holder.cm_image);
            holder.cm_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(typeactivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                    PhotoView photoView = mView.findViewById(R.id.photo_view);
                    Picasso.get().load(showitemArrayList.get(position).getImageUrl()).into(photoView);
                    mBuilder.setView(mView);
                    AlertDialog mDialog = mBuilder.create();
                    mDialog.show();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Showitem showitem = showitemArrayList.get(position);

                    Intent in = new Intent(typeactivity.this, itemDetailsActivity.class);
                    in.putExtra("showitem", showitem);
                    startActivity(in);
                }
            });
        }

        @Override
        public int getItemCount() {
            return showitemArrayList.size();
        }

        public class TypeViewHolder extends RecyclerView.ViewHolder {

            TextView cm_title, cm_desc;
            ImageView cm_image;

            public TypeViewHolder(@NonNull View itemView) {
                super(itemView);
                cm_title = itemView.findViewById(R.id.cm_title);
                cm_desc = itemView.findViewById(R.id.cm_decr);
                cm_image = itemView.findViewById(R.id.cm_image);


            }
        }
    }

//    class myadapter extends ArrayAdapter<Showitem> {
//        public myadapter(@NonNull Context context, List<Showitem> response) {
//            super(context, 0, response);
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//            if (convertView == null)
//                convertView = getLayoutInflater().inflate(R.layout.cm_type, parent, false);
//
//            cm_title = convertView.findViewById(R.id.cm_title);
//            cm_desc = convertView.findViewById(R.id.cm_decr);
//            cm_image = convertView.findViewById(R.id.cm_image);
//
//            cm_title.setText(getItem(position).getTitle());
//            String desc = getItem(position).getDesc();
//            if (desc.length() > 50)
//                cm_desc.setText(getItem(position).getDesc().substring(0,50) + " ......");
//            else
//                cm_desc.setText(getItem(position).getDesc());
//
//
//            Picasso.get().load(getItem(position).getImageUrl()).resize(150, 150).into(cm_image);
//
//            return convertView;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("search for " + type + "s");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("xxx", newText);

                    if (lang==1){
                        searchListAr = responsee.parallelStream().filter(x -> x.getDescAr().contains(newText)).collect(Collectors.toList());

                        recyclerView.setLayoutManager(new LinearLayoutManager(typeactivity.this));
                        adapter = new recycleadapter( searchListAr);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }else {
                        searchList = responsee.parallelStream().filter(x -> x.getDesc().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList());
                        recyclerView.setLayoutManager(new LinearLayoutManager(typeactivity.this));
                        adapter = new recycleadapter(searchList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }


                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("xxx", query);


//                    searchList = responsee.stream().filter(x -> x.getDesc().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());



                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);

        }


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}