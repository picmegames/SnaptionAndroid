package com.snaptiongame.app.presentation

/**
 * @author Tyler Wong
 */

interface BaseView<T> {
    fun setPresenter(presenter: T)
}
