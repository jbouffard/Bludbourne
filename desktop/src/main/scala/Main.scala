package com.jbouffard.bludbourne

import com.badlogic.gdx.backends.lwjgl._
import com.badlogic.gdx.{Appliation, GDX}

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "BludBourne"
    cfg.height = 480
    cfg.width = 800
    cfg.forceExit = false
    val app: Application = new LwjglApplication(new Bludbourne, cfg)
    app
}
