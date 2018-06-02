package es.demokt.kotlindemoproject.modules.store

import java.io.Serializable

class SupplierModel : Serializable {
  var description: String? = ""
  var businessName: String? = ""
  var image: String? = ""
  var name: String = ""
  var id: Int? = 0

  constructor()
  constructor(id: Int?) {
    this.id = id
  }
}