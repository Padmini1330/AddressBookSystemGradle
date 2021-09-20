package AddressBookSystemGradle;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class MultipleAddressBook 
{
	private AddressBook addressBook;
	private Map<String, AddressBook> addressBooksArray;
	String addressBookName;
	Scanner scanner = new Scanner(System.in);
	
	public MultipleAddressBook() 
	{
		addressBooksArray = new HashMap<String, AddressBook>();
	}
	public String getAddressBookName() 
	{
		return this.addressBookName;
	}
	
	public void setAddressBookName(String addressBookName) 
	{
		this.addressBookName = addressBookName;
	}

	public void addAddressBooks() 
	{
		System.out.println("enter address book name");
		String name = scanner.next();
		int index = 0;
		if (addressBooksArray.containsKey(name)) 
		{
			System.out.println("address book "+name+" already exists!");
			return;
		}
		System.out.println("created address book "+name);
		AddContactDetails addContactDetails=new AddContactDetails();
		addContactDetails.setAddressBookName(name);
		addressBooksArray.put(name, new AddressBook(name));
		String fileName = name+".txt";
		StringBuffer addressBookBuffer = new StringBuffer();
		addressBooksArray.values().stream().forEach(contact -> {
			String personDataString = contact.toString().concat("\n");
			addressBookBuffer.append(personDataString);
		});

		try 
		{
			Files.write(Paths.get(fileName), addressBookBuffer.toString().getBytes());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void showAddressBook() 
	{
		System.out.println("Addressbooks:");
		for (String addressBookName : addressBooksArray.keySet()) 
		{
			System.out.println(addressBookName);
		}
	}
	
	public void searchPersonByState(String name, String state) 
	{
		for(AddressBook addressBook : addressBooksArray.values()) 
		{
			List<Contact> contactList = addressBook.getContact();
			contactList.stream()
				.filter(person -> person.getFirstName().equals(name) && person.getState().equals(state))
				.forEach(person -> System.out.println(person));
		}
	}	
		
	public void searchPersonByCity(String name, String city) 
	{
		for(AddressBook addressBook : addressBooksArray.values())
		{
			List<Contact> contactList = addressBook.getContact();
			contactList.stream()
				.filter(person -> person.getFirstName().equals(name) && person.getCity().equals(city))
				.forEach(person -> System.out.println(person));
		}
	}

	public AddressBook selectAddressBook(String name) 
	{	
		if (addressBooksArray.containsKey(name)) 
		{
			addressBook = addressBooksArray.get(name);
			this.setAddressBookName(name);
			return addressBook;
		}
		System.out.println("Address book "+name+" not found");
		return null;
	}
}
