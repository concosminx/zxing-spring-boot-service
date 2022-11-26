package com.nimsoc.barcode.generators;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nimsoc.barcode.generators.decorator.Decorator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZXingWithLogo {
  private String data;
  private boolean verify;
  private int width, height;
  private List<Decorator<BufferedImage>> decorators;
  private Charset charSet;


  public BufferedImage toImage() throws WriterException {
    BufferedImage qrcode = decorate(encode());
    verifyQRCode(qrcode);
    return qrcode;
  }

  private void verifyQRCode(BufferedImage qrCode) {
    if (!verify) {
      return;
    }
    try {
      Result actualData = decode(qrCode);
      if (actualData != null && !actualData.getText().equals(this.data)) {
        throw new RuntimeException("The data contained in the qrCode is not as expected: " + this.data + " actual: " + actualData);
      }
    } catch (Exception e) {
      throw new RuntimeException("Verifying qr code failed!", e);
    }
  }

  private Result decode(BufferedImage qrcode) throws FormatException, ChecksumException, NotFoundException {
    return new QRCodeReader().decode(new BinaryBitmap(
            new HybridBinarizer(
                new BufferedImageLuminanceSource(qrcode))
        ), getDecodeHints()
    );
  }

  private BufferedImage encode() throws WriterException {
    BitMatrix matrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, this.width, this.height, getEncodeHints());
    return MatrixToImageWriter.toBufferedImage(matrix);
  }

  private Map<EncodeHintType, Object> getEncodeHints() {
    Map<EncodeHintType, Object> encodeHints = new HashMap<>();
    encodeHints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
    encodeHints.put(EncodeHintType.CHARACTER_SET, this.charSet.name());
    return encodeHints;
  }

  private Map<DecodeHintType, Object> getDecodeHints() {
    Map<DecodeHintType, Object> decodeHints = new HashMap<>();
    decodeHints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
    decodeHints.put(DecodeHintType.CHARACTER_SET, this.charSet.name());
    return decodeHints;
  }

  private BufferedImage decorate(BufferedImage qrCodeImage) {
    for (Decorator<BufferedImage> decorator : decorators) {
      qrCodeImage = decorator.decorate(qrCodeImage);
    }
    return qrCodeImage;
  }


}