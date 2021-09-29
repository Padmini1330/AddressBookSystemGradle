package AddressBookSystemGradle;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddressBookSystemTest 
{
	AddContactDetails addressBookImpl;
	
	@Before
	public void setUp() throws Exception
	{
		addressBookImpl=new AddContactDetails();
	}
	
	@Test
	public void givenAddressBookID_CheckIfAllContactsAreFetched_WithAddressBookSize()
	{
		List<Contact> contacts=addressBookImpl.readDb(101);
		Assert.assertEquals(3, contacts.size());
	}
	
	@Test
	public void givenAddressBookIDAndContactDetails_CheckIFContactISInsertedIntoTable()
	{
		Contact contact= new Contact(4,103, 23, "Louis", "Litt","bangalore", "Karnataka","57", "875454" ,"louis@gmail.com");
		Contact updatedContact = addressBookImpl.writeAddressBookDB(contact, 103);
		List<Contact> employeeList = addressBookImpl.readDb(103);
		Boolean result = addressBookImpl.compareContactSync(updatedContact, 103);
		Assert.assertTrue(result);
		
	}
	
	@Test
	public void givenDateRange_WhenCorrect_RetrieveAllContactsAdded() 
	{
		LocalDate startDate = LocalDate.of(2021, 4, 19);
		LocalDate endDate = LocalDate.of(2021, 8, 19);
		List<Contact> contacts = addressBookImpl.readContactsAddedInRange(Date.valueOf(startDate), Date.valueOf(endDate));
		System.out.println(contacts.size());
	}
	
}
