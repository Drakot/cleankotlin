package es.demokt.kotlindemoproject.modules.main

import android.util.Log
import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.home.ResponseItem
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor
import es.demokt.kotlindemoproject.modules.home.HomeModel
import es.demokt.kotlindemoproject.modules.home.Status
import es.demokt.kotlindemoproject.utils.DateUtils
import java.util.Calendar
import java.util.Date

/**
 * Created by aluengo on 17/4/18.
 */
class HomeInteractor : BaseInteractor<Void, HomeModel>() {
  val dateUtils = DateUtils()
  private var endDate: String? = null
  private var startDate: String? = null
  val model = HomeModel()

  override fun execute() {
    endDate = dateUtils.formatDate(Date())
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, -15)
    startDate = dateUtils.formatDate(calendar.time)

    DataManager.getCurrentStore {
      model.store = it
      callChartStats()
    }
  }

  private fun callChartStats() {

    Log.i("HomeInteractor", startDate)
    Log.i("HomeInteractor", endDate)

    DataManager.getScanStatus(startDate, endDate, object : MyCallback<List<ResponseItem>> {
      override fun onSuccess(result: List<ResponseItem>?) {
        var items: List<Float>? = mutableListOf()
        var itemDate: List<Date>? = mutableListOf()

        val dateUtils = DateUtils()

        val dates = dateUtils.getDates(startDate!!, endDate!!)

        model.items = mutableListOf()
        model.itemDate = mutableListOf()

        result?.forEach {
          (itemDate as MutableList<Date>).add(dateUtils.formatDate(it.date)!!)
          (items as MutableList<Float>).add(it.dailyNumber.toFloat())
        }

        dates.forEach {
          var value = 0f
          if (itemDate?.contains(it)!!) {
            value = items?.get(itemDate.indexOf(it))!!

          }
          (model.items as MutableList<Float>).add(value)
        }

        model.itemDate = dates

        callStats()

      }

      override fun onError(error: ErrorReponse) {
        callStats()
      }

    })
  }

  private fun callStats() {
    DataManager.getStatsToday(object : MyCallback<Status> {

      override fun onSuccess(result: Status?) {
        model.todayStatus = result!!
        callAverageStats()
      }

      override fun onError(error: ErrorReponse) {
        callAverageStats()
      }

    })
  }

  private fun callAverageStats() {
    DataManager.getStatsAverage(startDate, endDate, object : MyCallback<Status> {

      override fun onSuccess(result: Status?) {
        model.averageStatus = result!!
        callSuccess(model)
      }

      override fun onError(error: ErrorReponse) {
        callSuccess(model)
      }
    })
  }
}