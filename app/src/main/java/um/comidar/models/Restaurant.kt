package um.comidar.models

import java.io.Serializable

class Restaurant(val imageResId: Int,
                val restaurantId: Long = 0,
                val name: String,
                val description: String,
                val direction: String,
                val telephone: String,
                val email: String,
                val imageTemporaryUrl: String): Serializable
