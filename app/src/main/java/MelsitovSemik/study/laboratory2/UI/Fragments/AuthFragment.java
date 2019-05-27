package MelsitovSemik.study.laboratory2.UI.Fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import MelsitovSemik.study.laboratory2.Interfaces.ISign;
import MelsitovSemik.study.laboratory2.R;

public class AuthFragment extends Fragment {

    private ISign mISign;
    private Unbinder mUnbinder;
    @BindView(R.id.auth_fragment_login)
    EditText mLoginEditText;
    @BindView(R.id.auth_fragment_password)
    EditText mPasswordEditText;

    public static AuthFragment getInstance(){
        return new AuthFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mISign = (ISign) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(255), color.nextInt(255), color.nextInt(255)));

        return view;
    }

    @OnClick({R.id.auth_fragment_auth_btn, R.id.auth_fragment_sign})
    void click(View view){

        Random color = new Random();
        view.setBackgroundColor(Color.argb(255, color.nextInt(240), color.nextInt(255), color.nextInt(255)));

        String email = mLoginEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if(!checkPassword(password) || !checkEmail(email))
            return;


        switch (view.getId()){
            case R.id.auth_fragment_auth_btn:
                mISign.ISignIn(email, password);
                break;
            case R.id.auth_fragment_sign:
                mISign.ISignUp(email, password);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private boolean checkEmail(String email){
        if(TextUtils.isEmpty(email)){
            mPasswordEditText.setError(getString(R.string.error_email_empty));
            return false;
        }



        return true;
    }

    private boolean checkPassword(String password){
        if(TextUtils.isEmpty(password)){
            mPasswordEditText.setError(getString(R.string.error_password_empty));
            return false;
        }
        if(password.length() < 6){
            mPasswordEditText.setError(getString(R.string.error_password_length));
            return false;
        }
        return true;
    }


}
