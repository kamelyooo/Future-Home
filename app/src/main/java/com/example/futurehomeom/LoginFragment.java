
package com.example.futurehomeom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.style.CubeGrid;

import es.dmoral.toasty.Toasty;

import static com.daimajia.androidanimations.library.Techniques.ZoomIn;


public class LoginFragment extends DialogFragment {
    CubeGrid doubleBounce;
    EditText NameUser, Pass;
Button loginButton;
ProgressBar progressBar;
    SharedPreferences preferences;
    Fragment currentFragment;
    TextView loginTextCancel,loginRegText;
    AlertDialog dialog;
    public LoginFragment() {


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("info", Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.cm_login, null);

        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);

        doubleBounce = new CubeGrid();
        progressBar.setIndeterminateDrawable(doubleBounce);
        NameUser = view.findViewById(R.id.userName_ET);
        Pass = view.findViewById(R.id.logpassword_ET);
        loginTextCancel=view.findViewById(R.id.loginTextCancel);
        loginButton=view.findViewById(R.id.loginButton);
        loginRegText=view.findViewById(R.id.loginRegText);
        loginRegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegesterFragment fragment=new RegesterFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(),"");
                progressBar.setVisibility(View.GONE);
                if (getActivity() != null)
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                dialog.dismiss();
            }
        });
        loginTextCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.GONE);
                                        if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        currentFragment = getFragmentManager().findFragmentByTag("carttag");
                                        if (currentFragment != null){
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.detach(currentFragment);
                                            fragmentTransaction.attach(currentFragment);
                                            fragmentTransaction.commit();

                                        }
                dialog.dismiss();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NameUser.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(NameUser.getText().toString()).matches()) {
                    NameUser.setError(getString(R.string.pleaseentervalidEmail));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.userName_ET));

                } else if (Pass.getText().toString().isEmpty()) {
                    Pass.setError(getString(R.string.passwordmustWritepassword));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.logpassword_ET));
                } else{
                    loginRegText.setEnabled(false);
                loginTextCancel.setEnabled(false);
                YoYo.with(ZoomIn)
                        .duration(100)
                        .playOn(view.findViewById(R.id.loginButton));
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Backendless.UserService.login(NameUser.getText().toString(), Pass.getText().toString(), new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser response) {
                        String name = response.getProperty("name").toString();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("name", name);
                        editor.apply();
                        loginTextCancel.setEnabled(true);
                        loginRegText.setEnabled(true);
                        Toasty.success(getActivity(), getString(R.string.loggedin), Toast.LENGTH_SHORT, true).show();

                        currentFragment = getFragmentManager().findFragmentByTag("profiletag");
                        if (currentFragment != null) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();

                        }
                        currentFragment = getFragmentManager().findFragmentByTag("carttag");
                        if (currentFragment != null) {
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.detach(currentFragment);
                            fragmentTransaction.attach(currentFragment);
                            fragmentTransaction.commit();

                        }
                        progressBar.setVisibility(View.GONE);
                        if (getActivity() != null)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        dismiss();
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        loginTextCancel.setEnabled(true);
                        loginRegText.setEnabled(true);
                        Log.i("falt", fault.getCode());
                        progressBar.setVisibility(View.GONE);
                        if (getActivity() != null)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (fault.getCode().equals("3003")) {
                            if (getActivity() != null)
                                Toasty.error(getActivity(), getString(R.string.error_in_password_or_name), Toast.LENGTH_SHORT, true).show();
                        } else if (fault.getCode().equals("3087")) {
                            if (getActivity() != null)
                                Toasty.error(getActivity(), getString(R.string.please_confirm_the_email), Toast.LENGTH_SHORT, true).show();
                        } else if (fault.getCode().equals("3002")) {
                            if (getActivity() != null)
                                Toasty.error(getActivity(), getString(R.string.logged_for_many_times), Toast.LENGTH_SHORT, true).show();
                        } else if (fault.getCode().equals("3044")) {
                            if (getActivity() != null)
                                Toasty.error(getActivity(), getString(R.string.multi_logged_for_many_Devices), Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(getActivity(), getString(R.string.pleaseCheckYourInternet), Toast.LENGTH_SHORT, true).show();

                        }
                    }
                }, true);
            }
            }
        });

        //        progressBar.setVisibility(View.GONE);
        //                        if (getActivity() != null)
        //                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //
        //                        currentFragment = getFragmentManager().findFragmentByTag("carttag");
        //                        if (currentFragment != null){
        //                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        //                            fragmentTransaction.detach(currentFragment);
        //                            fragmentTransaction.attach(currentFragment);
        //                            fragmentTransaction.commit();
        //
        //                        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);




        dialog = builder.create();


        return dialog;
    }

}