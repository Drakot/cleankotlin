package es.demokt.kotlindemoproject.modules.main

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.login.LoginRequest
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor

/**
 * Created by aluengo on 12/4/18.
 */
class LoginInteractor : BaseInteractor<LoginRequest, Boolean>() {

  override fun execute() {
    DataManager.login(requestData!!, object : MyCallback<Boolean> {

      override fun onSuccess(result: Boolean?) {
        callSuccess(result!!)
      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}