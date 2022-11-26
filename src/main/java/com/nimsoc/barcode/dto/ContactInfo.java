package com.nimsoc.barcode.dto;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.property.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.TimeZone;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactInfo {

  private String lastName;
  private String firstName;
  private String additionalName;
  private String namePrefix;
  private String nameSuffix;
  private String formattedName;
  private Address homeAddress;
  private Address workAddress;
  private LocalDate birthDay;
  private String email;
  private String language;
  private String organization;
  private String role;
  private Telephone cellPhone;
  private Telephone voicePhone;
  private String title;
  private TimeZone timeZone;
  private String url;

  public String encode() {
    VCard vcard = new VCard();
    StructuredName sn = new StructuredName();
    sn.setFamily(this.lastName);
    sn.setGiven(this.firstName);
    sn.getAdditionalNames().add(this.additionalName);
    sn.getPrefixes().add(this.namePrefix);
    sn.getSuffixes().add(this.nameSuffix);
    vcard.setStructuredName(sn);
    vcard.setFormattedName(this.formattedName);

    ezvcard.property.Address hAddress = new ezvcard.property.Address();
    hAddress.setExtendedAddress(this.homeAddress.getExtendedAddress());
    hAddress.setPoBox(this.homeAddress.getPostOfficeAddress());
    hAddress.setStreetAddress(this.homeAddress.getStreet());
    hAddress.setCountry(this.homeAddress.getCountry());
    hAddress.setRegion(this.homeAddress.getRegion());
    hAddress.setPostalCode(this.homeAddress.getPostalCode());
    hAddress.setLocality(this.homeAddress.getLocality());
    vcard.getAddresses().add(hAddress);

    Birthday contactInfoBirthday = new Birthday(java.util.Date.from(this.birthDay.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant()));
    vcard.setBirthday(contactInfoBirthday);

    vcard.getEmails().add(new Email(this.getEmail()));
    vcard.getLanguages().add(new Language(this.getLanguage()));
    vcard.getRoles().add(new Role(this.getRole()));
    if (this.cellPhone != null) {
      vcard.getTelephoneNumbers().add(new ezvcard.property.Telephone(this.cellPhone.getNumber()));
    }
    if (this.voicePhone != null) {
      vcard.getTelephoneNumbers().add(new ezvcard.property.Telephone(this.voicePhone.getNumber()));
    }
    vcard.getTitles().add(new Title(this.title));
    vcard.setTimezone(new Timezone(this.timeZone));
    vcard.getUrls().add(new Url(this.url));

    return Ezvcard.write(vcard).version(VCardVersion.V3_0).go();
  }


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Address {
    private AddressType type;

    private String postOfficeAddress;
    private String extendedAddress;
    private String street;
    private String locality;
    private String region;
    private String postalCode;
    private String country;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Telephone {
    private TelephoneType type;
    private String number;
  }

  public enum AddressType {
    dom,
    intl,
    postal,
    parcel,
    home,
    work
  }

  public enum TelephoneType {
    PREF,
    WORK,
    HOME,
    VOICE,
    FAX,
    MSG,
    CELL,
    PAGER,
    BBS,
    MODEM,
    CAR,
    ISDN,
    VIDEO
  }


}



