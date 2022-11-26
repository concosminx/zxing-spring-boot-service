package com.nimsoc.barcode.generators.decorator;

public interface Decorator<T> {

  T decorate(T qrcode);
}
