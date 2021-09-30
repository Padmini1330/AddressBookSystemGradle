package AddressBookSystemGradle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class AddContactDetails implements AddContactDetailsIF
{

	Scanner scanner = new Scanner(System.in);
	private String addressBookName;
	private HashMap<String, Contact> addressBook;
	private Contact editDetails;
	
	public enum IOService
	{
		CONSOLE_IO,FILE_IO,CSV_IO,JSON_IO,DB_IO
	}
	
	public HashMap<String, ArrayList<Contact>> personWithCity;
	public HashMap<String, ArrayList<Contact>> personWithState;

	public AddContactDetails() 
	{
		personWithCity = new HashMap<String, ArrayList<Contact>>();
		personWithState = new HashMap<String, ArrayList<Contact>>();
	}
	public ArrayList<Contact> getContact() 
	{
		return new ArrayList<Contact>(addressBook.values());
	}
	
	public String getAddressBookName() 
	{
		return this.addressBookName;
	}
	
	public void setAddressBookName(String addressBookName) 
	{
		this.addressBookName = addressBookName;
	}
	
	@Override
	public void addContact(HashMap<String, Contact> addressBookContact) {
		System.out.println("Add Contact");
		System.out.println("Enter addressbook id:");
		int addressBookID = scanner.nextInt();
		System.out.println("Enter first name:");
		String firstName = scanner.next();
		System.out.println("Enter last name");
		String lastName = scanner.next();
		System.out.println("Enter address");
		int address = scanner.nextInt();
		System.out.println("Enter Phone");
		String phoneNumber = scanner.next();
		System.out.println("Enter contactID");
		int contactID = scanner.nextInt();
		System.out.println("Enter email");
		String email = scanner.next();
		Contact contact = new Contact(addressBookID,firstName, lastName, address, phoneNumber, contactID,email,LocalDate.now());
		
		if(contact.equals(addressBookContact)) 
		{
			System.out.println("Contact "+firstName+" already exists!");
			return;
		}
		personWithCity(contact);
		personWithState(contact);
		addressBookContact.put(firstName, contact);
		System.out.println("Contact added successfully!");
		
		
				
	}

	@Override
	public void showContacts(HashMap<String, Contact> addressBookContact) 
	{
		for(Contact contact:addressBookContact.values())
			System.out.println(contact);
	}

	@Override
	public void editContact(HashMap<String, Contact> addressBookContact) 
	{
		System.out.println("Edit contact:");
		System.out.println("Select Option:\n1.First Name\n2.Last Name\n3.City\n4.State\n5.Zip Code\n6.Phone\n7.Email");
		int choice = scanner.nextInt();
		System.out.println("Enter First Name of contact to be edited");
		String editName = scanner.next();
		int index = 0;
		if(!addressBookContact.containsKey(editName))
		{
			System.out.println("No such contact found!");
			return;
		}
		else
			editDetails=addressBookContact.get(editName);
		
		switch (choice) 
		{
		case 1:
			System.out.println("Enter new First Name:");
			String newFName = scanner.next();
			editDetails.setFirstName(newFName);
			System.out.println("Edited first name");
			break;
		case 2:
			System.out.println("Enter new Last Name:");
			String newLName = scanner.next();
			editDetails.setLastName(newLName);
			System.out.println("Edited last name");
			break;
		case 3:
			System.out.println("Enter new City:");
			String newCity = scanner.next();
			editDetails.setCity(newCity);
			System.out.println("Edited city");
			break;
		case 4:
			System.out.println("Enter new State:");
			String newState = scanner.next();
			editDetails.setState(newState);
			System.out.println("Edited state");
			break;
		case 5:
			System.out.println("Enter new Zip code:");
			String newZip = scanner.next();
			editDetails.setZipCode(newZip);
			System.out.println("Edited zip code");
			break;
		case 6:
			System.out.println("Enter new Phone Number:");
			String newPNumber = scanner.next();
			editDetails.setPhoneNumber(newPNumber);
			System.out.println("Edited phone number");
			break;
		case 7:
			System.out.println("Enter new Email:");
			String newEmail = scanner.next();
			editDetails.setEmail(newEmail);
			System.out.println("Edited email");
			break;
		}
	}

	@Override
	public void deleteContact(HashMap<String, Contact> addressBookContact) 
	{
		System.out.println("Enter Name of Contact to delete");
		String deletedName = scanner.next();
		if(addressBookContact.containsKey(deletedName))
		{
			addressBookContact.remove(deletedName);
			System.out.println("deleted address book "+ deletedName);
			return;
		}
		System.out.println("Contact name "+deletedName+" does'nt exits");
	}
	
	public void personWithCity(Contact contact) 
	{
		if (personWithCity.containsKey(contact.getCity()))
			personWithCity.get(contact.getCity()).add(contact);
		else 
		{
			ArrayList<Contact> contactList = new ArrayList<Contact>();
			contactList.add(contact);
			personWithCity.put(contact.getCity(), contactList);
		}
	}

	public void personWithState(Contact contact) 
	{
		if (personWithState.containsKey(contact.getState()))
			personWithState.get(contact.getState()).add(contact);
		else 
		{
			ArrayList<Contact> contactList = new ArrayList<Contact>();
			contactList.add(contact);
			personWithState.put(contact.getState(), contactList);
		}
	}

	public void showPersonList(String location, HashMap<String, ArrayList<Contact>> contactsList) 
	{
		contactsList.values()
		.stream()
		.map(place -> place.stream().filter(person -> person.getCity().equals(location) || person.getState().equals(location)))
		.forEach(contact -> System.out.println("Contact's list based on "+ location+" is " + contact));
	}
	
	public void showCountofContacts(String location, HashMap<String, ArrayList<Contact>> contactsList ) 
	{
		System.out.println("Number of contact persons in "+location+" are:");
		
		long count = contactsList.values()
				.stream()
				.map(place -> place.stream().filter(person -> person.getCity().equals(location) || person.getState().equals(location)))
				.count();
		
		
		System.out.println("No contacts found in the selected location!");
	}
	
	public void sortContacts(HashMap<String, Contact> addressBook, int sortKey) 
	{
		ArrayList<Contact> contactsList = new ArrayList<Contact>(addressBook.values());
	
		switch (sortKey) 
		{
		case 1:
			List<Contact> sortedContactsList = contactsList.stream()
					.sorted((s1, s2) -> s1.getFirstName()
					.compareTo(s2.getFirstName()))
					.collect(Collectors.toList());
			System.out.println("Contacts after sorting : ");
			System.out.println(sortedContactsList);
			break;
		case 2:
			List<Contact> sortedContactsByCityList = contactsList.stream()
					.sorted((s1, s2) -> s1.getCity()
					.compareTo(s2.getCity()))
					.collect(Collectors.toList());
			System.out.println("Contacts after sorting : ");
			System.out.println(sortedContactsByCityList);
			break;
		case 3:
			List<Contact> sortedContactsByStateList = contactsList.stream()
					.sorted((s1, s2) -> s1.getState()
					.compareTo(s2.getState()))
					.collect(Collectors.toList());
			System.out.println("Contacts after sorting : ");
			System.out.println(sortedContactsByStateList);
			break;
		case 4:
			List<Contact> sortedContactsByZipList = contactsList.stream()
					.sorted((s1, s2) -> Integer.valueOf(s1.getZipCode())
					.compareTo(Integer.valueOf(s2.getZipCode())))
					.collect(Collectors.toList());
			System.out.println("Contacts after sorting : ");
			System.out.println(sortedContactsByZipList);
			break;
		default:
			break;
		}
	}

	//read and write into normal file
	public void writeAddressDataToFile(String addressBookName, HashMap<String, Contact> addressBook) 
	{
		StringBuffer contactBuffer = new StringBuffer();
		addressBook.values().forEach(contact -> {
			String contactString = contact.toString().concat("\n");
			contactBuffer.append(contactString);
		});

		try 
		{
			if (Paths.get(addressBookName) == null) {
				String newFile = "src/" + addressBookName + ".txt";
				Files.createFile(Paths.get(newFile));
			}
			Files.write(Paths.get(addressBookName.concat(".txt")), contactBuffer.toString().getBytes());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void readAddressDataFromFile(String name, HashMap<String, Contact> addressBook) 
	{
		List<Contact> listOfContacts = new ArrayList<Contact>();
		try 
		{
			String newFile = "src/" + name + ".txt";
			Files.lines(Paths.get(name.concat(".txt"))).forEach(System.out::println);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
	
	public String[] getStringArray(Contact contact) 
	{
		String record[] = new String[8];
		record[0]=contact.getFirstName();
		record[1]=contact.getLastName();
		record[2]=contact.getCity();
		record[3]=Integer.toString(contact.getAddressID());
		record[4]=contact.getState();
		record[5]=contact.getEmail();
		record[6]=contact.getZipCode();
		record[7]=contact.getPhoneNumber();
		return record;
	}
	
	//read and write into csv file
	public void writeToCsvFile(String fileName,HashMap<String, Contact> addressBook) 
	{
		try 
		{
			CSVWriter writer = new CSVWriter(new FileWriter(fileName.concat(".csv")));
			List<String[]> contactsArrayList= new ArrayList();
			for(Contact contact:addressBook.values()) 
			{
				contactsArrayList.add(getStringArray(contact));
			}
			writer.writeAll(contactsArrayList);
			writer.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	public void readFromCsvFile(String fileName,HashMap<String, Contact> addressBook) 
	{
		 try {
		        FileReader filereader = new FileReader(fileName+".csv");
		        CSVReader csvReader = new CSVReader(filereader);
		        String[] nextRecord;
		        while ((nextRecord = csvReader.readNext()) != null) 
		        {
		            for (String data : nextRecord) 
		            {
		                System.out.print(data + "\t");
		            }
		            System.out.println();
		        }
		    }
		    catch (Exception e) 
		 	{
		        e.printStackTrace();
		    }
	}
	
	//read and write into json
	public void writeToJson(String name, HashMap<String, Contact> addressBook) throws IOException 
	{
		Gson gson = new Gson();
		String json = gson.toJson(addressBook);
		FileWriter writer = new FileWriter(name.concat(".json"));
		writer.write(json);
		writer.close();
	}

	public void readFromJson(String name, HashMap<String, Contact> addressBook) throws FileNotFoundException 
	{
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new FileReader(name));
		Contact[] contactsFile = gson.fromJson(reader, Contact[].class);
		List<Contact> addressBookDetails = Arrays.asList(contactsFile);
		System.out.println(addressBookDetails);
	}
	
	
	
	
	//service:read contact details from database
	public List<Contact> readDb(int addressBookID, IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
			return AddressBookDBService.getDBInstance().readContacts(addressBookID);
		return null;
	}
	
	//service:write contact details to database
	public Contact writeAddressBookDB(Contact contact,int addressBookID, IOService ioService)
	{
		if(ioService.equals(IOService.DB_IO))
			return AddressBookDBService.getDBInstance().writeAddressBookDB(contact,addressBookID);
		return null;
	}
	
	public boolean compareContactSync(Contact updatedContact,int addressBookID, IOService ioService) 
	{
		List<Contact> contactsList=readDb(addressBookID, ioService);
		for(Contact contact:contactsList) 
		{
			if(contact.toString().equals(updatedContact.toString()))
				return true;
		}
		return false;
	}
	
	//service:read contacts in specific range
	public List<Contact> readContactsAddedInRange(Date startDate, Date endDate, IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
			return AddressBookDBService.getDBInstance().readContactsAddedInRange(startDate, endDate);
		return null;
	}
	
	//service:read contacts from specific city
	public List<Contact> readContactsAddedInGivenCity(String city, IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
			return AddressBookDBService.getDBInstance().readContactsInGivenCity(city);
		return null;
	}
	
	//service:read contacts from specific state
	public List<Contact> readContactsAddedInGivenState(String state, IOService ioService) 
	{
		if(ioService.equals(IOService.DB_IO))
			return AddressBookDBService.getDBInstance().readContactsInGivenState(state);
		return null;
		
	}
	
	
}