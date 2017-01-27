package in.voiceme.app.voiceme.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import timber.log.Timber;

import static com.google.gdata.util.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

  /**
   * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
   * performed by the {@code fragmentManager}.
   */

  public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                           @NonNull Fragment fragment, int frameId, String tag, int enterAnimation, int exitAnimation,
                                           int popEnter, int popExit) {

    checkNotNull(fragmentManager);
    checkNotNull(fragment);

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(enterAnimation, exitAnimation, popEnter, popExit);
    fragmentTransaction.replace(frameId, fragment, tag);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commitAllowingStateLoss();
  }

  public static void addFragmentToActivityWithSharedElement(
          @NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, int frameId, String tag,
          int enterAnimation, int exitAnimation, int popEnter, int popExit, View sharedElement,
          String transitionName) {

    checkNotNull(fragmentManager);
    checkNotNull(fragment);

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(enterAnimation, exitAnimation, popEnter, popExit);
    fragmentTransaction.addSharedElement(sharedElement, transitionName);
    fragmentTransaction.replace(frameId, fragment, tag);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commitAllowingStateLoss();
  }

  public static void addStickyFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId, String tag, int enterAnimation, int exitAnimation,
                                                 int popEnter, int popExit) {

    checkNotNull(fragmentManager);
    checkNotNull(fragment);

    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.setCustomAnimations(enterAnimation, exitAnimation, popEnter, popExit);
    fragmentTransaction.replace(frameId, fragment, tag);
    fragmentTransaction.commitAllowingStateLoss();
  }

  public static void showSnackBar(@NonNull View view, @NonNull String message) {

    checkNotNull(view);
    checkNotNull(message);
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
  }

  public static void dismissKeyboard(@NonNull View view) {
    InputMethodManager imm = (InputMethodManager) VoicemeApplication.getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void showKeyboard(@NonNull View view) {

    InputMethodManager imm = (InputMethodManager) VoicemeApplication.getContext()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    view.requestFocus();
  }

  public static Uri openCamera(Activity activity) {

    if (isStoragePermissionGranted(activity,
        activity.getResources().getInteger(R.integer.camera_request))) {

      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      Uri photoURI;

      ContentValues values = new ContentValues(1);
      values.put(MediaStore.Images.Media.MIME_TYPE, "profile_image/jpg");
      photoURI = activity.getContentResolver()
          .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
      intent.addFlags(
          Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
      if (photoURI != null) {
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
      }

      activity.startActivityForResult(intent,
          activity.getResources().getInteger(R.integer.camera_request));

      return photoURI;
    }
    return null;
  }

  public static void openGallery(Activity activity) {

    if (isStoragePermissionGranted(activity,
        activity.getResources().getInteger(R.integer.storage_request))) {

      Intent intent = new Intent();
      intent.setType("profile_image/jpeg");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      activity.startActivityForResult(Intent.createChooser(intent, "Select picture"),
          activity.getResources().getInteger(R.integer.pick_image_request));
    }
  }

  public static boolean isContactsPermission(Activity activity) {

    if (isContactsPermissionGranted(activity)) {

      return true;
    } else {
      ActivityCompat.requestPermissions(activity,
          new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
          activity.getResources().getInteger(R.integer.contacts_request));
    }

    return false;
  }

  public static boolean isAudioRecordingPermission(Activity activity) {

    if (isAudioPermissionGranted(activity)) {

      return true;
    } else {
      ActivityCompat.requestPermissions(activity,
          new String[] { Manifest.permission.RECORD_AUDIO },
          activity.getResources().getInteger(R.integer.recorder_request));
    }

    return false;
  }

  private static boolean isAudioPermissionGranted(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO)
              == PackageManager.PERMISSION_GRANTED) {
        Timber.d("Permission is granted");
        return true;
      } else {

        Timber.d("Permission is revoked");
        ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.RECORD_AUDIO },
                activity.getResources().getInteger(R.integer.recorder_request));
        return false;
      }
    } else { //permission is automatically granted on sdk<23 upon installation
      Timber.d("Permission is granted");
      return true;
    }
  }


  private static boolean isContactsPermissionGranted(Activity activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
          == PackageManager.PERMISSION_GRANTED) {
        Timber.d("Permission is granted");
        return true;
      } else {

        Timber.d("Permission is revoked");
        ActivityCompat.requestPermissions(activity,
            new String[] { Manifest.permission.READ_CONTACTS },
            activity.getResources().getInteger(R.integer.contacts_request));
        return false;
      }
    } else { //permission is automatically granted on sdk<23 upon installation
      Timber.d("Permission is granted");
      return true;
    }
  }

  public static boolean isStoragePermissionGranted(Activity activity, int storageRequestId) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          == PackageManager.PERMISSION_GRANTED) {
        Timber.d("Permission is granted");
        return true;
      } else {

        Timber.d("Permission is revoked");
        ActivityCompat.requestPermissions(activity,
            new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, storageRequestId);
        return false;
      }
    } else { //permission is automatically granted on sdk<23 upon installation
      Timber.d("Permission is granted");
      return true;
    }
  }

  public static Bitmap screenShot(View view) {
    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    view.draw(canvas);
    return bitmap;
  }
}

