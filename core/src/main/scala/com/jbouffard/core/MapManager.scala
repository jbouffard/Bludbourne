package com.jbouffard.bludbourne

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.{MapLayer, MapObject}
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math._

import scala.collection.JavaConverters._


class MapManager {
  import MapManager._

  final val TAG: String = classOf[MapManager].getName()

  private var playerStart = new Vector2(0, 0)
  private var playerStartPosition: Vector2 = new Vector2(0, 0)
  private var playerStartPositionRec = new Vector2(0, 0)
  private var closestPlayerStartPosition: Vector2 = new Vector2(0, 0)
  private var convertedUnits: Vector2 = new Vector2(0, 0)

  private val playerStartLocationTable: Map[String, Vector2] =
    Map(
      TOP_WORLD -> playerStart.cpy(),
      TOWN -> playerStart.cpy(),
      CASTLE_OF_DOOM -> playerStart.cpy()
    )

  private var currentMap: Option[TiledMap] = None

  private var currentMapName: Option[String] = None

  private var collisionLayer: Option[MapLayer] = None
  private var portalLayer: Option[MapLayer] = None
  private var spawnsLayer: Option[MapLayer] = None

  def loadMap(mapName: Option[String]): Unit =
    mapName match {
      case Some(name) => loadMap(name)
      case None => Gdx.app.debug(TAG, "No map name was given!")
    }

  def loadMap(mapName: String): Unit = {
    playerStart.set(0, 0)

    mapTable.get(mapName) match {
      case None =>
        Gdx.app.debug(TAG, s"This map was not found in the mapTable: $mapName")
      case Some(name) if (name.isEmpty) =>
        Gdx.app.debug(TAG, s"This map was not found in the mapTable: $mapName")
      case Some(name) =>
        Utility.loadMapAsset(name)

        var (currentMap, currentMapName) =
          if (Utility.assetLoaded(name))
            (Some(Utility.getMapAsset(name)), Some(mapName))
          else {
            Gdx.app.debug(TAG, s"This map was not found in the mapTable: $name")
            (None, None)
          }

        collisionLayer =
          currentMap.get.getLayers().get(MAP_COLLISION_LAYER) match {
            case null =>
              Gdx.app.debug(TAG, s"Could not find COLLISION_LAYER for map: $name")
              None
            case result: MapLayer => Some(result)
          }

        portalLayer =
          currentMap.get.getLayers().get(MAP_PORTAL_LAYER) match {
            case null =>
              Gdx.app.debug(TAG, s"Could not find PORTAL_LAYER for map: $name")
              None
            case result: MapLayer => Some(result)
          }

        spawnsLayer =
          currentMap.get.getLayers().get(MAP_SPAWNS_LAYER) match {
            case null =>
              Gdx.app.debug(TAG, s"Could not find SPAWNS_LAYER for map: $name")
              None
            case result: MapLayer => Some(result)
          }

        playerStartLocationTable.get(currentMapName.get) match {
          case None => Gdx.app.debug(TAG, s"Could not get playerStartLocation from Table in Map: $currentMapName")
          case Some(start) =>
            if (start.isZero()) {
              setClosestStartPosition(playerStart)
              val newStart = playerStartLocationTable.get(currentMapName.get).get

              playerStart.set(newStart.x, newStart.y)
            } else
              playerStart.set(start.x, start.y)
        }
    }

    Gdx.app.debug(TAG, s"PlayerSatart: ${playerStart.x}, ${playerStart.y}")
  }

  def getCurrentMap(): Option[TiledMap] =
    currentMap match {
      case None =>
        currentMapName = Some(TOWN)
        Some(loadMap(currentMapName))
        currentMap
      case _ => currentMap
    }

  def getCollisionLayer(): Option[MapLayer] = collisionLayer

  def getPortalLayer(): Option[MapLayer] = portalLayer

  def getPlayerStartUnitScaled(): Vector2 = {
    val playerStartCpy: Vector2 = playerStart.cpy()

    playerStartCpy.set(playerStart.x * UNIT_SCALE, playerStart.y * UNIT_SCALE)

    playerStartCpy
  }

  def setClosestStartPosition(position: Vector2): Unit = {
    playerStartPositionRec.set(0, 0)
    closestPlayerStartPosition.set(0, 0)
    val shortestDistance: Float = 0f

    spawnsLayer match {
      case None => {}
      case Some(layer) =>
        layer.getObjects.iterator().asScala.map { obj =>
          obj.getName().toUpperCase match {
            case PLAYER_START =>
              obj
                .asInstanceOf[RectangleMapObject]
                .getRectangle()
                .getPosition(playerStartPositionRec)

              val distance: Float = position.dst2(playerStartPositionRec)

              closestPlayerStartPosition.set(playerStartPositionRec)

              playerStartLocationTable ++ Map(currentMapName.get -> closestPlayerStartPosition.cpy())
          }
        }
    }
  }

  def setClosestStartPositionFromScaledUnits(position: Vector2): Unit =
    if (UNIT_SCALE > 0) {
      convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE)
      setClosestStartPosition(convertedUnits)
    }
}

object MapManager {
  final val TOP_WORLD: String = "TOP_WORLD"
  final val TOWN: String = "TOWN"
  final val CASTLE_OF_DOOM: String = "CASTLE_OF_DOOM"

  final val MAP_COLLISION_LAYER: String = "MAP_COLLISION_LAYER"
  final val MAP_SPAWNS_LAYER: String = "MAP_SPAWNS_LAYER"
  final val MAP_PORTAL_LAYER: String = "MAP_PORTAL_LAYER"
  final val layers: List[String] =
    List(
      MAP_COLLISION_LAYER,
      MAP_SPAWNS_LAYER,
      MAP_PORTAL_LAYER
    )

  final val PLAYER_START: String = "PLAYER_START"

  final val mapTable: Map[String, String] =
    Map(
      TOP_WORLD -> "maps/topworld.tmx",
      TOWN -> "maps/town.tmx",
      CASTLE_OF_DOOM -> "maps/town.tmx"
    )

  final val UNIT_SCALE: Float = 1/16f
}
