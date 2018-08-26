package elamien.abdullah.shopemall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // TODO: 8/26/2018 replace it with your project key.
    private static final String WEB_API_KEY = "replace-it-with-your-web-api-key";
    private static final int RC_GOOGLE_SIGNIN = 9;

    @BindView(R.id.loginImage)
    KenBurnsView loginImage;
    @BindView(R.id.loginEmailEditText)
    EditText loginEmailEditText;
    @BindView(R.id.loginPasswordEditText)
    EditText loginPasswordEditText;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        loadImage();
        setupGoogleClient();
    }

    private void setupGoogleClient() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_API_KEY)
                .requestEmail()
                .build();
        mGoogleClient = GoogleSignIn.getClient(this, signInOptions);
    }

    private void loadImage() {
        GlideApp.with(this)
                .load("https://goo.gl/kXrupd")
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(loginImage);
    }

    @OnClick(R.id.loginGoogleAuthImageButton)
    public void onGoogleButtonClick() {
        Intent intent = mGoogleClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GOOGLE_SIGNIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signinGoogleUser(account);
            } catch (ApiException e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void signinGoogleUser(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    @OnClick(R.id.loginButton)
    public void onLoginButtonClick(View view) {
        if (!isEmail(loginEmailEditText)) {
            loginEmailEditText.setError(getString(R.string.register_email_error_msg));
        } else if (isTextEmpty(loginPasswordEditText)) {
            loginPasswordEditText.setError(getString(R.string.register_password_error_msg));
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }
                });
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    private boolean isEmail(EditText emailET) {
        String email = emailET.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
