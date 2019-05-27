package MelsitovSemik.study.laboratory2.UI.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import MelsitovSemik.study.laboratory2.R;
import MelsitovSemik.study.laboratory2.UI.Fragments.AuthFragment;
import MelsitovSemik.study.laboratory2.Interfaces.ISign;

public class SignActivity extends SingleFragmentActivity implements ISign {

    private FirebaseAuth mAuth;

    @Override
    protected int getResID() {
        return R.layout.activity_main;
    }

    @Override
    protected Fragment getBasicFragment() {
        return AuthFragment.getInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user){
        if(user == null)
            return;
        startActivity(new Intent(SignActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void ISignIn(String email, String password) {
        if(!isNetworkAvailable()){
            Toast.makeText(this, R.string.internet_disconnect, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("==> ", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                }
                });
    }



    @Override
    public void ISignUp(String email, String password) {
        if(!isNetworkAvailable()){
            Toast.makeText(this, R.string.internet_disconnect, Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w("==> ", "signInWithEmail:failure", task.getException());

                            Toast.makeText(SignActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
}
