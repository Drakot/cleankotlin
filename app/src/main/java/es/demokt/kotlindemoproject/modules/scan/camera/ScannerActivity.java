package es.demokt.kotlindemoproject.modules.scan.camera;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import es.demokt.kotlindemoproject.R;
import es.demokt.kotlindemoproject.modules.base.BaseFragment;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by aluengo on 23/4/18.
 */

public abstract class ScannerActivity extends BaseFragment {
  private static final int RC_HANDLE_GMS = 9001;
  protected CameraSource cameraSource;
  protected boolean detect;
  CameraSourcePreview2 mPreview;

  @Override public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mPreview = view.findViewById(R.id.camera_view);
  }

  /**
   * Restarts the camera.
   */
  @Override public void onResume() {
    super.onResume();
    startCameraSource();
  }

  /**
   * Stops the camera.
   */
  @Override public void onPause() {
    super.onPause();
    stopCamera();
  }

  public void stopCamera() {
    if (mPreview != null) {
      mPreview.stop();
    }
  }

  /**
   * Releases the resources associated with the camera source, the associated detectors, and the
   * rest of the processing pipeline.
   */
  @Override public void onDestroy() {
    super.onDestroy();
    if (mPreview != null) {
      mPreview.release();
    }
  }

  /**
   * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  protected void startCameraSource() throws SecurityException {
    // check that the device has play services available.
    int code =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
    if (code != ConnectionResult.SUCCESS) {
      Dialog dlg = GoogleApiAvailability.getInstance()
          .getErrorDialog(this.getActivity(), code, RC_HANDLE_GMS);
      dlg.show();
    }

    if (cameraSource != null) {
      try {
        detect = false;
        if (ActivityCompat.checkSelfPermission(this.getBaseActivity(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
          getBaseActivity().checkPermissionCamera();
        } else {
          mPreview.start(cameraSource);
        }
      } catch (IOException e) {
        cameraSource.release();
        cameraSource = null;
      }
    }
  }
}
