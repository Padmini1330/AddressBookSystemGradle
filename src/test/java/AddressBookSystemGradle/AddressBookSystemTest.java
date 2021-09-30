package AddressBookSystemGradle;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import AddressBookSystemGradle.AddContactDetails.IOService;


public class AddressBookSystemTest 
{
	AddContactDetails contactDetails;
	
	@Before
	public void setUp() throws Exception
	{
		contactDetails=new AddContactDetails();
	}
	
	@Test
	public void givenAddressBookID_CheckIfAllContactsAreFetched_WithAddressBookSize()
	{
		List<Contact> contacts=contactDetails.readDb(101,IOService.DB_IO);
		System.out.println(contacts);
		Assert.assertEquals(4, contacts.size());
	}
	
	@Test
	public void givenAddressBookIDAndContactDetails_CheckIFContactIsInsertedIntoTable()
	{
		Contact contact= new Contact(102,"alex", "john",30, "875474",11,"a@gmail.com",LocalDate.now(),"mysore","karnataka","112");
		System.out.println(contact);
		Contact insertedContact = contactDetails.writeAddressBookDB(contact, 102,IOService.DB_IO);
		System.out.println("inserted contact"+insertedContact);
		Boolean result = contactDetails.compareContactSync(insertedContact, 102,IOService.DB_IO);
		Assert.assertTrue(result);
		
	}
	
	@Test
	public void givenDateRange_WhenCorrect_RetrieveAllContactsAdded() 
	{
		LocalDate startDate = LocalDate.of(2021, 4, 19);
		LocalDate endDate = LocalDate.of(2021, 8, 19);
		List<Contact> contacts = contactDetails.readContactsAddedInRange(Date.valueOf(startDate), Date.valueOf(endDate),IOService.DB_IO);
		System.out.println(contacts.size());
	}
	
	
	@Test
	public void givenCityAndState_WhenCorrect_RetrieveAllContactsInCity() 
	{
		String city = "Bangalore";
		List<Contact> contactsInCity = contactDetails.readContactsAddedInGivenCity(city,IOService.DB_IO);
		Assert.assertEquals(1, contactsInCity.size());
	}
	
	@Test
	public void givenCityAndState_WhenCorrect_RetrieveAllContactsInState() 
	{
		String state = "karnataka";
		List<Contact> contactsInState = contactDetails.readContactsAddedInGivenState(state,IOService.DB_IO);
		Assert.assertEquals(4, contactsInState.size());
	}
}
