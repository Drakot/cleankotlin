package es.demokt.kotlindemoproject.modules.main

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor
import es.demokt.kotlindemoproject.modules.login.Register

/**
 * Created by aluengo on 12/4/18.
 */
class RegisterInteractor : BaseInteractor<Register, Boolean>() {

  override fun execute() {
    DataManager.signup(requestData!!, object : MyCallback<Boolean> {

      override fun onSuccess(result: Boolean?) {
        callSuccess(result!!)
      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}