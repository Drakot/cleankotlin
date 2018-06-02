package es.demokt.kotlindemoproject.modules.base.main

import es.demokt.kotlindemoproject.data.api.ErrorReponse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * BaseInteractor
 * Created by aluengo on 14/2/18.
 */
abstract class BaseInteractor<O, C>() : MainMVP.Interactor {

  var requestData: O? = null
  lateinit var callback: MainMVP.Interactor.CallbackResult<C>

  constructor(init: BaseInteractor<O, C>.() -> Unit) : this() {
    init()
  }

  fun callSuccess(result: C) {
    doAsync {
      uiThread {
        callback.onFinish()
        callback.onSuccess(result)
      }
    }
  }

  fun callError(err: ErrorReponse) {
    doAsync {
      uiThread {
        callback.onFinish()
        callback.onError(err)
      }
    }
  }

  override fun execute() {
  }

  fun run() {
    doAsync {
      execute()
    }
  }

  fun doRequest(
    data: O?,
    call: MainMVP.Interactor.CallbackResult<C>
  ) {
    requestData = data
    callback = call
    run()
  }

}