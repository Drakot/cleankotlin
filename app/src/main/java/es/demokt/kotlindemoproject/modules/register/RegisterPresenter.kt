package es.demokt.kotlindemoproject.modules.main

import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.EmailOrPass
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Error
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.login.Register
import es.demokt.kotlindemoproject.utils.Valid

/**
 * Created by aluengo on 12/4/18.
 */
class RegisterPresenter(private val view: RegisterView) : MainMVP.Presenter {
  private val interactor: RegisterInteractor by lazy { RegisterInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun callInteractor(register: Register) {
    view.showProgress(true)
    interactor.doRequest(register, object : MainMVP.Interactor.CallbackResult<Boolean> {

      override fun onSuccess(result: Boolean) {
        Log.d("register", "go Home")
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
      callInteractor(register)
    } else {
      view.checkError(EmailOrPass)
    }
  }

}