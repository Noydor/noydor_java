package ru.stqa.noy.addressbook.tests;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.noy.addressbook.model.AddNewData;
import ru.stqa.noy.addressbook.model.Contacts;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class ContactModificationTests extends TestBase{

  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().contacts().size() == 0) {
      app.goTo().homePage();
      app.contact().create(new AddNewData().withFirstname("Samuel").withLastname("Smith").withNickname("Smithy").withTitle("Mr").withCompany("Seastar").withAddress("Berlin, Niederkirchnerstrasse, 18").withHome("4955123456").withMobile("4976543210").withEmail("samuelsmith@gmail.com").withHomepage("samuelsmith.com"), true);
    }
  }

  @Test
  public void testContactModification() {
    Contacts before = app.db().contacts();
    AddNewData modifiedContact = before.iterator().next();
    app.contact().initContactModificationById(modifiedContact.getId());
    AddNewData contact = new AddNewData().withId(modifiedContact.getId()).withFirstname("Samuel").withLastname("Smith").withNickname("Smithy").withTitle("Mr").withCompany("Seastar").withAddress("Berlin, Niederkirchnerstrasse, 18").withHome("4955123456").withMobile("4976543210").withWork("123456").withEmail("samuelsmith@gmail.com").withHomepage("samuelsmith.com");
    app.goTo().homePage();
    app.contact().modify(contact);
    Contacts after = app.db().contacts();
    assertEquals(after.size(), before.size());
    assertThat(after, equalTo(before.without(modifiedContact).withAdded(contact)));
    verifyContactListInUI();
  }
}
