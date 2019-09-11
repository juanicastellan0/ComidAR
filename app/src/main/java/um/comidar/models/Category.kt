package um.comidar.models

class Category(categoryId : Long, name : String) {
    var categoryId : Long = 0
    var name : String = ""

    init {
        this.categoryId = categoryId
        this.name = name
    }
}