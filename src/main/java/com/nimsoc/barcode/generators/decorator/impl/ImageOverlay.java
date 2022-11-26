package com.nimsoc.barcode.generators.decorator.impl;


import com.nimsoc.barcode.generators.decorator.Decorator;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageOverlay implements Decorator<BufferedImage> {
  public static final Float DEFAULT_OVERLAY_TRANSPARENCY = 1f;
  public static final Float DEFAULT_OVERLAY_TO_QRCODE_RATIO = 0.25f;

  private BufferedImage overlay;
  private Float overlayToQRCodeRatio, overlayTransparency;

  public static Decorator<BufferedImage> addImageOverlay(BufferedImage overlay, Float overlayTransparency, Float overlayToQRCodeRatio) {
    return new ImageOverlay(overlay, overlayTransparency, overlayToQRCodeRatio);
  }

  private ImageOverlay(BufferedImage overlay, Float overlayTransparency, Float overlayToQRCodeRatio) {
    if (overlay == null) {
      throw new IllegalArgumentException("Overlay is required");
    }

    this.overlay = overlay;
    this.overlayTransparency = overlayTransparency == null ? DEFAULT_OVERLAY_TRANSPARENCY : overlayTransparency;
    this.overlayToQRCodeRatio = overlayToQRCodeRatio == null ? DEFAULT_OVERLAY_TO_QRCODE_RATIO : overlayToQRCodeRatio;
  }

  public BufferedImage decorate(BufferedImage qrcode) {
    BufferedImage scaledOverlay = scaleOverlay(qrcode);

    Integer deltaHeight = qrcode.getHeight() - scaledOverlay.getHeight();
    Integer deltaWidth = qrcode.getWidth() - scaledOverlay.getWidth();

    BufferedImage combined = new BufferedImage(qrcode.getWidth(), qrcode.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D) combined.getGraphics();
    g2.drawImage(qrcode, 0, 0, null);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, overlayTransparency));
    g2.drawImage(scaledOverlay, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), null);

    return combined;
  }

  private BufferedImage scaleOverlay(BufferedImage qrcode) {
    Integer scaledWidth = Math.round(qrcode.getWidth() * overlayToQRCodeRatio);
    Integer scaledHeight = Math.round(qrcode.getHeight() * overlayToQRCodeRatio);

    BufferedImage imageBuff = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics g = imageBuff.createGraphics();
    g.drawImage(overlay.getScaledInstance(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH), 0, 0, new Color(0, 0, 0), null);
    g.dispose();

    return imageBuff;
  }
}
