package com.jbouffard.bludbourne

import com.jbouffard.bludbourne.screens.MainGameScreen
import com.badlogic.gdx.Game


class Bludbourne extends Game {
  final val mainGameScreen = new MainGameScreen()

  override def create() {
    setScreen(mainGameScreen)
  }

  override def dispose() {
    mainGameScreen.dispose()
  }
}
