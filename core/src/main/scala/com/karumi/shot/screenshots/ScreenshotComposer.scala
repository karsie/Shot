package com.karumi.shot.screenshots

import java.io.File

import com.karumi.shot.domain.Screenshot
import com.sksamuel.scrimage.{AwtImage, Color, Image}

object ScreenshotComposer {

  private val tileSize = 512

  private[screenshots] def composeNewScreenshot(screenshot: Screenshot): Image = {
    val width  = screenshot.screenshotDimension.width
    val height = screenshot.screenshotDimension.height
    if (screenshot.recordedPartsPaths.size == 1) {
      Image.fromFile(new File(screenshot.recordedPartsPaths.head))
    } else {
      var composedImage = Image.filled(width, height, Color.Transparent)
      var partIndex     = 0
      for (
        x <- 0 until screenshot.tilesDimension.width;
        y <- 0 until screenshot.tilesDimension.height
      ) {
        val partFile  = new File(screenshot.recordedPartsPaths(partIndex))
        val part      = Image.fromFile(partFile).awt
        val xPosition = x * tileSize
        val yPosition = y * tileSize
        val newComposedImage = composedImage.overlay(new AwtImage(part), xPosition, yPosition)
        composedImage.awt.flush()
        part.flush()
        composedImage = newComposedImage
        partIndex += 1
      }
      composedImage
    }
  }
}
