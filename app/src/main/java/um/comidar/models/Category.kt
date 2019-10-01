package um.comidar.models

import java.io.Serializable

data class Category(val imageResId: Int,
                    val categoryId: Long = 0,
                    val name: String): Serializable