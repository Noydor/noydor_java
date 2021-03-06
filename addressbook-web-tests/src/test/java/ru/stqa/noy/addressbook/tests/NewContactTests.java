package ru.stqa.noy.addressbook.tests;

import com.thoughtworks.xstream.XStream;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.stqa.noy.addressbook.model.AddNewData;
import ru.stqa.noy.addressbook.model.Contacts;
import ru.stqa.noy.addressbook.model.GroupData;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewContactTests extends TestBase{

  @DataProvider
  public Iterator<Object[]> validContacts() throws IOException {
    try(BufferedReader reader = new BufferedReader(new FileReader(new File("src/test/resources/contacts.xml")))) {
      String xml = "";
      String line = reader.readLine();
      while (line != null) {
        xml += line;
        line = reader.readLine();
      }
      XStream xstream = new XStream();
      xstream.processAnnotations(AddNewData.class);
      List<AddNewData> contacts =(List<AddNewData>) xstream.fromXML(xml);
      return contacts.stream().map((c) -> new Object[] {c}).collect(Collectors.toList()).iterator();
    }
  }

  @Test(dataProvider = "validContacts")
  public void testNewContact(AddNewData contact) {
    Contacts before = app.db().contacts();
    app.contact().gotoAddNewPage();
    app.contact().create(contact, true);
    Contacts after = app.db().contacts();
    assertThat(after.size(), equalTo(before.size() + 1));
    app.goTo().homePage();
    assertThat(after, equalTo(before.withAdded(contact.withId(after.stream().mapToInt((c) -> c.getId()).max().getAsInt()))));
    verifyContactListInUI();
   }

   //@Test
   //public void testCurrentDir() {
     //File currentDir = new File(".");
     //System.out.println(currentDir.getAbsolutePath());
     //File photo = new File("src/test/resources/noydor.jpg");
     //System.out.println(photo.getAbsolutePath());
     //System.out.println(photo.exists());
   //}

}
