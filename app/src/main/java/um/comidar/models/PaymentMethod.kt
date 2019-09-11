package um.comidar.models

class PaymentMethod(paymentMethodId : Long, name : String) {
    var paymentMethodId : Long = 0
    var name : String = ""

    init {
        this.paymentMethodId = paymentMethodId
        this.name = name
    }
}