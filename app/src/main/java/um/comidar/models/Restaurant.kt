package um.comidar.models

class Restaurant(
    restaurantId : Long,
    name : String,
    description : String,
    direction : String,
    telephone : String,
    email : String,
    imageTemporaryUrl : String
) {
    var restaurantId : Long = 0
    var name : String = ""
    var description : String = ""
    var direction : String = ""
    var telephone : String = ""
    var email : String = ""
    var imageTemporaryUrl : String = ""

    init {
        this.restaurantId = restaurantId
        this.name = name
        this.description = description
        this.direction = direction
        this.telephone = telephone
        this.email = email
        this.imageTemporaryUrl = imageTemporaryUrl
    }

}