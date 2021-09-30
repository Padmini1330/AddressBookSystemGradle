package AddressBookSystemGradle;

import java.time.LocalDate;
import java.util.HashMap;

import com.opencsv.bean.CsvBindByName;

public class Contact 
{
	@CsvBindByName
	private int contactID;
	@CsvBindByName
	private int addressBookID;
	@CsvBindByName
	private LocalDate date;
	@CsvBindByName
	private String firstName;
	@CsvBindByName
	private String lastName;
	@CsvBindByName
	private int addressID;
	@CsvBindByName
	private String phoneNumber;
	@CsvBindByName
	private String email;
	@CsvBindByName
	private String city;
	@CsvBindByName
	private String state;
	@CsvBindByName
	private String zip;
	Address address;
	
	public int getContactId() 
	{
		return contactID;
	}
	public void setContactId(int contactId) 
	{
		this.contactID = contactId;
	}
	public int getAddressBookId() 
	{
		return addressBookID;
	}
	public void setAddressBookId(int addressBookId) 
	{
		this.addressBookID=addressBookId;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAddressID() {
		return addressID;
	}

	public void setAddressID(int addressID) {
		this.addressID = addressID;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	public LocalDate getDate() 
	{
		return date;
	}
	public void setDate(LocalDate date) 
	{
		this.date = date;
	}
	public String getZip() 
	{
		return zip;
	}
	public void setZip(String zip) 
	{
		this.zip = zip;
	}
	
	
	
	
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getCity() {
		return getAddress().getCity();
	}

	public void setCity(String city) {
		this.getAddress().setCity(city);
	}


	public String getState() {
		return getAddress().getState();
	}

	public void setState(String state) {
		this.getAddress().setState(state);
	}

	
	public String getZipCode() {
		return getAddress().getZip();
	}

	public void setZipCode(String zip) {
		this.getAddress().setZip(zip); 
	}
	
	

	public Contact(int addressBookID, String firstName, String lastName, int addressID, String phoneNumber, int contactID,
			String email,LocalDate date) 
	{
		this.addressBookID=addressBookID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.addressID=addressID;
		this.phoneNumber=phoneNumber;
		this.contactID=contactID;
		this.email=email;
		this.date=date;
	}
	
	public Contact(int addressBookID, String firstName, String lastName, int addressID, String phoneNumber, int contactID,
			String email,LocalDate date,String city,String state,String zip) 
	{
		this.addressBookID=addressBookID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.addressID=addressID;
		this.phoneNumber=phoneNumber;
		this.contactID=contactID;
		this.email=email;
		this.date=date;
		address=new Address(addressID,city,state,zip);
	}
	

	@Override
	public boolean equals(Object anotherObject) {
		HashMap<String, Contact> addressBook = (HashMap<String, Contact>) anotherObject;
		if (addressBook.keySet().stream().anyMatch(s -> (s.equals(firstName)))) {
			return true;
		}
		return false;

	}

	@Override
	public String toString() {
		return "Contact [contactID=" + contactID + ", addressBookID=" + addressBookID + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", addressID=" + addressID + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", date="
						+ date + "]";
	}


}	
