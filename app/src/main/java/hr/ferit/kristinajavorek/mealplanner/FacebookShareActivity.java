package hr.ferit.kristinajavorek.mealplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Arrays;
import java.util.List;

public class FacebookShareActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private CallbackManager callbackManager;
    Bitmap imageBitmap = null;
    private EditText etImageDescription;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_share);
        etImageDescription = (EditText) findViewById(R.id.etImageDescription);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                takePictureIntent();
                if(imageBitmap!=null) sharePhotoToFacebook();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        }

    private void sharePhotoToFacebook(){
        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.breakfast);
        Bitmap image = imageBitmap;
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Meal")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            if (FacebookSdk.isFacebookRequestCode(requestCode)) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
