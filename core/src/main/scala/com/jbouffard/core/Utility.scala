package com.jbouffard.bludbourne

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.maps.tiled.{TiledMap, TmxMapLoader}
import com.badlogic.gdx.assets.AssetManager


class Utility {
  final val assetManager: AssetManager = new AssetManager()
  final val TAG: String = classOf[Utility].getName()
  val pathResolver: InternalFileHandleResolver = new InternalFileHandleResolver()

  def unloadAsset(assetPath: String): Unit =
    if (assetManager.isLoaded(assetPath))
      assetManager.unload(assetPath)
    else
      Gdx.app.debug(TAG, s"Asset is not loaded; Nothing to unload: $assetPath")

  // This can be used to update progess meter values when loading async
  def loadComplete(): Float = assetManager.getProgress()

  def numberAssetsQueued(): Int = assetManager.getQueuedAssets()

  // Can be called in the render loop if loading assets async
  def updateAssetLoading(): Boolean = assetManager.update()

  def assetLoaded(assetPath: String): Boolean =
    assetManager.isLoaded(assetPath)

  def isFile(assetPath: String): Boolean =
    pathResolver.resolve(assetPath).exists()

  def loadMapAsset(assetPath: String): Unit =
    if (isFile(assetPath)) {
      assetManager.setLoader(classOf[TiledMap], new TmxMapLoader(pathResolver))

      assetManager.load(assetPath, classOf[TiledMap])

      assetManager.finishLoadingAsset(assetPath)
      Gdx.app.debug(TAG, s"Map loaded: $assetPath")
    } else
      Gdx.app.debug(TAG, s"Map could NOT be loaded: $assetPath")

  def getMapAsset(assetPath: String): TiledMap =
    if (assetLoaded(assetPath))
      assetManager.get(assetPath, classOf[TiledMap])
    else {
      Gdx.app.debug(TAG, s"Map could NOT be gotten: $assetPath")
      null
    }

  def loadTextureAsset(assetPath: String): Unit =
    if (isFile(assetPath)) {
      assetManager.setLoader(classOf[Texture], new TextureLoader(pathResolver))

      assetManager.load(assetPath, classOf[Texture])

      assetManager.finishLoadingAsset(assetPath)
      Gdx.app.debug(TAG, s"Texture loaded: $assetPath")
    } else
      Gdx.app.debug(TAG, s"Texture could NOT be loaded: $assetPath")

  def getTextureAsset(assetPath: String): Texture =
    if (assetLoaded(assetPath))
      assetManager.get(assetPath, classOf[Texture])
    else {
      Gdx.app.debug(TAG, s"Texture could NOT be gotten: $assetPath")
      null
    }
}
