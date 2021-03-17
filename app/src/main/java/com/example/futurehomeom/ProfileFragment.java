package com.example.futurehomeom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.shreyaspatil.MaterialDialog.AbstractDialog;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import de.mateware.snacky.Snacky;
import es.dmoral.toasty.Toasty;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    String name;
    TextView currentUser_tv;
    Button history_btn, currentAdressbtn, currentConnectbtn, logOut_btn, loginbtn2,currnentlang;
    SharedPreferences preferences;
    SharedPreferences preferencess;
    ProfileFragment profileFragment;
    ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);
        preferencess = getActivity().getSharedPreferences("lang", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        currentUser_tv = view.findViewById(R.id.currentUser_tv);
        history_btn = view.findViewById(R.id.history_btn);
        currentAdressbtn = view.findViewById(R.id.currentAdressbtn);
        currentConnectbtn = view.findViewById(R.id.currentConnectbtn);
        logOut_btn = view.findViewById(R.id.logOut_btn);
        loginbtn2 = view.findViewById(R.id.login_btn2);
        currnentlang=view.findViewById(R.id.currentLang);
        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        CubeGrid doubleBounce = new CubeGrid();
        progressBar.setIndeterminateDrawable(doubleBounce);
        name = preferences.getString("name", "");
        currentUser_tv.setText(name);
        if (name.isEmpty()) {
            logOut_btn.setVisibility(View.INVISIBLE);
            loginbtn2.setVisibility(View.VISIBLE);
            history_btn.setEnabled(false);
            history_btn.setTextColor(Color.parseColor("#c7cfb7"));
        }
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), HistoryActivity.class);
                startActivity(in);
            }
        });

        currentAdressbtn.setOnClickListener(this);
        logOut_btn.setOnClickListener(this);
        currentConnectbtn.setOnClickListener(this);
        loginbtn2.setOnClickListener(this);
        currnentlang.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logOut_btn) {
            BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(getActivity())
                    .setTitle(getString(R.string.LogOut))
                    .setMessage(getString(R.string.Are_you_sure_want_to_Logout))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.LogOutt), R.drawable.ic_sad_24px, new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            progressBar.setVisibility(View.VISIBLE);
                            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            Backendless.UserService.logout(new AsyncCallback<Void>() {
                                @Override
                                public void handleResponse(Void response) {

                                    history_btn.setEnabled(false);
                                    history_btn.setTextColor(Color.parseColor("#c7cfb7"));
                                    logOut_btn.setVisibility(View.INVISIBLE);
                                    loginbtn2.setVisibility(View.VISIBLE);
                                    currentUser_tv.setText("");
                                    preferences.edit().clear().commit();

                                    progressBar.setVisibility(View.GONE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                                Toasty.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleFault(BackendlessFault fault) {
                                    progressBar.setVisibility(View.GONE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    Snacky.builder().setActivity(getActivity())
                                            .setDuration(Snacky.LENGTH_INDEFINITE)
                                            .setActionText(R.string.Retry).setActionClickListener(v1 -> {


                                        profileFragment = new ProfileFragment();
                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.countaner, profileFragment, "profiletag").commit();

                                    })
                                            .setText(R.string.pleaseCheckYourInternet)
                                            .info().show();
                                }
                            });
//                        Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.cancell), R.drawable.ic_close_24px, new BottomSheetMaterialDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                            dialogInterface.dismiss();
                        }
                    })
                    .build();

            // Show Dialog
            mBottomSheetDialog.show();
        } else if (v.getId() == R.id.login_btn2) {
            LoginFragment fragment = new LoginFragment();
            fragment.setCancelable(false);
            fragment.show(getActivity().getSupportFragmentManager(), "");


        } else if (v.getId() == R.id.currentConnectbtn) {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity())
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle(R.string.Connectus).setDialogBackgroundColor(Color.parseColor("#f8f1f1"))


                    .addButton(getString(R.string.OmarMarmoush), -1, Color.parseColor("#11698e"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + "01100551610"));
                        startActivity(intent);
                        dialog.dismiss();
                    }).addButton(getString(R.string.MomenElsaka), -1, Color.parseColor("#19456b"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + "01101096566"));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }).addButton(getString(R.string.MishoElzamly), -1, Color.parseColor("#16c79a"), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(android.content.DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + "01156987101"));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
// Show the alert
            builder.show();
        } else if (v.getId() == R.id.currentAdressbtn) {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity())
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle(R.string.Address).setDialogBackgroundColor(Color.parseColor("#f8f1f1"))

                    .addButton(getString(R.string.LocationOnMap), -1, Color.parseColor("#11698e"), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=30.1381587,31.3147231(label)"));
                        startActivity(intent);

                        dialog.dismiss();
                    }).setIcon(R.drawable.ic_location_on_24px).setMessage(R.string.AnsarAlSona);
            builder.show();
        }else if (v.getId()==R.id.currentLang){
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity());
            builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
            builder.setTitle(R.string.SelectLanguage);
            int lang = preferencess.getInt("lang", 0);
            builder.setSingleChoiceItems(new String[]{ "English", "العربيه"}, lang,new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {

                    SharedPreferences.Editor editor = preferencess.edit();
                    editor.putInt("lang",which);
                    editor.apply();

                    Intent intent = getActivity().getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().overridePendingTransition(0, 0);
                    getActivity().finish();

                    getActivity().overridePendingTransition(0, 0);
                    startActivity(intent);
                }
            });
            builder.show();
        }
    }
}