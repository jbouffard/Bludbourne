/*
package com.jbouffard.bludbourne.screen

import com.badlogic.gdx.{Gdx, Screen}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.graphics.g2d.{Sprite, TextureRegion}
import com.badlogic.gdx.maps.{MapLayer, MapObject}
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle

//import com.jbouffard.bludbourne.{Entity, MapManager, PlayerController}

class MainGameScreen extends Screen {
  final val TAG: String = getClass[ManinGameScreen].getName

  private case class ViewPort(
    viewPortWidth: Float,
    viewPortHeight: Float,
    virtualWidth: Float,
    virtualHeight: Float,
    physicalWidth: Float,
    physicalHeight: Float,
    aspectRatio: Float
  )

  private var controller: PlayerController
  private var currentPlayerFrame: TextureRegion
  private var currentPlayerSprite: Sprite

  private var mapRenderer: OthogonalTiledMapRenderer = null
  private var camera: OrthographicCamera = null
  private val mapManager: MapManager = ???
  private var player: Entity

  def MainGameScreen() {
    val mapManager = new MapManager()
  }

  @override def show(): Unit = {
    setUpViewPort(10, 10)

    camera = new OrthographicCamera()
    camera.setToOrtho(
      false,
      viewPort.viewPortWidth,
      viewPort.viewPortHeight
    )

    mapRenderer =
      new OrthogonalTiledMapRenderer(
        mapManager.getCurrentMap(),
        MapManager.UNIT_SCALE
      )

    mapRenderer.setView(camera)

    Gdx.app.debug(TAG, s"UntiScale value is: ${mapRenderer.getUnitScale()}")

    player = new Entity()

    player.init(
      MapManager.getPlayerStartUnitScaled().x,
      MapManager.getPlayerStartUnitScaled().y
    )

    currentPlayerSprite = player.getFrameSprit()

    controller = new PlayerController(player)
    Gdx.input.setInputProcessor(controller)
  }

  @override def hide(): Unit = {}

  @override def render(delta: Float): Unit = {
    Gdx.gl.glClearColor(0, 0, 0, 1)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    camera.position.set(
      currentPlayerSprite.getX(),
      currentPlayerSprite.getY(),
      0f
    )

    camera.update()
    player.update(delta)

    currentPlayerFrame = player.getFrame()

    updatePortalLayerActivation(player.boundingBox)

    if (!collidedWithMap(player.boundingBox)
      player.setNextPositionToCurrent()

    controller.update(delta)

    mapRenderer.setView(camera)
    mapRenderer.redner()

    mapRenderer.getBatch().begin()
    mapRenderer.getBatch().draw(
      currentPlayerFrame,
      currentPlayerSprite.getX(),
      currentPlayerSprite.getY(),
      1,
      1
    )
    mapRenderer.getBatch().end()
  }

  @override def resize(width: Int, height: Int): Unit = {}

  @override def pause(): Unit = {}

  @override def resume(): Unit = {}

  @override def dispose(): Unit = {
    player.dispose()
    controller.dispose()
    Gdx.input.setInputProcessor(null)
    mapRenderer.dispose()
  }

  private def setupViewPort(width: Int, height: Int): Unit = {
    val aspectRatio = width / height

    val physicalWidth = Gdx.graphics.getWidth()
    val physicalHeight = Gdx.graphics.getHeight()

    val (viewPortWidth, viewPortHeight) =
      if (physicalWidth / physicalHeight >= aspectRatio) {
        val portWidth = height * (physicalWidth / physicalHeight)

        (portWidth, height)
      } else {
        val portHeight = width * (physicalHeight / physicalWidth)

        (width, portHeight)
      }

    Gdx.app.debug(TAG, s"WorldRenderer: virtual: width: ${width} height: ${height}")
    Gdx.app.debug(TAG, s"WorldRenderer: viewPort: viewPortWidth: ${viewPortWidth} viewPortHeight: ${viewPortHeight}")
    Gdx.app.debug(TAG, s"WorldRenderer: physical: physicalWidth: ${physicalWidth} physicalHeight: ${physicalHeight}")

    ViewPort(
      viewPortWidth,
      viewPortHeight,
      width,
      height,
      physicalWidth,
      physcialHeight,
      aspectRatio
    )
  }

  private def collidedWithMap(boundingBox: Rectangle): Boolean =
    mapManager.getCollisionLayer() match {
      case null => false
      case layer: MapLayer =>
        val objects: MapObjects = layer.getObjects()

        // gotta avoid that empty.reduceLeft
        if (objects.isEmpty)
          false
        else
          objects.map { obj =>
            obj match {
              case rectangle: Rectangle if boundingBox.overlaps(rectangle) => true
              case _ => false
            }
          }.reduce { _ || _ }
    }

  private def updatePortalLayerActivation(boundingBox: Rectangle): Unit =
    mapManager.getPortalLayer() match {
      case null => {}
      case layer: MapLayer =>
        val objects: MapObjects = layer.getObjects()

        if (objects.isEmpty)
          {}
        else
          objects.map { obj =>
            obj.getName() match {
              case null => _
              case name: Strng =>
                mapManager.setClosestStartPositionFromScaledUnits(plyaer.getCurrentPosition())
                mapManager.loadMap(name)
                player.init(
                  mapManager.getPlayerStartUnitScaled().x,
                  mapManager.getPlayerStartUnitScaled().y
                )

                mapRenderer.setMap(mapManager.getCurrentMap())

                Gdx.app.debug(TAG, s"Portal Activated to: $name")
            }
          }
    }
}
*/
