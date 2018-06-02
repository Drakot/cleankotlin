package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.modules.home.UserStore

class SearchStoreModel {
  var currentStore: UserStore? = null
  var stores : List<UserStore>? = null
  var lastPage : Int = 0
}