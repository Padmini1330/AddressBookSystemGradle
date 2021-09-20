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
	
	public enum IOService{
		CONSOLE_IO,FILE_IO,CSV_IO,JSON_IO
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
		System.out.println("Enter first name:");
		String firstName = scanner.next();
		System.out.println("Enter last name");
		String lastName = scanner.next();
		System.out.println("Enter city");
		String city = scanner.next();
		System.out.println("Enter address");
		String address = scanner.next();
		System.out.println("Enter state");
		String state = scanner.next();
		System.out.println("Enter Zip");
		int zip = scanner.nextInt();
		System.out.println("Enter Phone");
		int phoneNumber = scanner.nextInt();
		System.out.println("Enter email");
		String email = scanner.next();
		Contact contact = new Contact(firstName, lastName, address, state, city, zip, phoneNumber, email);
		
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
			int newZip = scanner.nextInt();
			editDetails.setZipCode(newZip);
			System.out.println("Edited zip code");
			break;
		case 6:
			System.out.println("Enter new Phone Number:");
			int newPNumber = scanner.nextInt();
			editDetails.setPhoneNumber(newPNumber);
			System.out.println("Edited phone number");
			break;
		case 7:
			System.out.println("Enter new Email:");
			String newEmail = scanner.next();
			editDetails.setEmailId(newEmail);
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
		record[3]=contact.getAddress();
		record[4]=contact.getState();
		record[5]=contact.getEmailId();
		record[6]=Integer.toString(contact.getZipCode());
		record[7]=Integer.toString(contact.getPhoneNumber());
		return record;
	}
	
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
}