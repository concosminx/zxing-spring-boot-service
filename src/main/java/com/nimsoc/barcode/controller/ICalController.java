package com.nimsoc.barcode.controller;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.RandomUidGenerator;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.GregorianCalendar;

@RestController
@RequestMapping("/ical")
public class ICalController {


  @GetMapping(produces = "text/calendar")
  public ResponseEntity<byte[]> demo() throws IOException {
    Calendar cal = new Calendar();
    cal.getProperties().add(new ProdId("-//C.I.C.//iCal4j 3.2.7//EN"));
    cal.getProperties().add(Version.VERSION_2_0);
    cal.getProperties().add(CalScale.GREGORIAN);

    java.util.Calendar calendar = java.util.Calendar.getInstance();
    calendar.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
    calendar.set(java.util.Calendar.DAY_OF_MONTH, 25);

    // initialise as an all-day event..
    VEvent christmas = new VEvent(new Date(calendar.getTime()), "Christmas Day");

    // Generate a UID for the event..
    UidGenerator ug = new RandomUidGenerator();
    christmas.getProperties().add(ug.generateUid());

    cal.getComponents().add(christmas);


    // Create a TimeZone
    TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    TimeZone timezone = registry.getTimeZone("Europe/Bucharest");
    VTimeZone tz = timezone.getVTimeZone();

    // Start Date is on: April 1, 2008, 9:00 am
    java.util.Calendar startDate = new GregorianCalendar();
    startDate.setTimeZone(timezone);
    startDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
    startDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
    startDate.set(java.util.Calendar.YEAR, 2008);
    startDate.set(java.util.Calendar.HOUR_OF_DAY, 9);
    startDate.set(java.util.Calendar.MINUTE, 0);
    startDate.set(java.util.Calendar.SECOND, 0);

    // End Date is on: April 1, 2008, 13:00
    java.util.Calendar endDate = new GregorianCalendar();
    endDate.setTimeZone(timezone);
    endDate.set(java.util.Calendar.MONTH, java.util.Calendar.APRIL);
    endDate.set(java.util.Calendar.DAY_OF_MONTH, 1);
    endDate.set(java.util.Calendar.YEAR, 2008);
    endDate.set(java.util.Calendar.HOUR_OF_DAY, 13);
    endDate.set(java.util.Calendar.MINUTE, 0);
    endDate.set(java.util.Calendar.SECOND, 0);

    // Create the event
    String eventName = "Progress Meeting";
    DateTime start = new DateTime(startDate.getTime());
    DateTime end = new DateTime(endDate.getTime());
    VEvent meeting = new VEvent(start, end, eventName);

    // add timezone info..
    meeting.getProperties().add(tz.getTimeZoneId());

    // generate unique identifier..
    Uid uid = ug.generateUid();
    meeting.getProperties().add(uid);

    // add attendees..
    Attendee dev1 = new Attendee(URI.create("mailto:dev1@mycompany.com"));
    dev1.getParameters().add(Role.REQ_PARTICIPANT);
    dev1.getParameters().add(new Cn("Developer 1"));
    meeting.getProperties().add(dev1);

    Attendee dev2 = new Attendee(URI.create("mailto:dev2@mycompany.com"));
    dev2.getParameters().add(Role.OPT_PARTICIPANT);
    dev2.getParameters().add(new Cn("Developer 2"));
    meeting.getProperties().add(dev2);
    cal.getComponents().add(meeting);

    CalendarOutputter outputter = new CalendarOutputter();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    outputter.output(cal, baos);

    //Attaching binary data
    //FileInputStream fin = new FileInputStream("etc/artwork/logo.png");
    //ByteArrayOutputStream bout = new ByteArrayOutputStream();
    //for (int i = fin.read(); i >= 0;) {
    //    bout.write(i);
    //    i = fin.read();
    //}
    //
    //ParameterList params = new ParameterList();
    //params.add(Value.BINARY);
    //params.add(Encoding.BASE64);
    //
    //Attach attach = new Attach(params, bout.toByteArray());

    //https://www.ical4j.org/examples/recur/

    return ResponseEntity.ok(baos.toByteArray());
  }
}
