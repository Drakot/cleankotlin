package es.demokt.kotlindemoproject.modules.main

import android.app.AlertDialog.Builder
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import com.squareup.picasso.Picasso
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.R.layout
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Cancelled
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Error
import es.demokt.kotlindemoproject.modules.addProduct.CurrentProduct
import es.demokt.kotlindemoproject.modules.base.BaseFragment
import es.demokt.kotlindemoproject.modules.product.result.Product
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.utils.Extras
import es.demokt.kotlindemoproject.utils.Extras.SUPPLIER
import es.demokt.kotlindemoproject.utils.FileUtil
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.dialog_img.view.phView
import kotlinx.android.synthetic.main.fragment_add_product.imageListPreview
import kotlinx.android.synthetic.main.fragment_add_product.imgListPrev
import kotlinx.android.synthetic.main.fragment_add_product.ivSelectPicture
import kotlinx.android.synthetic.main.fragment_add_product.llPictures
import kotlinx.android.synthetic.main.fragment_add_product.llProvider
import kotlinx.android.synthetic.main.fragment_add_product.tvProductEan
import kotlinx.android.synthetic.main.fragment_add_product.tvProductName
import kotlinx.android.synthetic.main.fragment_add_product.tvProductObservation
import kotlinx.android.synthetic.main.fragment_add_product.tvProductPrice
import kotlinx.android.synthetic.main.fragment_add_product.tvProductProvider
import kotlinx.android.synthetic.main.fragment_add_product.tvProductReference
import kotlinx.android.synthetic.main.item_product_picture.view.btnDelete
import kotlinx.android.synthetic.main.item_product_picture.view.ivImage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource
import uk.co.senab.photoview.PhotoView
import java.io.File
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by aluengo on 12/4/18.
 */
open class AddProductFragment : BaseFragment(), AddProductView {

  private val presenter: AddProductPresenter by lazy { AddProductPresenter(this) }
  internal val CAMERA_RESULT = 0
  internal val REQUEST_CODE = 3

  val PICTURE = 0
  val GALLERY = 1
  internal var selectedMediaOption = 0
  private lateinit var filePaths: ArrayList<String>
  val RESULT_OK = 0

  override fun onCreateViewId(): Int {
    return R.layout.fragment_add_product
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    CurrentProduct.filePaths = ArrayList()
  }

  override fun viewCreated(view: View?) {
    configurator?.title = getString(R.string.title_add_product)
    ivSelectPicture.setOnClickListener {
      showSelectionList()
    }

    llProvider.setOnClickListener {
      showProviderList()
    }

    presenter.init(arguments)

    val currentEan = arguments?.getString(Extras.EAN)
    if (!currentEan.isNullOrEmpty()) {
      tvProductEan.setText(currentEan)
    }
  }

  private fun showProviderList() {
    val extras = Bundle()
    if (providerIsValid()) {
      extras.putSerializable(SUPPLIER, SupplierModel(llProvider.tag as Int))
    }
    getNavigator()
        .target(this, REQUEST_CODE)
        .setExtras(extras)
        .addBackStack(true)
        .navigateToSupplier()
  }

  override fun hasToolbar(): Boolean {
    return true
  }

  override fun hasToolbarBackButton(): Boolean {
    return true
  }

  override fun setData(product: Product?) {

    CurrentProduct.product = product

    if (product?.productId != 0) {
      tvProductObservation.setText(product?.productObservation)
      tvProductName.setText(product?.name)
      tvProductReference.setText(product?.supplierRef)
      tvProductPrice.setText(product?.price.toString())
      tvProductEan.setText(product?.eanValue)
      llProvider.tag = product?.supplierId
      tvProductProvider.text = llProvider.tag.toString()
      tvProductEan.tag = product?.productId
      reloadPreviewImages(product)

      getNavigator().clearExtras()
    }

  }

  private fun reloadPreviewImages(product: Product?) {
    if (product?.images != null) {
      if (product.images!!.count() > 0)
        imgListPrev.visibility = View.VISIBLE
      product.images?.forEach {

        val image = ImageView(context)
        Picasso.get().load(it).into(image)

        image.setOnClickListener({ curView ->
          val mBuilder = Builder(context)
          val mView = layoutInflater.inflate(layout.dialog_img, null)

          val photoView = PhotoView(context)
          Picasso.get().load(it).into(photoView)

          photoView.layoutParams =
              LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          photoView.scaleType = CENTER_CROP

          mView.phView.addView(photoView)

          mBuilder.setView(mView)
          val mDialog = mBuilder.create()
          mDialog.show()
        })

        imageListPreview.addView(image)

      }
    }

  }

  fun providerIsValid(): Boolean {
    return llProvider.tag != null && llProvider.tag is Int
  }

  fun idIsValid(): Boolean {
    return tvProductEan.tag != null && tvProductEan.tag is Int
  }

  override fun retrieveData(): Product {
    val data = Product()
    if (providerIsValid()) {
      data.supplierId = llProvider.tag as Int
    }

    if (idIsValid()) {
      data.productId = tvProductEan.tag as Int
    }

    data.productObservation = tvProductObservation.text.toString()
    data.storeId = 0

    data.name = tvProductName.text.toString()
    data.supplierRef = tvProductReference.text.toString()
    val price = tvProductPrice.text.toString()
    if (!price.isNullOrEmpty()) {
      data.price = price.toDouble()
    }
    data.eanValue = tvProductEan.text.toString()

    if (CurrentProduct.filePaths.count() > 0) {
      CurrentProduct.filePaths.forEach {
        val file = File(it)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("images[]", file.name, requestFile)
        data.imagesList.add(body)
      }
    }

    return data

  }

  private fun showSelectionList() {
    /*val dialogBuilder = AlertDialog.Builder(activity!!)

    dialogBuilder.setTitle(getString(R.string.select_item))
    val values = resources.getStringArray(R.array.media_options)
    dialogBuilder.setItems(
        values
    ) { dialog, which -> openCamera(which) }
    val dialog = dialogBuilder.create()
    dialog.show()*/
    openCamera(0)
  }

  private fun openCamera(option: Int) {
    EasyImage.openCamera(this, 0)
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)

    val activityResult = ActivityResult(requestCode, data)


    if (activityResult.resultCode == REQUEST_CODE) {
      val supplier = data?.getSerializableExtra(Extras.SUPPLIER) as SupplierModel
      llProvider.tag = supplier.id
      tvProductProvider.text = supplier.name
      setUpToolbar()

      // Reload images
      reloadPreviewImages(CurrentProduct.product)
      setFilePaths(CurrentProduct.filePaths)

      getNavigator().clearExtras().clearTarget()
    }

    // EasyImage
    EasyImage.handleActivityResult(requestCode, resultCode, data, activity, object : EasyImage.Callbacks {
      override fun onImagePicked(imageFile: File?, source: ImageSource?, type: Int) {
        val compressedImageFile = Compressor(context).compressToFile(imageFile);
        CurrentProduct.filePaths.add(compressedImageFile!!.path)
        setFilePaths(CurrentProduct.filePaths)
        Log.i(LOG_TAG, CurrentProduct.filePaths.size.toString() + " files")
      }

      override fun onImagePickerError(e: Exception?, source: ImageSource?, type: Int) {
        checkError(Error)
      }

      override fun onCanceled(source: ImageSource?, type: Int) {
        checkError(Cancelled)
      }

    })
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
      CurrentProduct.filePaths.add(imagePath)
    } else if (selectedMediaOption == GALLERY) {
      if (data != null) {
        val clipData = data.clipData
        if (clipData != null) {
          for (i in 0 until clipData.itemCount) {
            val uri = clipData.getItemAt(i)
                .uri
            Log.i(LOG_TAG, uri.path + " " + System.currentTimeMillis())
            val path = FileUtil.saveToInternalStorage(activity, uri)
            CurrentProduct.filePaths.add(path)
          }
        } else {
          val path = data.data!!.path
          if (path.contains("image")) {
            val galleryPath = FileUtil.saveToInternalStorage(activity, data.data)
            CurrentProduct.filePaths.add(galleryPath)
          }
        }
      }
    }
    setFilePaths(CurrentProduct.filePaths)
    Log.i(LOG_TAG, CurrentProduct.filePaths.size.toString() + " files")

  }

  private fun setFilePaths(filePaths: ArrayList<String>) {
    addImages(CurrentProduct.filePaths)
  }

  private fun addImages(filePaths: ArrayList<String>) {
    llPictures.removeAllViews()
    CurrentProduct.filePaths.forEachIndexed { index, filePath ->
      val itemView = activity!!.layoutInflater!!.inflate(R.layout.item_product_picture, null)

      val file = File(filePath)
      val uri = Uri.fromFile(file)
      itemView.ivImage.setImageURI(uri)
      Log.d(LOG_TAG, "Element => " + index)
      itemView.btnDelete.setOnClickListener({
        Log.d(LOG_TAG, "Remove element => " + index)
        CurrentProduct.filePaths.removeAt(index)
        addImages(CurrentProduct.filePaths)
      })
      llPictures.addView(itemView)
    }
  }

  override fun context(): Context {
    return context!!
  }

  override fun goToHome() {
    CurrentProduct.product = null
    CurrentProduct.filePaths = arrayListOf()
    getNavigator().clearBackStack().clearExtras().addBackStack(false).navigateToHome()
  }

  override fun setSupplier(name: String) {
    tvProductProvider.setText(name)
  }
}

