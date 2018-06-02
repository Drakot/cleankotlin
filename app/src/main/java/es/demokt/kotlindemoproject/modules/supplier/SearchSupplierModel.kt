package es.demokt.kotlindemoproject.modules.supplier

import es.demokt.kotlindemoproject.modules.store.SupplierModel

class SearchSupplierModel {
  var currentSupplier: SupplierModel? = null
  var data: List<SupplierModel>? = null
  var lastPage: Int = 0
}