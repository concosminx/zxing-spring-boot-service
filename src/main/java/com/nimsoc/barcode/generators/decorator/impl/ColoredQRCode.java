package com.nimsoc.barcode.generators.decorator.impl;

import com.nimsoc.barcode.generators.decorator.Decorator;
import com.nimsoc.barcode.generators.decorator.ImageUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;

public class ColoredQRCode implements Decorator<BufferedImage> {
  private Color color;

  public static Decorator<BufferedImage> colorizeQRCode(Color color) {
    return new ColoredQRCode(color);
  }

  private ColoredQRCode(Color color) {
    this.color = color;
  }

  public BufferedImage decorate(BufferedImage qrcode) {
    FilteredImageSource prod = new FilteredImageSource(qrcode.getSource(), new QRCodeRGBImageFilter());
    return ImageUtils.imageToBufferedImage(Toolkit.getDefaultToolkit().createImage(prod));
  }

  private class QRCodeRGBImageFilter extends RGBImageFilter {
    public int filterRGB(int x, int y, int rgb) {
      if (rgb == Color.black.getRGB()) {
        return color.getRGB();
      }
      return rgb;
    }
  }
}
