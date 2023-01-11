package com.accuratebits.tallkingheads.data

import java.util.*
import kotlin.random.Random

data class Talk(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val score: Int = 0,
    val given: Boolean = false
) {
    companion object {
        fun demo(prefix: String): List<Talk> = (1..10).map { Talk(title = "$prefix $it", score = Random.nextInt(1, 5)) }
    }
}
