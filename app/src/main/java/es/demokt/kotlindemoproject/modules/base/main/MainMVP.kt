package es.demokt.kotlindemoproject.modules.base.main

import es.demokt.kotlindemoproject.data.api.ErrorReponse

/**
 * MainMVP
 * Created by aluengo on 9/2/18.
 */
class MainMVP {

  interface View {
    fun onSuccess(s: String) {}
    fun onError(s: String) {}
    fun showProgress(show: Boolean) {}
    fun checkError(error: ErrorReponse) {}
  }

  interface Presenter {
    fun showMessage(message: String) {}
    fun showError(message: String) {}
    fun goToLogin() {}
    fun hideKeyBoard() {}
  }

  interface Interactor {
    fun execute()

    interface CallbackResult<in T> {
      fun onSuccess(result: T)
      fun onError(error: ErrorReponse)
      fun onFinish() {}
    }
  }

}