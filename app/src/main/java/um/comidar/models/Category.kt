package um.comidar.models

import java.io.Serializable

data class Category(val categoryId: Long = 0,
                    val name: String,
                    val imageTemporaryUrl: String): Serializable