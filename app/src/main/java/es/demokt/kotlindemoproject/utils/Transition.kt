package es.demokt.kotlindemoproject.utils

import android.view.View

/**
 * @author by AlbertoLuengo on 21/7/16.
 */
class Transition {
    var transitionMode = TransitionMode.LEFT_TO_RIGHT
    lateinit var sharedElement: View
    lateinit var transitionName: String
    var transitionResource: Int = 0

    constructor(transitionMode: TransitionMode) {
        this.transitionMode = transitionMode
    }

    constructor()

    enum class TransitionMode {
        LEFT_TO_RIGHT, DOWN_TO_UP
    }

    fun sharedElement(sharedElement: View): Transition {
        this.sharedElement = sharedElement
        return this
    }

    fun transitionName(transitionName: String): Transition {
        this.transitionName = transitionName
        return this
    }

    fun transitionResource(transitionResource: Int): Transition {
        this.transitionResource = transitionResource
        return this
    }
}
