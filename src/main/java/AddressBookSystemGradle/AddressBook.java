package AddressBookSystemGradle;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

public class AddressBook 
{
	private String name;
	public HashMap<String, Contact> addressBook;

	public AddressBook(String name) 
	{
		this.name = name;
		addressBook = new HashMap<String, Contact>();
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public ArrayList<Contact> getContact() 
	{
		return new ArrayList<Contact>(addressBook.values());
	}

	public Map<String, Contact> getAddressBook() 
	{
		return addressBook;
	}

}
	