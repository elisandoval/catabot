package catabot

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("catabot")
                .mainClass(Application.javaClass)
                .start()
    }
}
