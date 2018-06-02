package es.demokt.kotlindemoproject.modules.main

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor

/**
 * Created by aluengo on 12/4/18.
 */
class MainInteractor : BaseInteractor<Void, Boolean>() {

  override fun execute() {
    callSuccess(!DataManager.getAuth()?.token.isNullOrEmpty())
  }
}