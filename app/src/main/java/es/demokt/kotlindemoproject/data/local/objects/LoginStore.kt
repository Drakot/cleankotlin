package es.demokt.kotlindemoproject.data.local.objects

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by aluengo on 26/2/18.
 */
open class LoginStore : RealmObject() {

  @PrimaryKey
  var email: String = ""
  var password: String = ""
  var token: String = ""
  var refreshToken: String = ""

}