package es.demokt.kotlindemoproject.modules.main

import android.os.Bundle
import android.util.Log
import es.demokt.kotlindemoproject.R
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.MandatoryFields
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.login.Register
import es.demokt.kotlindemoproject.modules.product.result.Product
import es.demokt.kotlindemoproject.modules.store.GetSupplierInteractor
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.utils.Extras
import es.demokt.kotlindemoproject.utils.Valid

/**
 * Created by aluengo on 12/4/18.
 */
class AddProductPresenter(private val view: AddProductView) : MainMVP.Presenter {
  private val interactor: AddProductInteractor by lazy { AddProductInteractor() }
  private val getSupplierInteractor: GetSupplierInteractor by lazy { GetSupplierInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun callInteractor(data: Product) {
    view.showProgress(true)
    interactor.doRequest(data, object : MainMVP.Interactor.CallbackResult<Boolean> {

      override fun onSuccess(result: Boolean) {
        view.goToHome()
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

  fun okClicked(register: Register) {
    if (Valid.isEmailValid(register.email) && Valid.isPasswordValid(register.password)) {

    } else {
      view.onError("Error")
    }
  }

  fun save() {
    Log.i(logTag, "save")
    val data = view.retrieveData()
    var error = ""
    when {
      data.eanValue.isNullOrEmpty() -> error = view.context()
          .getString(R.string.error_mandatort_field)
      data.name.isNullOrEmpty() -> error = view.context()
          .getString(R.string.error_mandatort_field)
      data.supplierId == 0 -> error = view.context()
          .getString(R.string.error_mandatort_field)
      data.price == 0.0 -> error = view.context()
          .getString(R.string.error_mandatort_field)
      else -> callInteractor(data)
    }
    if (error.isNotEmpty())
      view.checkError(MandatoryFields)
  }

  fun init(arguments: Bundle?) {
    if (arguments?.getSerializable(Extras.PRODUCT) != null) {
      val product = arguments.getSerializable(Extras.PRODUCT) as Product?

      view.showProgress(true)
      getSupplierInteractor.doRequest(product!!.supplierId, object : MainMVP.Interactor.CallbackResult<SupplierModel> {

        override fun onSuccess(result: SupplierModel) {
          view.setData(product)
          view.setSupplier(result.name)
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
  }

}