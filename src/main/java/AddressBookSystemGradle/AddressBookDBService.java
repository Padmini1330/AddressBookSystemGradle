package AddressBookSystemGradle;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AddressBookSystemGradle.AddContactDetails.IOService;

public class AddressBookDBService 
{
	
	
	public static Connection getConnection() throws SQLException 
    {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookService?useSSL=false";
		String userName = "root";
		String password = "Padmini_1330";
		Connection connection;
		
		System.out.println("Connecting to the database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		System.out.println("Connection is Succcessfully Established!! "+connection);
		
		return connection;
	}
	
	AddContactDetails contactDetails=new AddContactDetails();
	public void writeService(String fileName, HashMap<String, Contact> addressBook, IOService ioService) 
	{
		if (ioService == IOService.CSV_IO)
			contactDetails.writeToCsvFile(fileName, addressBook);
		else if (ioService == IOService.FILE_IO)
			contactDetails.writeAddressDataToFile(fileName, addressBook);
		else if (ioService == IOService.JSON_IO) 
		{
			try 
			{
				contactDetails.writeToJson(fileName, addressBook);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	public void readService(String fileName, HashMap<String, Contact> addressBook, IOService ioService) 
	{
		if (ioService == IOService.CSV_IO)
			contactDetails.readFromCsvFile(fileName, addressBook);
		else if (ioService == IOService.FILE_IO)
			contactDetails.readAddressDataFromFile(fileName, addressBook);
		else if (ioService == IOService.JSON_IO) 
		{
			try 
			{
				contactDetails.readFromJson(fileName, addressBook);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public List<Contact> readContacts(int addressBookID)
	{
		String sql="SELECT * from ContactDetails where AddressBookID=\""+addressBookID+"\"";
		List<Contact> contactsList=new ArrayList<Contact>();
		try
		{
			Connection connection=this.getConnection();
			System.out.println("connected");
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next())
			{
				contactsList.add(new Contact(resultSet.getInt("AddressBookID"),resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getInt("AddressID"), resultSet.getString("PhoneNumber"),resultSet.getInt("ContactID"), resultSet.getString("Email")));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return contactsList;
	}
	
	public void writeAddressBookDB(Contact contact,int addressBookID)
	{
		String insertQueryString="INSERT into ContactDetails values("+contact.getAddressBookId()+",\""+contact.getFirstName()+"\",\""+contact.getLastName()+"\",\""+contact.getAddressID()+"\",\""+contact.getPhoneNumber()+"\",\""+contact.getContactId()+"\",\""+contact.getEmail()+"\")";
		String insertPlaceQueryString="INSERT into Address values("+contact.getAddress().getAddressID()+",\""+contact.getCity()+"\",\""+contact.getState()+"\",\""+contact.getZipCode()+"\")";
		try 
		{
			Connection connection=this.getConnection();
			System.out.println("connected");
			Statement statement=connection.createStatement();
			statement.executeUpdate(insertPlaceQueryString);
			statement.executeUpdate(insertQueryString);
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	
}
