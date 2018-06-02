package es.demokt.kotlindemoproject.modules.home

class UserStore {

  var id: Int = 0
  var name: String = ""
  var location: String = ""

  override fun toString(): String {
    return id.toString() + " " + name + " " + location
  }
}