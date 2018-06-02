package es.demokt.kotlindemoproject.data

import android.net.Uri
import es.demokt.kotlindemoproject.data.network.home.ResponseItem
import es.demokt.kotlindemoproject.data.network.home.StatsAvgResponse
import es.demokt.kotlindemoproject.data.network.home.StatsTodayResponse
import es.demokt.kotlindemoproject.data.network.home.StatusRequest
import es.demokt.kotlindemoproject.data.network.login.LoginResponse
import es.demokt.kotlindemoproject.data.network.product.AddProductRequest
import es.demokt.kotlindemoproject.data.network.product.AddProductRequestBody
import es.demokt.kotlindemoproject.data.network.product.SearchProductResponse
import es.demokt.kotlindemoproject.data.network.product.UpdateProductRequestBody
import es.demokt.kotlindemoproject.data.network.signup.SignupRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreResponse
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierResponse
import es.demokt.kotlindemoproject.modules.MainApplication
import es.demokt.kotlindemoproject.modules.home.HomeModel
import es.demokt.kotlindemoproject.modules.home.Status
import es.demokt.kotlindemoproject.modules.home.UserStore
import es.demokt.kotlindemoproject.modules.login.Login
import es.demokt.kotlindemoproject.modules.login.Register
import es.demokt.kotlindemoproject.modules.product.result.Product
import es.demokt.kotlindemoproject.modules.store.SearchStoreModel
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel
import es.demokt.kotlindemoproject.utils.DateUtils
import es.demokt.kotlindemoproject.utils.FileUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Builder
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection
import java.util.Date

object Mapper {
  fun map(origin: LoginResponse?): Login? {
    val target = Login()
    if (origin != null) {

      target.token = origin.response.accessToken
      target.refreshToken = origin.response.refreshToken

      return target
    } else {
      return null
    }
  }

  fun map(origin: Register): SignupRequest {
    val target = SignupRequest()

    target.email = origin.email
    target.name = origin.name
    target.surname = origin.surname
    target.password = origin.password
    target.password_confirmation = origin.password

    return target
  }

  fun map(result: SearchStoreResponse?): SearchStoreModel? {
    val allStores: MutableList<UserStore> = mutableListOf()
    val storeModel = SearchStoreModel()

    if (result?.data?.response != null) {
      storeModel.lastPage = result.meta?.lastPage!!
      result.data.response.forEach {
        val user = UserStore()
        user.id = it.id!!
        user.name = it.nombre!!
        user.location = it.poblacion!!


        allStores.add(user)
      }
      storeModel.stores = allStores
    }

    return storeModel
  }

  fun map(result: SearchProductResponse?): MutableList<Product>? {
    var target: MutableList<Product>? = null

    if (result?.response?.products != null) {
      target = mutableListOf()
      result.response.products.forEach {
        val data = Product()

        data.supplierId = it.supplierId

        data.productObservation = it.productObservation
        data.storeId = it.store?.storeId

        data.name = it.productName
        data.supplierRef = it.supplierRef
        data.price = it.store?.price
        data.eanValue = null
        data.productId = it.productId!!

        data.images = mutableListOf()

        it.images?.forEach {
          if (it.image_url != null)
            (data.images as MutableList<String>).add(it.image_url)
        }

        target.add(data)
      }

    }


    return target
  }

  fun map(origin: Product): AddProductRequest {
    val target = AddProductRequest()

    target.supplierId = origin.supplierId

    target.productObservation = origin.productObservation
    target.storeId = origin.storeId

    target.productName = origin.name
    target.supplierRef = origin.supplierRef
    target.price = origin.price
    target.eanValue = origin.eanValue

    val images: List<Part> = mutableListOf()
    origin.images?.forEach {
      val file1Uri = Uri.fromFile(File(it))
      val file = prepareFilePart("images", file1Uri)
      //var file = MultipartBody.Part.create(MediaType.parse("image/*"), it)
      (images as MutableList<Part>).add(file)
    }
    target.images = images

    return target
  }

  private val TXT_PLAIN = "text/plain"
  fun mapRequest(origin: Product): AddProductRequestBody {
    val target = AddProductRequestBody()

    target.supplierId =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.supplierId.toString())
    target.productObservation =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.productObservation)
    target.storeId =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.storeId.toString())
    target.productName =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.name)
    target.price =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.price.toString())
    target.supplierRef =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.supplierRef.toString())
    target.eanValue =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.eanValue.toString())

    val builder = Builder()
    builder.setType(MultipartBody.FORM)
    builder.addFormDataPart("content", "images")

    target.imagesList = origin.imagesList!!

    origin.images?.forEach {
      val file = File(it)
      builder.addFormDataPart(
          "images", file.name,
          RequestBody.create(MediaType.parse("image/*"), file)
      )
    }

    target.images = builder.build()

    return target
  }

  private fun prepareFilePart(partName: String, fileUri: Uri): Part {
    // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
    // use the FileUtil to get the actual file by uri
    val file = FileUtils.getFile(MainApplication.applicationContext(), fileUri)
    val mimeType = URLConnection.guessContentTypeFromName(file.name)
    val requestFile = RequestBody.create(MediaType.parse(mimeType), file)

    // MultipartBody.Part is used to send also the actual file name
    return Part.createFormData(partName, file.name, requestFile)
  }

  fun mapUpdateProduct(origin: Product): UpdateProductRequestBody {
    val target = UpdateProductRequestBody()

    target.productId = RequestBody.create(MediaType.parse(TXT_PLAIN), origin.productId.toString())
    target.productObservation = RequestBody.create(MediaType.parse(TXT_PLAIN), origin.productObservation.toString())
    target.productName = RequestBody.create(MediaType.parse(TXT_PLAIN), origin.name.toString())
    target.supplierId =  RequestBody.create(MediaType.parse(TXT_PLAIN), origin.supplierId.toString())
    target.supplierRef = RequestBody.create(MediaType.parse(TXT_PLAIN), origin.supplierRef.toString())
    target.price = RequestBody.create(MediaType.parse(TXT_PLAIN), origin.price.toString())

    val builder = Builder()
    builder.setType(MultipartBody.FORM)
    builder.addFormDataPart("content", "images")

    target.imagesList = origin.imagesList!!

    origin.images?.forEach {
      val file = File(it)
      builder.addFormDataPart(
          "images", file.name,
          RequestBody.create(MediaType.parse("image/*"), file)
      )
    }

    target.images = builder.build()
    return target
  }

  fun map(origin: SupplierModel): SupplierRequest {
    val target = SupplierRequest()

    target.supplierName =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.name)
    target.supplierDescription =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.description)
    target.supplierBusinessName =
        RequestBody.create(MediaType.parse(TXT_PLAIN), origin.businessName)
    if (origin.image != null && origin.image!!.isNotEmpty()) {
      target.supplierImage =
          RequestBody.create(MediaType.parse("image/*"), File(origin.image))
    }
    return target
  }

  fun map(origin: SupplierResponse): SupplierModel? {
    val target = SupplierModel()

    target.businessName = origin.supplierBusinessName
    target.description = origin.supplierDescription
    target.id = origin.supplierId
    target.name = origin.supplierName
    target.image = origin.supplierImage

    return target
  }

  fun map(result: SupplierByIdResponse): SupplierModel? {

    return map(result.response!!)
  }

  fun map(origin: SearchSupplierResponse?): SearchSupplierModel? {

    val data: MutableList<SupplierModel> = mutableListOf()
    val target = SearchSupplierModel()

    if (origin?.data?.response != null) {
      target.lastPage = origin.meta?.lastPage!!
      origin.data.response.forEach {
        val item = SupplierModel()
        item.businessName = it.razonSocial
        item.description = it.nota
        item.id = it.id
        item.name = it.nombre
        item.image = it.urlImagen

        data.add(item)
      }
      target.data = data
    }

    return target
  }

  fun map(origin: Int): SupplierByIdRequest {
    return SupplierByIdRequest(origin)
  }

  fun map(startDate: String?, endDate: String?): StatusRequest {
    val target = StatusRequest()

    target.start_date = startDate
    target.end_date = endDate
    target.custom_period = "1"

    return target
  }

  fun map(origin: List<ResponseItem>?): HomeModel {
    val target = HomeModel()
    val dateUtils = DateUtils()
    target.itemDate = mutableListOf()
    target.items = mutableListOf()
    origin?.forEach {
      (target.itemDate as MutableList<Date>).add(dateUtils.formatDate(it.date)!!)
      (target.items as MutableList<Float>).add(it.dailyNumber.toFloat())
    }

    return target
  }

  fun map(origin: StatsAvgResponse): Status? {
    val target = Status()

    target.added = origin.response.avgAdded.toInt()
    target.scanned = origin.response.avgScanned.toInt()
    target.updated = origin.response.avgUpdated.toInt()

    return target
  }

  fun map(origin: StatsTodayResponse): Status? {
    val target = Status()

    target.added = origin.response.numberAdded
    target.scanned = origin.response.numberScanned
    target.updated = origin.response.numberUpdated

    return target
  }

}