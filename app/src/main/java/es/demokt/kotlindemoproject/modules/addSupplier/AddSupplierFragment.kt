package es.demokt.kotlindemoproject.modules.main

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.utils.Extras
import es.demokt.kotlindemoproject.utils.FileUtil
import kotlinx.android.synthetic.main.fragment_add_supplier.ivSelectPicture
import kotlinx.android.synthetic.main.fragment_add_supplier.ivSupplierPicture
import kotlinx.android.synthetic.main.fragment_add_supplier.tvSupplierBusinessName
import kotlinx.android.synthetic.main.fragment_add_supplier.tvSupplierName
import kotlinx.android.synthetic.main.fragment_add_supplier.tvSupplierObservation
import java.io.File

open class AddSupplierFragment : BaseFragment(), AddSupplierView {

  private val presenter: AddSupplierPresenter by lazy { AddSupplierPresenter(this) }
  internal val CAMERA_RESULT = 0
  internal val REQUEST_CODE = 3

  val PICTURE = 0
  val GALLERY = 1
  val EXTRA_SUPPLIER = "supplier"
  internal var selectedMediaOption = 0
  private var filePath: String? = ""
  val RESULT_OK = 0

  override fun onCreateViewId(): Int {
    return R.layout.fragment_add_supplier
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun viewCreated(view: View?) {
    configurator?.title = getString(R.string.title_add_supplier)
    ivSelectPicture.setOnClickListener {
      showSelectionList()
    }
    presenter.init(arguments)
  }

  override fun hasToolbar(): Boolean {
    return true
  }

  override fun hasToolbarBackButton(): Boolean {
    return true
  }

  override fun setData(supplier: SupplierModel?) {
    if (supplier?.id != 0) {
      tvSupplierName.setText(supplier?.name)
      tvSupplierBusinessName.setText(supplier?.businessName)
      tvSupplierObservation.setText(supplier?.description)
      tvSupplierName.tag = supplier?.id
      if (supplier?.image?.isNotBlank()!!) {
        Picasso.get().load(supplier.image).into(ivSupplierPicture)
      }
    }

  }

  fun idIsValid(): Boolean {
    return tvSupplierName.tag != null && tvSupplierName.tag is Int
  }

  override fun retrieveData(): SupplierModel {
    val data = SupplierModel()

    if (idIsValid()) {
      data.id = tvSupplierName.tag as Int
    }

    data.description = tvSupplierObservation.text.toString()
    data.name = tvSupplierName.text.toString()
    data.businessName = tvSupplierBusinessName.text.toString()
    if (filePath != null && filePath?.isNotBlank()!!)
      data.image = filePath
    return data

  }

  private fun showSelectionList() {
    val dialogBuilder = AlertDialog.Builder(activity!!)

    dialogBuilder.setTitle(getString(R.string.select_item))
    val values = resources.getStringArray(R.array.media_options)
    dialogBuilder.setItems(
        values
    ) { dialog, which -> openCamera(which) }
    val dialog = dialogBuilder.create()
    dialog.show()
  }

  private fun openCamera(option: Int) {
    selectedMediaOption = option
    var intent: Intent? = null
    if (PICTURE == selectedMediaOption) {
      intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      startActivityForResult(intent, CAMERA_RESULT)
    } else if (GALLERY == selectedMediaOption) {
      intent = Intent()
      intent.action = Intent.ACTION_PICK
      intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
      intent.type = "image/*"
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_RESULT)
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    val activityResult = ActivityResult(requestCode, data)
    if (activityResult.resultCode == RESULT_OK) {
      onPictureTaken(activityResult)
    } else {
      Log.w(LOG_TAG, "result.resultCode not ok")
    }
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    inflater: MenuInflater
  ) {
    super.onCreateOptionsMenu(menu, inflater)
    val messages = menu.add(0, 1, 0, "")
        .setIcon(R.drawable.ic_done_black_24dp)

    MenuItemCompat.setShowAsAction(messages, MenuItemCompat.SHOW_AS_ACTION_ALWAYS)

    val drawable = messages.icon
    // If we don't mutate the drawable, then all drawable's with this id will have a color
    // filter applied to it.
    drawable?.mutate()
    drawable?.setTint(resources.getColor(R.color.white))
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == 1) {
      presenter.save()
    }
    return false
  }

  private fun onPictureTaken(result: ActivityResult) {
    val data = result.resultData
    if (selectedMediaOption == PICTURE) {
      val bitmap = FileUtil.getImageDataFromIntent(result.resultData)
      val imagePath = FileUtil.saveToInternalStorage(bitmap, activity)
      setFilePath(imagePath)

    } else if (selectedMediaOption == GALLERY && data != null) {
      val path = data.data!!.path
      if (path.contains("image")) {
        this.filePath = File(getRealPathFromURI(data.data!!)).absolutePath
        val path = FileUtil.saveToInternalStorage(activity, data.data!!)
        Picasso.get().load(data.data!!)
            .centerInside().fit().into(ivSupplierPicture)
        this.filePath = path
      }

    }
    Log.i(LOG_TAG, filePath)
  }

  private fun getRealPathFromURI(contentURI: Uri): String {
    val result: String
    val cursor = activity!!.getContentResolver().query(contentURI, null, null, null, null)
    if (cursor == null) { // Source is Dropbox or other similar local file path
      result = contentURI.path
    } else {
      cursor.moveToFirst()
      val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
      result = cursor.getString(idx)
      cursor.close()
    }
    return result
  }

  private fun setFilePath(filePath: String) {
    this.filePath = filePath
    setImage(filePath)
  }

  private fun setImage(filePath: String) {

    val file = File(filePath)
    val uri = Uri.fromFile(file)
    ivSupplierPicture.setImageURI(uri)
  }

  override fun context(): Context {
    return context!!
  }

  override fun goToHome() {
    getNavigator().clearBackStack().clearExtras().addBackStack(false).navigateToHome()
  }

  override fun goBackToList(result: SupplierByIdResponse) {
    Log.w(LOG_TAG, " => " + result.response?.supplierName)

    getNavigator().addExtra(Extras.SEARCH_SUPPLIER, result.response?.supplierName!!)
    getNavigator().goBack()
  }
}

