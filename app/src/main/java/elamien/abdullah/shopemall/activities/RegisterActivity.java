package elamien.abdullah.shopemall.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import elamien.abdullah.shopemall.GlideApp;
import elamien.abdullah.shopemall.R;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.loginMemberLabelTextView)
    TextView existedMembershipLabel;
    @BindView(R.id.registerLayoutParent)
    ConstraintLayout registerLayoutParent;
    @BindView(R.id.registerImage)
    KenBurnsView registerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenWindow();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        loadRegisterImage();
        setupExistedMembershipLabel();
    }

    private void setFullScreenWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void loadRegisterImage() {
        GlideApp.with(this)
                .load(getString(R.string.register_activity_image))
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.error_holder)
                .into(registerImage);
    }

    private void setupExistedMembershipLabel() {
        SpannableString textLink = new SpannableString(getString(R.string.membership_register_label));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        textLink.setSpan(clickableSpan, 17, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        existedMembershipLabel.setText(textLink);
        existedMembershipLabel.setMovementMethod(LinkMovementMethod.getInstance());
        existedMembershipLabel.setHighlightColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.signinWithGoogleImageView)
    public void onSigninGoogleClick() {
    }

    @OnClick(R.id.signupButton)
    public void onSignupButtonClick() {
    }
}
