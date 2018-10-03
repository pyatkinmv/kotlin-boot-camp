package io.rybalkinsd.kotlinbootcamp.practice

/**
 * NATO phonetic alphabet
 */
val alphabet = setOf("Alfa", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliett", "Kilo", "Lima", "Mike", "November", "Oscar", "Papa", "Quebec", "Romeo", "Sierra", "Tango", "Uniform", "Victor", "Whiskey", "Xray", "Yankee", "Zulu")

/**
 * A mapping for english characters to phonetic alphabet.
 * [ a -> Alfa, b -> Bravo, ...]
 */
val association: Map<Char, String> = alphabet.associateBy { x -> x[0].toLowerCase() }

/**
 * Extension function for String which encode it according to `association` mapping
 *
 * @return encoded string
 *
 * Example:
 * "abc".encode() == "AlfaBravoCharlie"
 *
 */
fun String.encode(): String = map { it.toLowerCase() }
                .map { association[it] ?: it }
                .joinToString("")

/**
 * A reversed mapping for association
 * [ alpha -> a, bravo -> b, ...]
 */
val reversedAssociation: Map<String, Char> = association.keys.associateBy { x -> association[x]!! }

/**
 * Extension function for String which decode it according to `reversedAssociation` mapping
 *
 * @return encoded string or null if it is impossible to decode
 *
 * Example:
 * "alphabravocharlie".decode() == "abc"
 * "charliee".decode() == null
 *
 */
fun String.decode(): String? {
    var res = StringBuilder(this)
    var isThereWord = false
    alphabet.forEach {
        var index = res.lastIndexOf(it)
        while (index != -1) {
            isThereWord = true
            if (index + it.length < res.length && res[index + it.length].isLowerCase()) return@decode null
            res.replace(index, index + it.length, it[0].toString())
            index = res.lastIndexOf(it)
        }
    }
    if (!isThereWord) return null
    return res.toString().toLowerCase()
}
