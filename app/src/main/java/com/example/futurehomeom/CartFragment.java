package com.example.futurehomeom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.daimajia.androidanimations.library.YoYo;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.sanojpunchihewa.glowbutton.GlowButton;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.mateware.snacky.Snacky;
import es.dmoral.toasty.Toasty;

import static com.daimajia.androidanimations.library.Techniques.ZoomIn;

public class CartFragment extends Fragment {

    TextView cm_myCartHeight, cm_myCartWidth, Cm_myCartTitle;
    ImageView cm_myCartiamge, plusHeight, minusHeight, plusWidth, minusWidth;
    ListView lv_myCart;
    List<Order> myorders;
    myyadapter adapter;
    Flow removeitem;
    int heighta;
    int widtha;
    GlowButton completeOrder_btn;
    TextView emptyTV;
    ImageView emptyCart;
    ProgressBar bar;
    CubeGrid doubleBounce;
    CartFragment cartFragmentc;
    SharedPreferences preferencess;
    int lang;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.i("life","oncreat");
        super.onCreate(savedInstanceState);

        preferencess = getActivity().getSharedPreferences("lang", Context.MODE_PRIVATE);
        lang = preferencess.getInt("lang", 0);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        completeOrder_btn = view.findViewById(R.id.completeOrder_btnn);
        lv_myCart = view.findViewById(R.id.lv_myCart);
        emptyCart = view.findViewById(R.id.emptyCart);
        emptyTV = view.findViewById(R.id.emptyTv);
        bar = (ProgressBar) view.findViewById(R.id.spin_kit);
        doubleBounce = new CubeGrid();
        bar.setIndeterminateDrawable(doubleBounce);
        //get order from room
        myorders = marmoushdatabase.getInstance(getActivity()).orderDAo().myorders();
        adapter = new myyadapter(getActivity(), myorders);
        lv_myCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (adapter.getCount() > 0) {
            emptyCart.setVisibility(View.GONE);
            emptyTV.setVisibility(View.GONE);
            lv_myCart.setVisibility(View.VISIBLE);
            completeOrder_btn.setVisibility(View.VISIBLE);
        }
        else if (adapter.getCount() == 0){
            emptyCart.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.VISIBLE);
            lv_myCart.setVisibility(View.GONE);
            completeOrder_btn.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();

        completeOrder_btn.setOnClickListener(v -> {
//            lv_myCart.setVisibility(View.GONE);
//            completeOrder_btn.setVisibility(View.GONE);
//            progress.setVisibility(View.VISIBLE);

            bar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            YoYo.with(ZoomIn)
                    .duration(50)
                    .playOn(view.findViewById(R.id.completeOrder_btnn));


            Backendless.UserService.isValidLogin(new AsyncCallback<Boolean>() {
                @Override
                public void handleResponse(Boolean response) {
                    if (response) {
//                        if (myorders.isEmpty())
//                            Toasty.error(getActivity(), "NO Order to Submit", Toast.LENGTH_SHORT, true).show();
//                        else {
//                            bar.setVisibility(View.VISIBLE);
//                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Backendless.Data.of(Order.class).create(myorders, new AsyncCallback<List<String>>() {
                                @Override
                                public void handleResponse(List<String> response) {
                                    Toasty.success(getActivity(), getString(R.string.OrderSubmited), Toast.LENGTH_SHORT, true).show();
                                    for (Order myorder : myorders) {
                                        MyOrder myOrderlist=new MyOrder();
                                        myOrderlist.objectId=myorder.getObjectId();
                                        myOrderlist.state="Processing";
                                        myOrderlist.title=myorder.title;
                                        marmoushdatabase.getInstance(getActivity()).myorderDAo().addMyOrderItem(myOrderlist);

                                    }

//                                    for (Order myorder : myorders) {
                                        marmoushdatabase.getInstance(getActivity()).orderDAo().deleteAll();
//       nk                             }
                                    myorders.clear();
                                    adapter.notifyDataSetChanged();
                                    bar.setVisibility(View.GONE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    completeOrder_btn.setVisibility(View.GONE);
                                    emptyCart.setVisibility(View.VISIBLE);
                                    emptyTV.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    bar.setVisibility(View.GONE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    lv_myCart.setVisibility(View.VISIBLE);
                                    completeOrder_btn.setVisibility(View.VISIBLE);
                                    Log.i("wwww", fault.getCode());
                                    Snacky.builder().setActivity(getActivity())
                                            .setDuration(Snacky.LENGTH_INDEFINITE)
                                            .setActionText(R.string.Retry).setActionClickListener(v1 -> {


                                        cartFragmentc = new CartFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.countaner, cartFragmentc,"carttag").commit();

                                    })
                                            .setText(R.string.pleaseCheckYourInternet)
                                            .info().show();
                                }
                            });
//                        }
                    } else {
                        LoginFragment fragment=new LoginFragment();
                        fragment.setCancelable(false);
                        fragment.show(getActivity().getSupportFragmentManager(),"");

                    }
                }
                @Override
                public void handleFault(BackendlessFault fault) {

                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_INDEFINITE)
                            .setActionText(R.string.Retry).setActionClickListener(v1 -> {

                      cartFragmentc = new CartFragment();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.countaner, cartFragmentc,"carttag").commit();

                    })
                            .setText(R.string.pleaseCheckYourInternet)
                            .info().show();

                    Log.i("life","oncreat");
                }
            });
            adapter.notifyDataSetChanged();
        });


        return view;
    }




    class myyadapter extends ArrayAdapter<Order> {
        public myyadapter(@NonNull Context context, List<Order> myorders) {
            super(context, 0, myorders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.cm_cartactivity, parent, false);

            Cm_myCartTitle = convertView.findViewById(R.id.cm_TitleMycart);
            cm_myCartHeight = convertView.findViewById(R.id.cm_HeightMycart);
            cm_myCartWidth = convertView.findViewById(R.id.cm_WidthMycart);
            cm_myCartiamge = convertView.findViewById(R.id.cm_imageMycart);
            plusHeight = convertView.findViewById(R.id.PlusHeight);
            minusHeight = convertView.findViewById(R.id.minusHeight);
            plusWidth = convertView.findViewById(R.id.plusWidth);
            minusWidth = convertView.findViewById(R.id.minusWidth);
            removeitem = convertView.findViewById(R.id.removeItem);

            plusHeight.setOnClickListener(v -> {
                heighta = getItem(position).height;
                heighta++;
                getItem(position).setHeight(heighta);//habdet amanyyyyyy
                marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                adapter.notifyDataSetChanged();
            });
            plusWidth.setOnClickListener(v -> {
                widtha = getItem(position).width;
                widtha++;
                getItem(position).setWidth(widtha);
                marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                adapter.notifyDataSetChanged();
            });

            minusHeight.setOnClickListener(v -> {
                heighta = getItem(position).height;
                if (heighta<=1){
                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_SHORT)
                                .setText(R.string.TheHeightCant0)
                            .info().show();
                }else{
                    heighta--;
                    getItem(position).setHeight(heighta);
                    marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                    adapter.notifyDataSetChanged();}
            });
            minusWidth.setOnClickListener(v -> {
                widtha = getItem(position).width;
                if (widtha<=1){
                    Snacky.builder().setActivity(getActivity())
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setText(R.string.TheWidthCant0)
                            .info().show();
                }else{
                    widtha--;
                    getItem(position).setWidth(widtha);
                    marmoushdatabase.getInstance(getActivity()).orderDAo().update(getItem(position));
                    adapter.notifyDataSetChanged();}
            });
            removeitem.setOnClickListener(v -> {
                BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(getActivity())
                        .setTitle(getString(R.string.Delete))
                        .setMessage(getString(R.string.AreyousurewanttoDelete))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.Deletee), R.drawable.ic_trashcan, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
//
                                int deletorders = marmoushdatabase.getInstance(getActivity()).orderDAo().deletorders(getItem(position));
                                if (deletorders>0)
                                    Toasty.success(getActivity(), getString(R.string.orderdeleted), Toast.LENGTH_SHORT,true).show();
                                myorders.remove(getItem(position));
                                adapter.notifyDataSetChanged();


                                if (adapter.getCount() > 0) {
                                    emptyCart.setVisibility(View.GONE);
                                    emptyTV.setVisibility(View.GONE);
                                    lv_myCart.setVisibility(View.VISIBLE);
                                    completeOrder_btn.setVisibility(View.VISIBLE);
                                }
                                else if (adapter.getCount() == 0){
                                    emptyCart.setVisibility(View.VISIBLE);
                                    emptyTV.setVisibility(View.VISIBLE);
                                    lv_myCart.setVisibility(View.GONE);
                                    completeOrder_btn.setVisibility(View.GONE);
                                }
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton(getString(R.string.Cancell), R.drawable.ic_close_24px, new BottomSheetMaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        })
                        .build();

                // Show Dialog
                mBottomSheetDialog.show();
            });

            adapter.notifyDataSetChanged();



            if (lang==1)Cm_myCartTitle.setText(getItem(position).titleAr);
            else Cm_myCartTitle.setText(getItem(position).title);

            cm_myCartHeight.setText(getItem(position).height + "");
            cm_myCartWidth.setText(getItem(position).width + "");
            Picasso.get().load(getItem(position).url).resize(150,150).into(cm_myCartiamge);
            cm_myCartiamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
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