package io.rybalkinsd.kotlinbootcamp.geometry

import kotlin.math.max
import kotlin.math.min

/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
}

/**
 * 2D point with integer coordinates
 */
data class Point(val x: Int, val y: Int) : Collider {
    override fun isColliding(other: Collider): Boolean =
         when (other) {
            is Point -> other == this
            is Bar -> other.isColliding(this)
            else -> false
        }
    }
/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
class Bar(val firstCornerX: Int, val firstCornerY: Int, val secondCornerX: Int, val secondCornerY: Int) : Collider {
    val minX: Int = min(firstCornerX, secondCornerX)
    val maxX: Int = max(firstCornerX, secondCornerX)
    val minY: Int = min(firstCornerY, secondCornerY)
    val maxY: Int = max(firstCornerY, secondCornerY)

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Bar) return false
        return minX == other.minX && maxX == other.maxX && minY == other.minY && maxY == other.maxY
    }

    fun doesContainPartOf(other: Bar): Boolean {
        return (other.firstCornerX in minX..maxX || other.secondCornerX in minX..maxX) &&
                (other.firstCornerY in minY..maxY || other.secondCornerY in minY..maxY)
    }

    fun doesContainPoint(point: Point): Boolean {
        return point.x in minX..maxX && point.y in minY..maxY
    }

    override fun isColliding(other: Collider): Boolean =
            when (other) {
                is Point -> doesContainPoint(other)
                is Bar -> doesContainPartOf(other) || other.doesContainPartOf(this)
                else -> false
            }
}