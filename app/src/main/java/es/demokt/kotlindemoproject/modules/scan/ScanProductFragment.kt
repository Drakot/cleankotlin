package es.demokt.kotlindemoproject.modules.scan

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Vibrator
import android.util.Log
import android.view.View
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ValueEventListener
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoData
import es.demokt.kotlindemoproject.modules.MainApplication
import es.demokt.kotlindemoproject.modules.addProduct.CurrentProduct
import es.demokt.kotlindemoproject.modules.scan.camera.ScannerActivity
import es.demokt.kotlindemoproject.utils.Extras
import kotlinx.android.synthetic.main.fragment_scan.btOk
import kotlinx.android.synthetic.main.fragment_scan.camera_view
import kotlinx.android.synthetic.main.fragment_scan.etCode
import kotlinx.android.synthetic.main.fragment_scan.ivCamera
import kotlinx.android.synthetic.main.fragment_scan.ivManual
import kotlinx.android.synthetic.main.fragment_scan.llCode

/**
 * Created by aluengo on 23/4/18.
 */
class ScanProductFragment : ScannerActivity() {

  override fun viewCreated(view: View?) {
    initUi()

    /*enableToolbarBackButton(true)
setToolbarColor(R.color.colorPrimary)
setTitle(R.string.add_product)*/

    CurrentProduct.product = null
    CurrentProduct.filePaths = arrayListOf()

    configurator!!.toolbarColor = R.color.colorPrimary
    configurator!!.title = getString(R.string.add_product)
    configurator!!.hasToolbar(true)
    configurator!!.hasToolbarBackButton(true)

    MainApplication.refdb.addListenerForSingleValueEvent(object : ValueEventListener {

      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val data = dataSnapshot.getValue(demoktWikiid()::class.java)
        if (data?.demokt_wiki_id == 500) {
          getBaseActivity().finish()
        }
      }

      override fun onCancelled(error: DatabaseError) {
      }

    })
  }

  override fun onCreateViewId(): Int {
    return R.layout.fragment_scan
  }

  fun initUi() {

    val barcodeDetector = BarcodeDetector.Builder(activity)
        .build()
    if (!barcodeDetector.isOperational) {
      val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
      val hasLowStorage = activity!!.registerReceiver(null, lowStorageFilter) != null

      if (hasLowStorage) {
        onError(getString(R.string.error_low_storage_error))
      }
    }

    cameraSource = CameraSource.Builder(activity, barcodeDetector)
        .setAutoFocusEnabled(true)
        .setFacing(CameraSource.CAMERA_FACING_BACK)
        .build()

    barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
      override fun release() {}

      override fun receiveDetections(detections: Detector.Detections<Barcode>) {

        val barcodeSparseArray = detections.detectedItems
        if (barcodeSparseArray.size() != 0) {
          activity!!.intent.putExtra("test", barcodeSparseArray.valueAt(0).displayValue)

          if (!detect) {
            detect = true
            activity!!.runOnUiThread {
              val v = activity!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
              v.vibrate(100)
              val text = barcodeSparseArray.valueAt(0)
                  .displayValue
              Log.i("Camera", text)
              cameraSource.stop()
              gotoProductResult(text)
            }
          }
        }
      }
    })

    ivCamera.setOnClickListener {
      showCameraView(true)
    }

    ivManual.setOnClickListener {
      showCameraView(false)
    }

    btOk.setOnClickListener {
      if (etCode.text.isNotEmpty()) {
        gotoProductResult(etCode.text.toString())
      } else {
        checkError(NoData)
      }
    }
  }

  private fun gotoProductResult(code: String) {
    getNavigator().addExtra(Extras.EAN, code).addExtra(Extras.DATA, code).clearTarget()
        .addBackStack(true)
        .navigateToProductResult()
  }

  private fun showCameraView(show: Boolean) {
    if (camera_view.visibility == View.VISIBLE && show) {

    } else {
      var visibility = View.VISIBLE
      if (!show) {
        visibility = View.GONE
        llCode.visibility = View.VISIBLE

        stopCamera()
      } else {
        llCode.visibility = View.GONE
        getBaseActivity().hideKeyboard(this.view)
        startCameraSource()
      }


      camera_view.visibility = visibility
    }
  }

  override fun onPermissionAccepted() {
    startCameraSource()
  }
}

@IgnoreExtraProperties
class demoktWikiid {

  var demokt_wiki_id: Int? = 0

  constructor()

  constructor(demokt_wiki_id: Int) : this() {
    this.demokt_wiki_id = demokt_wiki_id
  }

}