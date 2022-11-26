# SpringBoot app for QR|Barcode generation with ZXing library

### Guides
Features:

1. Generate barcode (*.png)
* UPC-A / GET: [http://localhost:8080/barcodes/zx/upca/12345678901](http://localhost:8080/barcodes/zx/upca/12345678901)
* EAN13 / GET: [http://localhost:8080/barcodes/zx/ean13/761070000477](http://localhost:8080/barcodes/zx/ean13/761070000477)
* CODE128 / POST, send data in request body: http://localhost:8080/barcodes/zx/code128
* PDF417 / POST, send data in request body: http://localhost:8080/barcodes/zx/pdf417
2. Read a barcode 
* POST, send image as multipart data with name **file** to: http://localhost:8080/barcodes/zx/read
3. Generate qr with / without logo (*.png)
* send a POST request to [http://localhost:8080/barcodes/zx/qr/custom](http://localhost:8080/barcodes/zx/qr/custom) with params / body:
  * data - text to encode (request param)
  * width - image width, optional, default is 256 (request param)
  * height - image height, optional, default is 256 (request param)
  * color - qr color, default is black (request param)
  * file - optional, image file with logo (multipart file request param)

![# QR Example](screens/screen1.png)

4. Generate vcard / qr (*.txt)
* GET, generate a demo json representing a contact: http://localhost:8080/vcard/demo/contact
* GET, generate a demo VCARD format for same demo contact: http://localhost:8080/vcard/demo/card
5. Generate ics format (just a simple demo)
* GET, generate an ics demo sample - http://localhost:8080/ical

### Based on
* [QRCodeBuilder](https://github.com/skrymer/qrbuilder) project
* Generating Barcodes and QR Codes in Java tutorial from [baeldung](https://www.baeldung.com/java-generating-barcodes-qr-codes)


Hint: make gradlew executable: `git update-index --chmod=+x gradlew`