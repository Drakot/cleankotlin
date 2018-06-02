package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor
import es.demokt.kotlindemoproject.modules.home.UserStore

/**
 * Created by aluengo on 18/4/18.
 */
class SaveStoreInteractor : BaseInteractor<UserStore, UserStore>() {

  override fun execute() {
    DataManager.saveStore(requestData!!, object : MyCallback<UserStore> {

      override fun onSuccess(result: UserStore?) {
        callSuccess(result!!)
      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}