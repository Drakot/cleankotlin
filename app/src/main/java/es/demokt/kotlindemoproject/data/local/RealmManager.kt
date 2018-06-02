package es.demokt.kotlindemoproject.data.local

import android.util.Log
import es.demokt.kotlindemoproject.data.StoreMapper
import es.demokt.kotlindemoproject.data.local.objects.LoginStore
import es.demokt.kotlindemoproject.data.local.objects.UserStoreStore
import es.demokt.kotlindemoproject.modules.MainApplication
import es.demokt.kotlindemoproject.modules.home.UserStore
import es.demokt.kotlindemoproject.modules.login.Login
import io.realm.RealmObject

/**
 * Realm Database
 * Created by aluengo on 26/2/18.
 */
object RealmManager {

  private val app by lazy { MainApplication.instance }

  fun saveAuth(login: Login?) {
    val realm = app.getDatabase()
    realm.executeTransaction { db ->

      db.copyToRealmOrUpdate(StoreMapper.map(login))
    }

  }

  fun getAuth(): Login? {
    val realm = app.getDatabase()
    val result: LoginStore? = realm.where(LoginStore::class.java)
        .findFirst()

    return StoreMapper.map(result)
  }

  fun deleteAll() {
    val realm = app.getDatabase()
    realm.executeTransaction { db ->
      realm.deleteAll()
    }

  }

  fun getCurrentStore(): UserStore? {
    val result: UserStoreStore? = getStoreModel(UserStoreStore::class.java)

    return StoreMapper.map(result)
  }

  fun saveStore(
    data: UserStore,
    callback: (item: UserStore?) -> Unit
  ) {
    val realm = app.getDatabase()
    realm.executeTransaction{ db ->
      db.delete(UserStoreStore::class.java)
      db.copyToRealmOrUpdate(StoreMapper.map(data))
    }

    val currentStore = getCurrentStore()
    Log.e("RealmManager saveStore", currentStore.toString())
    callback(currentStore)
  }

  private fun <T : RealmObject> getStoreModel(storeModelClass: Class<T>): T? {
    var data: T? = null
    try {
      val realm = app.getDatabase()
      realm.beginTransaction()
      data = realm.where(storeModelClass)
          .findFirst()
      realm.commitTransaction()
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return data
  }

}