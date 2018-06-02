package es.demokt.kotlindemoproject.data.local.objects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserStoreStore : RealmObject() {

  @PrimaryKey
  var id: Int = 0
  var name: String = ""
  var location: String = ""

}