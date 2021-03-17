package com.example.futurehomeom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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


public class RegesterFragment extends DialogFragment {
    EditText regname, regmail, regpass, regphone, regadress;
    Button regstration;
   Fragment currentFragment;
    ProgressBar progressBar;
TextView regTextLogin,regTextCancel;
    AlertDialog dialog;
    public RegesterFragment() {
        // Required empty public constructor
    }


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.cm_regestration, null);

        regname = view.findViewById(R.id.regName_ET);
        regmail = view.findViewById(R.id.regMail_ET);
        regpass = view.findViewById(R.id.regpassword_ET);
        regphone = view.findViewById(R.id.regphone_ET);
        regadress = view.findViewById(R.id.regAdrss_ET);
        regstration = view.findViewById(R.id.registrationButton);
        regTextLogin=view.findViewById(R.id.regtextLogin);
        regTextCancel=view.findViewById(R.id.regTextCancel);


        progressBar = (ProgressBar) view.findViewById(R.id.spin_kit);
        CubeGrid doubleBounce = new CubeGrid();
        progressBar.setIndeterminateDrawable(doubleBounce);


        regTextCancel.setOnClickListener(new View.OnClickListener() {
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
        regTextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment fragment = new LoginFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), "");
                dialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        regstration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YoYo.with(ZoomIn)
                        .duration(100)
                        .playOn(view.findViewById(R.id.registrationButton));
                closeKeyBoard();
                String nameText = regname.getText().toString().trim();
                String emailText = regmail.getText().toString().trim();
                String passwordText = regpass.getText().toString().trim();
                String Phone = regphone.getText().toString().trim();
                String Address = regadress.getText().toString().trim();

                if (nameText.isEmpty()) {
                    regname.setError(getString(R.string.NamecantbeEmpty));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.regName_ET));

                } else if (emailText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    regmail.setError(getString(R.string.pleaseentervalidEmail));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.regMail_ET));

                } else if (passwordText.isEmpty()) {
                    regpass.setError(getString(R.string.passwordmustWritepassword));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.regpassword_ET));
                } else if (Phone.isEmpty()) {
                    regphone.setError(getString(R.string.phone_cant_be_Empty));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.regphone_ET));
                } else if (Address.isEmpty()) {
                    regadress.setError(getString(R.string.AdresscantbeEmpty));
                    YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(view.findViewById(R.id.regAdrss_ET));
                } else {
                    regTextCancel.setEnabled(false);
                    regTextLogin.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    BackendlessUser user = new BackendlessUser();
                    if (emailText != null) user.setEmail(emailText);
                    if (passwordText != null) user.setPassword(passwordText);
                    if (nameText != null) user.setProperty("name", nameText);
                    user.setProperty("phone", Phone);
                    user.setProperty("Adress", Address);

                    Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            regTextCancel.setEnabled(true);
                            regTextLogin.setEnabled(true);
                            Toasty.success(getActivity(),getString(R.string.Saved) , Toast.LENGTH_SHORT,true).show();
                            LoginFragment fragment = new LoginFragment();
                            fragment.setCancelable(false);
                            fragment.show(getFragmentManager(), "");
                            progressBar.setVisibility(View.GONE);
                            if (getActivity() != null)
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            dismiss();
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            regTextCancel.setEnabled(true);
                            regTextLogin.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            if (getActivity() != null)
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            if (getActivity() != null)
                                Toasty.error(getActivity(),getString(R.string.MailExist) , Toast.LENGTH_SHORT,true).show();

                        }
                    });
                }
            }
        });

        dialog = builder.create();

        return dialog;
    }

    private void closeKeyBoard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}