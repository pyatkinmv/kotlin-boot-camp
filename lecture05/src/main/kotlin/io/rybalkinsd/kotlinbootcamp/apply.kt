package io.rybalkinsd.kotlinbootcamp

class User(val id: Long) {
    var firstName: String? = null
    var lastName: String? = null

    var a1: Any? = null
    var a2: Any? = null
    var a3: Any? = null
    var a4: Any? = null
    var a5: Any? = null
    override fun toString(): String {
        return "User(id=$id, firstName=$firstName, lastName=$lastName)"
    }
}
