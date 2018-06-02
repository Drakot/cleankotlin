package es.demokt.kotlindemoproject.modules.main

import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.home.HomeModel
import es.demokt.kotlindemoproject.modules.home.Status
import es.demokt.kotlindemoproject.modules.home.UserStore

/**
 * Created by aluengo on 12/4/18.
 */
interface HomeView : MainMVP.View {
  fun setStore(result: UserStore)
  fun goToStoreSelection()
  fun setChartData(result: HomeModel)
  fun setAverage(status: Status?)
  fun setToday(status: Status?)
}