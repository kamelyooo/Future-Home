package com.example.futurehomeom;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.YoYo;
import com.github.chrisbanes.photoview.PhotoView;
import com.sanojpunchihewa.glowbutton.GlowButton;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

import static com.daimajia.androidanimations.library.Techniques.ZoomIn;

public class itemDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView titledetailitem,descdetailitem;
    ImageView imagedetailitem;
    EditText widthdetailitem,hieghtdetailitem,NotedetailItem;
    Showitem showitem;
    GlowButton addToCartButton;
    SharedPreferences preferencess;
    int lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        titledetailitem=findViewById(R.id.titledetailItem);
        descdetailitem=findViewById(R.id.descdetailItem);
        imagedetailitem=findViewById(R.id.imagedetailItem);
        hieghtdetailitem=findViewById(R.id.heightdetailItem);
        widthdetailitem=findViewById(R.id.widthdetailItem);
        NotedetailItem=findViewById(R.id.NotedetailItem);
addToCartButton=findViewById(R.id.addToCartButton);
getSupportActionBar().setTitle(getString(R.string.ItemDetails));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_im);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        preferencess = getSharedPreferences("lang", Context.MODE_PRIVATE);

        lang = preferencess.getInt("lang", 0);

        showitem = (Showitem) getIntent().getSerializableExtra("showitem");

        if (lang==1){
            titledetailitem.setText(showitem.getTitleAr());

            descdetailitem.setText(showitem.getDescAr());
        }else {
            titledetailitem.setText(showitem.getTitle());

            descdetailitem.setText(showitem.getDesc());
        }


        Picasso.get().load(showitem.getImageUrl()).into(imagedetailitem);

        imagedetailitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(itemDetailsActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                PhotoView photoView = mView.findViewById(R.id.photo_view);
                Picasso.get().load(showitem.getImageUrl()).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        addToCartButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        YoYo.with(ZoomIn)
                .duration(50)
                .playOn(findViewById(R.id.addToCartButton));
        if (widthdetailitem.getText().length()==0||hieghtdetailitem.getText().length()==0 ||
                Integer.parseInt(widthdetailitem.getText().toString())==0
                || Integer.parseInt(hieghtdetailitem.getText().toString())==0){
            Toasty.info(this, getString(R.string.pleaseEnterWidthandHeight), Toast.LENGTH_SHORT,true).show();
        }else {
            Order order = new Order();
            
            order.title = showitem.getTitle();
            order.code = showitem.getCode();
            order.height = Integer.parseInt(hieghtdetailitem.getText().toString());
            order.width = Integer.parseInt(widthdetailitem.getText().toString());
            order.url = showitem.getImageUrl();
            order.Note=NotedetailItem.getText().toString();
            order.titleAr=showitem.getTitleAr();


            long additem = marmoushdatabase.getInstance(this).orderDAo().additem(order);
            if (additem > 0) {
                Toasty.success(this, getString(R.string.addedtoCart), Toast.LENGTH_SHORT,true).show();
                finish();
            }
        }
    }
}