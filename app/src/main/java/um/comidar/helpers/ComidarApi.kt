package um.comidar.helpers

import okhttp3.*

object ComidarApi {
    private const val baseURL = "http://comidar.herokuapp.com"
    private val client = OkHttpClient()

    fun getList(callback : Callback, resource: String) {
        val url = "$baseURL/admin/$resource/list/all"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }

    fun getRestaurantsByCategoryId(callback: Callback, categoryId: Long) {
        val url = "$baseURL/admin/restaurant/list/search?categoryId=$categoryId"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }

    fun getDishesByRestaurantId(callback: Callback, restaurantId: Long) {
        val url = "$baseURL/admin/dish/list/search?restaurantId=$restaurantId"
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(callback)
    }
}