package es.demokt.kotlindemoproject.modules

import android.app.Application
import android.content.Context
import android.content.Intent
import com.karumi.dexter.Dexter
import es.demokt.kotlindemoproject.modules.main.MainActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.demokt.kotlindemoproject.R.string

class MainApplication : Application() {

  private lateinit var config: RealmConfiguration

  init {
    instance = this
    development = true
  }

  companion object {
    lateinit var instance: MainApplication

    fun applicationContext(): Context {
      return instance.applicationContext
    }

    var development: Boolean = true
    lateinit var refdb: DatabaseReference

  }

  override fun onCreate() {
    super.onCreate()
    val context: Context = MainApplication.applicationContext()
    Dexter.initialize(this);
    Realm.init(context)
    config = RealmConfiguration.Builder()
        .deleteRealmIfMigrationNeeded()
        .name("database.realm")
        .build()
    Realm.setDefaultConfiguration(config)

    val database = FirebaseDatabase.getInstance()
    Companion.refdb = database.getReference(getString(string.demokt_wiki_id))
  }

  fun getDatabase(): Realm {
    return Realm.getInstance(config)
  }

  fun logOut() {
    val i = Intent(applicationContext, MainActivity::class.java)
    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(i)
  }
}