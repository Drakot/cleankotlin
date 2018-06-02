package es.demokt.kotlindemoproject.modules.main

import android.os.Bundle
import android.util.Log
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.MandatoryFields
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.store.SaveSupplierInteractor
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.utils.Extras
import java.io.File

/**
 * Created by aluengo on 12/4/18.
 */
class AddSupplierPresenter(private val view: AddSupplierView) : MainMVP.Presenter {
  private val interactor: SaveSupplierInteractor by lazy { SaveSupplierInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun callInteractor(data: SupplierModel) {
    view.showProgress(true)
    interactor.doRequest(data, object : MainMVP.Interactor.CallbackResult<SupplierByIdResponse> {

      override fun onSuccess(result: SupplierByIdResponse) {
        if (data.image != null && data.image!!.isNotEmpty())
          deleteRecursive(File(data.image).parentFile)
        //view.goToHome()
        view.goBackToList(result)
      }

      override fun onError(error: ErrorReponse) {
        Log.d(logTag, error.toString())
        view.checkError(error)
      }

      override fun onFinish() {
        super.onFinish()
        view.showProgress(false)
      }

    })
  }

  fun deleteRecursive(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory)
      for (child in fileOrDirectory.listFiles())
        deleteRecursive(child)

    fileOrDirectory.delete()

  }

  fun save() {
    Log.i(logTag, "save")
    val data = view.retrieveData()
    var error = ""
    when {
      data.businessName.isNullOrEmpty() -> error = view.context()
          .getString(R.string.error_mandatort_field)
      data.description.isNullOrEmpty() -> error = view.context()
          .getString(R.string.error_mandatort_field)
      data.name.isNullOrEmpty() -> error = view.context()
          .getString(R.string.error_mandatort_field)
      else -> callInteractor(data)
    }
    if (error.isNotEmpty())
      view.checkError(MandatoryFields)
  }

  fun init(arguments: Bundle?) {
    if (arguments?.getSerializable(Extras.SUPPLIER) != null) {
      val supplier = arguments.getSerializable(Extras.SUPPLIER) as SupplierModel?
      view.setData(supplier)
    }

  }

}