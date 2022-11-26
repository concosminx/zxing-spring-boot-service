package com.nimsoc.barcode.generators;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.nimsoc.barcode.dto.CodeResponse;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ZXingUtil {

  public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
  }

  public static byte[] getQRCodeImageWithText(String text) throws WriterException, IOException {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);

    final BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    Graphics g = image.getGraphics();
    g.setFont(g.getFont().deriveFont(18f));
    Color textColor = Color.BLACK;
    g.setColor(textColor);
    g.drawString(text, 15, 300);
    g.dispose();

    ByteArrayOutputStream fin = new ByteArrayOutputStream();
    ImageIO.write(image, "png", fin);
    return fin.toByteArray();
  }

  public static BufferedImage generateUPCABarcodeImage(String barcodeText) throws Exception {
    UPCAWriter barcodeWriter = new UPCAWriter();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.UPC_A, 300, 100);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
    EAN13Writer barcodeWriter = new EAN13Writer();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, 300, 75);
    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static BufferedImage generateCode128BarcodeImage(String barcodeText) throws Exception {
    Code128Writer barcodeWriter = new Code128Writer();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 300, 100);

    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static BufferedImage generatePDF417BarcodeImage(String barcodeText) throws Exception {
    PDF417Writer barcodeWriter = new PDF417Writer();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.PDF_417, 500, 500);

    return MatrixToImageWriter.toBufferedImage(bitMatrix);
  }

  public static CodeResponse readCode(InputStream is) {
    CodeResponse cr = null;
    try {
      BufferedImage bufferedImg = ImageIO.read(is);
      LuminanceSource source = new BufferedImageLuminanceSource(bufferedImg);
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      Result result = new MultiFormatReader().decode(bitmap);
      cr = new CodeResponse(result.getBarcodeFormat().toString(), result.getText());
    } catch (NotFoundException | IOException e) {
      log.error("Error on code reading ", e);
    }
    return cr != null ? cr : new CodeResponse();
  }

  public static BufferedImage readImage(InputStream is) {
    try {
      return ImageIO.read(is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
