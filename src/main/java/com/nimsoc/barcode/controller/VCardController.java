package com.nimsoc.barcode.controller;

import com.google.zxing.WriterException;
import com.nimsoc.barcode.dto.ContactInfo;
import com.nimsoc.barcode.generators.ZXingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.TimeZone;

@RestController
@RequestMapping("/vcard")
@Slf4j
public class VCardController {

  @GetMapping("/demo/contact")
  public ContactInfo demo() {
    ContactInfo ci = ContactInfo.builder()
        .lastName("Doe")
        .firstName("John")
        .additionalName("Jimmy")
        .namePrefix("Mr.")
        .nameSuffix(null)
        .formattedName(null)
        .homeAddress(ContactInfo.Address.builder()
            .type(ContactInfo.AddressType.home)
            .country("Romania")
            .locality("Bucuresti")
            .street("Victoriei")
            .postalCode("119921")
            .region(null)
            .extendedAddress("Nr. 67")
            .build())
        .workAddress(null)
        .birthDay(LocalDate.of(1985, 11, 9))
        .email("johndoe@myowndomain.com")
        .language("ro-RO")
        .organization("SUPER Company")
        .role("Architect")
        .cellPhone(ContactInfo.Telephone.builder()
            .type(ContactInfo.TelephoneType.CELL)
            .number("+40 0740 012 345")
            .build())
        .voicePhone(null)
        .title("Software Architect")
        .timeZone(TimeZone.getDefault())
        .url("http://supercompany.com")
        .build();

    return ci;
  }

  @GetMapping("/demo/card")
  public String cardDemo() {
    return demo().encode();
  }

  @GetMapping(value = "/demo/qr", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> generateByteStream() throws IOException, WriterException {
    return ResponseEntity.ok(ZXingUtil.getQRCodeImage(demo().encode(), 300, 300));
  }

  @PostMapping(produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> createQr(@RequestBody ContactInfo ci) throws IOException, WriterException {
    log.info("Created entity: {}", ci);

    return ResponseEntity.ok(ZXingUtil.getQRCodeImage(ci.encode(), 300, 300));
  }

}
