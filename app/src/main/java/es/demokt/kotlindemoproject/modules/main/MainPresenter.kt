package es.demokt.kotlindemoproject.modules.main

import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.modules.base.main.MainMVP

/**
 * Created by aluengo on 12/4/18.
 */
class MainPresenter(private val view: MainView) : MainMVP.Presenter {
  val interactor: MainInteractor = MainInteractor()
  fun init() {
    view.showProgress(true)
    interactor.doRequest(null, object : MainMVP.Interactor.CallbackResult<Boolean> {

      private val logTag: String? = javaClass.simpleName

      override fun onSuccess(result: Boolean) {
        Log.d(logTag, "SUCCESS")
        if (result) {
          view.goToHome()
        } else {
          view.goToLogin()
        }
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