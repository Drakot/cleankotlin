package es.demokt.kotlindemoproject.modules.store

import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.home.UserStore

open class StorePresenter(private val view: StoreView) : MainMVP.Presenter {

  fun onViewCreated() {
    searchStore(SearchStoreByNameRequest("A", "1"))
  }

  private val saveInteractor: SaveStoreInteractor by lazy { SaveStoreInteractor() }
  private val interactor: StoreInteractor by lazy { StoreInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun searchStore(reqData: SearchStoreByNameRequest, paginated: Boolean = false) {
    view.showProgress(true)
    interactor.doRequest(reqData, object : MainMVP.Interactor.CallbackResult<SearchStoreModel> {

      override fun onSuccess(result: SearchStoreModel) {
        view.showList(result.stores!!.isNotEmpty(), result.lastPage)
        if(!paginated) {
          view.setList(result)
        } else {
          view.addToList(result)
        }
        view.showProgress(false)
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

  fun onItemSelected(it: UserStore) {
    view.showProgress(true)
    saveInteractor.doRequest(it, object : MainMVP.Interactor.CallbackResult<UserStore> {

      override fun onSuccess(result: UserStore) {
        view.goBack()
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