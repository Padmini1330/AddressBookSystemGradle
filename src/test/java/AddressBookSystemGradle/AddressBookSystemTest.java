package AddressBookSystemGradle;

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
		int AddressBookSize=addressBookImpl.readDb(101);
		int addressSize=AddressBookSize;
		System.out.println(AddressBookSize);
		Assert.assertEquals(addressSize,AddressBookSize);
	}
	
	@Test
	public void givenAddressBookIDAndContactDetails_CheckIFContactISInsertedIntoTable()
	{
		int AddressBookSize=addressBookImpl.readDb(103);
		Assert.assertEquals(0,AddressBookSize);
		System.out.println(AddressBookSize);
		Contact contact= new Contact(4,103, 23, "Louis", "Litt","bangalore", "Karnataka","57", "875454" ,"louis@gmail.com");
		addressBookImpl.writeAddressBookDB(contact,103);
		int updatedSize=addressBookImpl.readDb(103);
		System.out.println(updatedSize);
		Assert.assertEquals(AddressBookSize+1,updatedSize);
		
	}
}
