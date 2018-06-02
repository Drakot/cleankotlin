package es.demokt.kotlindemoproject.data

import es.demokt.kotlindemoproject.data.local.objects.LoginStore
import es.demokt.kotlindemoproject.data.local.objects.UserStoreStore
import es.demokt.kotlindemoproject.modules.home.UserStore
import es.demokt.kotlindemoproject.modules.login.Login

object StoreMapper {
  fun map(origin: Login?): LoginStore? {
    val target = LoginStore()
    if (origin != null) {

      target.token = origin.token
      target.refreshToken = origin.refreshToken

      return target
    } else {
      return null
    }
  }

  fun map(origin: LoginStore?): Login? {
    val target = Login()
    if (origin != null) {

      target.token = origin.token
      target.refreshToken = origin.refreshToken

      return target
    } else {
      return null
    }
  }

  fun map(origin: UserStoreStore?): UserStore? {

    val target = UserStore()
    if (origin != null) {

      target.id = origin.id
      target.location = origin.location
      target.name = origin.name

      return target
    } else {
      return null
    }
  }

  fun map(origin: UserStore): UserStoreStore {
    val target = UserStoreStore()

    target.id = origin.id
    target.location = origin.location
    target.name = origin.name

    return target
  }

}