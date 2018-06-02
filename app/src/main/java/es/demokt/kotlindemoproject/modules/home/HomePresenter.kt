package es.demokt.kotlindemoproject.modules.home

import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.main.HomeInteractor
import es.demokt.kotlindemoproject.modules.main.HomeView

/**
 * Created by aluengo on 12/4/18.
 */
class HomePresenter(private val view: HomeView) : MainMVP.Presenter {
  private val interactor: HomeInteractor by lazy { HomeInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun callInteractor() {
    view.showProgress(true)
    interactor.callback = object : MainMVP.Interactor.CallbackResult<HomeModel> {

      override fun onSuccess(result: HomeModel) {
        Log.e(logTag, result.toString())
        if (result.items != null) {
          view.setChartData(result)
        }
        view.setToday(result.todayStatus)
        view.setAverage(result.averageStatus)
        if (result.store == null) {
          view.goToStoreSelection()
        } else {
          view.setStore(result.store!!)
        }

      }

      override fun onError(error: ErrorReponse) {
        //view.goToStoreSelection()
        view.showProgress(false)
        view.checkError(error)
      }

      override fun onFinish() {
        super.onFinish()
        view.showProgress(false)
      }

    }
    interactor.run()

  }

  fun init() {
    callInteractor()
  }

}