package es.demokt.kotlindemoproject.modules.home

import java.util.Date

class HomeModel {
  var items: List<Float>? = null
  var itemDate: List<Date>? = null
  var store: UserStore? = null
  var todayStatus: Status = Status()
  var averageStatus: Status = Status()
}

class Status {
  var updated: Int = 0
  var scanned: Int = 0
  var added: Int = 0
}