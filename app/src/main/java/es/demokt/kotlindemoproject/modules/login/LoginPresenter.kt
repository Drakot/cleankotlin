package es.demokt.kotlindemoproject.modules.main

import android.util.Log
import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.network.login.LoginRequest
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.utils.Valid

/**
 * Created by aluengo on 12/4/18.
 */
class LoginPresenter(private val view: RegisterView) : MainMVP.Presenter {
  private val interactor: LoginInteractor by lazy { LoginInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun callInteractor(loginRequest: LoginRequest) {
    view.showProgress(true)
    interactor.doRequest(loginRequest, object : MainMVP.Interactor.CallbackResult<Boolean> {

      override fun onSuccess(result: Boolean) {
        Log.d("login", "go Home")
        view.goToHome()
        Log.d("login", DataManager.getAuth()!!.token)

      }

      override fun onError(error: ErrorReponse) {
        Log.d(logTag, error.toString())
        view.showProgress(false)
        view.checkError(error)
      }

      override fun onFinish() {
        super.onFinish()
        view.showProgress(false)
      }

    })

  }

  fun okClicked(
    email: String,
    password: String
  ) {
    if (Valid.isEmailValid(email) && Valid.isPasswordValid(password)) {
      callInteractor(LoginRequest(email, password))
    } else {
      view.onError("bobo")
    }
  }

}