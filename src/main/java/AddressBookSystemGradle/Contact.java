package AddressBookSystemGradle;

import java.util.HashMap;

import com.opencsv.bean.CsvBindByName;

public class Contact 
{
	
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
	public String getZip() 
	{
		return zip;
	}
	public void setZip(String zip) 
	{
		this.zip = zip;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}

	@CsvBindByName
	private int contactID;
	@CsvBindByName
	private int addressBookID;
	@CsvBindByName
	private String firstName;
	@CsvBindByName
	private String lastName;
	@CsvBindByName
	private int addressID;
	@CsvBindByName
	private String city;
	@CsvBindByName
	private String state;
	@CsvBindByName
	private String zip;
	@CsvBindByName
	private String phoneNumber;
	@CsvBindByName
	private String email;
	Address address;
	Contact(String firstName, String lastName, String city, int addressID, String state, String zip, String phoneNumber,
			String email)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressID = addressID;
		this.phoneNumber = phoneNumber;
		this.email = email;
		address=new Address(city, zip, state);
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	Contact(Integer contactId,Integer addressBookId,Integer addressID,String firstName, String lastName, String city, String state, String zip, String phoneNumber,
			String email) 
	{
		this.addressBookID=addressBookId;
		this.contactID=contactId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.addressID = addressID;
		this.phoneNumber = phoneNumber;
		this.email = email;
		Address address1=new Address(addressBookID,city,zip,state);
	}

	public Contact(int addressBookID, String firstName, String lastName, int addressID, String phoneNumber, int contactID, String email) 
	{
		this.addressBookID=addressBookID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.addressID=addressID;
		this.phoneNumber=phoneNumber;
		this.contactID=contactID;
		this.email=email;
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

	public void setAddressD(int addressID) {
		this.addressID = addressID;
	}

	public String getCity() {
		return getAddress().getCity();
	}

	public void setState(String state) {
		this.getAddress().setState(state);
	}

	public String getState() {
		return getAddress().getState();
	}

	public void setCity(String city) {
		this.getAddress().setCity(city);
	}

	public String getZipCode() {
		return getAddress().getZip();
	}

	public void setZipCode(String zip) {
		this.getAddress().setZip(zip); 
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return email;
	}

	public void setEmailId(String email) {
		this.email = email;
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
		return "First Name: " + getFirstName() + "\n" + "Last Name: " + getLastName() + "\n" + "city :" + getCity()
				+ "\n" + "Address: " + getAddress() + "\n" + "state: " + getState() + "\n" + "Phone Number: "
				+ getPhoneNumber() + "\n" + "Email: " + getEmailId();
	}


}	
