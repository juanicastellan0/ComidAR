package um.comidar.models

class Dish(
    dishId : Long,
    name : String,
    description : String,
    restaurantId : Long,
    price : Double,
    enable : Boolean
) {
    var dishId : Long = 0
    var name : String = ""
    var description : String = ""
    var restaurantId : Long = 0
    var price : Double = .0
    var enable : Boolean = false

    init {
        this.dishId = dishId
        this.name = name
        this.description = description
        this.restaurantId = restaurantId
        this.price = price
        this.enable = enable
    }
}