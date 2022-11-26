package com.nimsoc.barcode.controller;

import com.google.zxing.WriterException;
import com.nimsoc.barcode.dto.CodeResponse;
import com.nimsoc.barcode.generators.ZXingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/barcodes")
@Slf4j
public class ZXingController {

  //http://localhost:8080/barcodes/zx/qr/200/200
  //send the content as request body
  @PostMapping(value = "/zx/qr/{width}/{height}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generateByteStream(@RequestBody String textContent,
                                                   @PathVariable(value = "width") int width,
                                                   @PathVariable(value = "height") int height
  ) throws IOException, WriterException {
    ResponseEntity<byte[]> re = null;
    if (0 >= width ||
        0 >= height ||
        textContent == null) {
      re = ResponseEntity.badRequest().build();
    } else {
      re = ResponseEntity.ok(ZXingUtil.getQRCodeImage(textContent, width, height));
    }
    return re;
  }

  @PostMapping(value = "/zx/qr/withText", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generateByteStream(@RequestBody String textContent) throws IOException, WriterException {
    ResponseEntity<byte[]> re = null;
    if (textContent == null) {
      re = ResponseEntity.badRequest().build();
    } else {
      re = ResponseEntity.ok(ZXingUtil.getQRCodeImageWithText(textContent));
    }
    return re;
  }

  //http://localhost:8080/barcodes/zx/upca/12345678901
  @GetMapping(value = "/zx/upca/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> zxingUPCABarcode(@PathVariable("barcode") String barcode) throws Exception {
    return ResponseEntity.ok(ZXingUtil.generateUPCABarcodeImage(barcode));
  }

  //http://localhost:8080/barcodes/zx/ean13/761070000477
  @GetMapping(value = "/zx/ean13/{barcode}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> zxingEAN13Barcode(@PathVariable("barcode") String barcode) throws Exception {
    return ResponseEntity.ok(ZXingUtil.generateEAN13BarcodeImage(barcode));
  }

  //http://localhost:8080/barcodes/zx/code128
  //send the content as request body
  @PostMapping(value = "/zx/code128", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> zxingCode128Barcode(@RequestBody String barcode) throws Exception {
    return ResponseEntity.ok(ZXingUtil.generateCode128BarcodeImage(barcode));
  }

  //http://localhost:8080/barcodes/zx/pdf417
  //send the content as request body
  @PostMapping(value = "/zx/pdf417", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> zxingPDF417Barcode(@RequestBody String barcode) throws Exception {
    return ResponseEntity.ok(ZXingUtil.generatePDF417BarcodeImage(barcode));
  }

  //http://localhost:8080/barcodes/zx/read
  //send the content as multipart request body with part name = file
  @PostMapping(value = "/zx/read")
  private CodeResponse readQR(@RequestParam("file") MultipartFile file) throws Exception {
    return ZXingUtil.readCode(file.getInputStream());
  }
}
