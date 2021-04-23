package com.filter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.filter.utility.Helper;
import com.filter.utility.TransformImage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ControlActivity extends AppCompatActivity {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    Toolbar mControlToolbar;

    ImageView mCenterImageView;

    TransformImage mTransformImage;

    Button mSave;

    int mCurrentFilter;

    Uri mSelectedImageUri;

    SeekBar mSeekBar;
    ImageView mTickImageView;
    ImageView cancelImageView;

    int mScreenWidth;
    int mScreenHeight;

    Target mApplySingleFilter = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int currentFilterValue = mSeekBar.getProgress();

            if(mCurrentFilter == TransformImage.FILTER_BRIGHTNESS) {
                mTransformImage.applyBrightnessSubFilter(currentFilterValue);

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBitmap(TransformImage.FILTER_BRIGHTNESS));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS)));
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if(mCurrentFilter == TransformImage.FILTER_CONTRAST) {
                mTransformImage.applyContrastSubFilter(currentFilterValue);

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST),mTransformImage.getBitmap(TransformImage.FILTER_CONTRAST));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST)));
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if (mCurrentFilter == TransformImage.FILTER_VIGNETTE) {
                mTransformImage.applyVignetteSubFilter(currentFilterValue);

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE),mTransformImage.getBitmap(TransformImage.FILTER_VIGNETTE));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE)));
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight/2).into(mCenterImageView);
            } else if (mCurrentFilter == TransformImage.FILTER_SATURATION) {
                mTransformImage.applySaturationSubFilter(currentFilterValue);

                Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION),mTransformImage.getBitmap(TransformImage.FILTER_SATURATION));

                Picasso.with(ControlActivity.this).invalidate(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION)));
                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }


        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    };



    Target mSmallTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            mTransformImage = new TransformImage(ControlActivity.this,bitmap);
            mTransformImage.applyBrightnessSubFilter(TransformImage.DEFAULT_BRIGHTNESS);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS),mTransformImage.getBitmap(TransformImage.FILTER_BRIGHTNESS));
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).fit().centerInside().into(mFirstFilterPreviewImageView);
            //

            mTransformImage.applySaturationSubFilter(TransformImage.DEFAULT_SATURATION);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION),mTransformImage.getBitmap(TransformImage.FILTER_SATURATION));
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).fit().centerInside().into(mSecondFilterPreviewImageView);

            //

            mTransformImage.applyVignetteSubFilter(TransformImage.DEFAULT_VIGNETTE);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE),mTransformImage.getBitmap(TransformImage.FILTER_VIGNETTE));
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).fit().centerInside().into(mThirdFilterPreviewImageView);

            //

            mTransformImage.applyContrastSubFilter(TransformImage.DEFAULT_CONTRAST);

            Helper.writeDataIntoExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST),mTransformImage.getBitmap(TransformImage.FILTER_CONTRAST));
            Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).fit().centerInside().into(mFourthFilterPreviewImageView);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
   // set values for image picking
    final static int PICK_IMAGE = 2;
    final static  int MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION = 3;

    ImageView mFirstFilterPreviewImageView;
    ImageView mSecondFilterPreviewImageView;
    ImageView mThirdFilterPreviewImageView;
    ImageView mFourthFilterPreviewImageView;

    private static final String TAG = ControlActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        mControlToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCenterImageView = (ImageView) findViewById(R.id.centerimageView);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);

        mSave=findViewById(R.id.save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) mCenterImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();
                try {
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshGallery(outFile);

            }

            private void refreshGallery(File outFile) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(Uri.fromFile(outFile));
                    getApplicationContext().sendBroadcast(mediaScanIntent);
                } else {
                    getApplicationContext().sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
            }
        });

        mControlToolbar.setTitle(getString(R.string.app_name));
        mControlToolbar.setNavigationIcon(R.drawable.icon);
        mControlToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        mFirstFilterPreviewImageView = (ImageView) findViewById(R.id.imageView5);
        mSecondFilterPreviewImageView = (ImageView) findViewById(R.id.imageView4);
        mThirdFilterPreviewImageView = (ImageView) findViewById(R.id.imageView7);
        mFourthFilterPreviewImageView = (ImageView) findViewById(R.id.imageView6);

        mTickImageView = (ImageView) findViewById(R.id.imageView3);
        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ControlActivity.this,ImagePreviewActivity.class);
                startActivity(intent);
            }
        });

        // load image
        mCenterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermissions();

                if(ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        mFirstFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setMax(TransformImage.MAX_BRIGHTNESS);
                mSeekBar.setProgress(TransformImage.DEFAULT_BRIGHTNESS);

                mCurrentFilter = TransformImage.FILTER_BRIGHTNESS;

                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_BRIGHTNESS))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        mSecondFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setMax(TransformImage.MAX_SATURATION);
                mSeekBar.setProgress(TransformImage.DEFAULT_SATURATION);

                mCurrentFilter = TransformImage.FILTER_SATURATION;

                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_SATURATION))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        mThirdFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setMax(TransformImage.MAX_VIGNETTE);
                mSeekBar.setProgress(TransformImage.DEFAULT_VIGNETTE);

                mCurrentFilter = TransformImage.FILTER_VIGNETTE;

                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_VIGNETTE))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        mFourthFilterPreviewImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setMax(TransformImage.MAX_CONTRAST);
                mSeekBar.setProgress(TransformImage.DEFAULT_CONTRAST);

                mCurrentFilter = TransformImage.FILTER_CONTRAST;

                Picasso.with(ControlActivity.this).load(Helper.getFileFromExternalStorage(ControlActivity.this,mTransformImage.getFilename(TransformImage.FILTER_CONTRAST))).resize(0,mScreenHeight/2).into(mCenterImageView);
            }
        });

        mTickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(mApplySingleFilter);
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mScreenHeight = displayMetrics.heightPixels;
        mScreenWidth = displayMetrics.widthPixels;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new MaterialDialog.Builder(ControlActivity.this).title("Permission Granted")
                            .content("Thank you for providing storage permission")
                            .positiveText("Ok").canceledOnTouchOutside(true).show();
                } else {
                    Log.d(TAG,"Permission denied!");
                }
        }
    }

    // load image from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            mSelectedImageUri = data.getData();

            Picasso.with(ControlActivity.this).load(mSelectedImageUri).fit().centerInside().into(mCenterImageView);

            Picasso.with(ControlActivity.this).load(mSelectedImageUri).into(mSmallTarget);
        }
    }

    public void requestStoragePermissions() {
        if(ContextCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(ControlActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new MaterialDialog.Builder(ControlActivity.this).title(R.string.permission_title)
                        .content(R.string.permission_content)
                        .negativeText(R.string.permission_cancel)
                        .positiveText(R.string.permission_agree_settings)
                        .canceledOnTouchOutside(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(ControlActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_STORAGE_PERMISSION);
            }
            return;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
