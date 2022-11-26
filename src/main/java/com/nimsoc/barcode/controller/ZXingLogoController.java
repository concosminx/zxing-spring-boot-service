package com.nimsoc.barcode.controller;

import com.nimsoc.barcode.generators.ZXingWithLogo;
import com.nimsoc.barcode.generators.decorator.Decorator;
import com.nimsoc.barcode.generators.decorator.impl.ColoredQRCode;
import com.nimsoc.barcode.generators.decorator.impl.ImageOverlay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nimsoc.barcode.generators.ZXingUtil.readImage;

@RestController
@RequestMapping("/barcodes")
@Slf4j
public class ZXingLogoController {

  public static final float TRANSPARENCY = 1f;
  public static final float OVERLAY_RATIO = 0.15f;

  public static final Map<String, Color> COLOR_CACHE = new HashMap<>();

  @PostConstruct
  public void init() {
    COLOR_CACHE.put("blue", Color.blue);
    COLOR_CACHE.put("green", Color.green.darker());
    COLOR_CACHE.put("orange", Color.orange.darker());
    COLOR_CACHE.put("magenta", Color.magenta.darker());
    COLOR_CACHE.put("pink", Color.pink.darker());
    COLOR_CACHE.put("yellow", Color.yellow.darker());
    COLOR_CACHE.put("gray", Color.gray.darker());
    COLOR_CACHE.put("red", Color.red.darker());
  }

  //http://localhost:8080/barcodes/zx/qr/custom?data=stufftoencode&color=red&width=300&height=300
  @PostMapping(value = "/zx/qr/custom", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<BufferedImage> generate(
      @RequestParam(value = "file", required = false) MultipartFile file,
      @RequestParam("data") String data,
      @RequestParam(value = "width", required = false, defaultValue = "256") Integer width,
      @RequestParam(value = "height", required = false, defaultValue = "256") Integer height,
      @RequestParam(value = "color", required = false) String color
  ) throws Exception {
    List<Decorator<BufferedImage>> decorators = new ArrayList<>();

    //add color decorator if the color exists in cache
    if (color != null &&
        COLOR_CACHE.containsKey(color.toLowerCase())
    ) {
      decorators.add(ColoredQRCode.colorizeQRCode(COLOR_CACHE.get(color.toLowerCase())));
    }

    //add image overlay decorator only if the file parameter is not null
    if (file != null) {
      decorators.add(ImageOverlay.addImageOverlay(readImage(file.getInputStream()), TRANSPARENCY, OVERLAY_RATIO));
    }

    return ResponseEntity.ok(
        ZXingWithLogo
            .builder()
            .width(width)
            .height(height)
            .decorators(decorators)
            .charSet(Charset.forName("UTF-8"))
            .data(data)
            .verify(true)
            .build().toImage());
  }


}
