package um.comidar.models

import java.io.Serializable

class Dish(val dishId: Long,
           val name: String,
           val description: String,
           val restaurantId: Long,
           val price: Double,
           val enable: Boolean,
           val imageTemporaryUrl: String): Serializable