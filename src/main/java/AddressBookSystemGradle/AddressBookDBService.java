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
	AddContactDetails contactDetails=new AddContactDetails();
	private static AddressBookDBService addressBookDBService;
	private java.sql.PreparedStatement readContactPreparedStatement;
	private PreparedStatement contactAddedGivenRange;

	public AddressBookDBService() 
	{

	}

	public static AddressBookDBService getDBInstance() 
	{
		addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}
	
	public void writeService(String fileName, HashMap<String, Contact> addressBook, IOService ioService) {
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

	public void readService(String fileName, HashMap<String, Contact> addressBook, IOService ioService) {
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

	public List<Contact> readContacts(int addressBookID) 
	{
		ResultSet resultSet;
		if (readContactPreparedStatement == null)
			this.preparedStatementToReadContacts();
		try 
		{
			readContactPreparedStatement.setInt(1, addressBookID);
			resultSet = readContactPreparedStatement.executeQuery();
		} 
		catch (SQLException e)
		{
			throw new DatabaseException(e.getMessage());
		}

		return getContactList(resultSet);
	}

	public List<Contact> getContactList(ResultSet resultSet)
	{
		List<Contact> contactsList = new ArrayList<Contact>();
		try 
		{
			while(resultSet.next())
			{
				contactsList.add(new Contact(resultSet.getInt("AddressBookID"),resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getInt("AddressID"), resultSet.getString("PhoneNumber"),resultSet.getInt("ContactID"), resultSet.getString("Email")));
			}
		} 
		catch (SQLException e) 
		{
			throw new DatabaseException(e.getMessage());
		}
		return contactsList;

	}

	public Contact writeAddressBookDB(Contact contact, int addressBookID) 
	{
		Contact updatedContact;
		String insertQueryString="INSERT into ContactDetails values("+contact.getAddressBookId()+",\""+contact.getFirstName()+"\",\""+contact.getLastName()+"\",\""+contact.getAddressID()+"\",\""+contact.getPhoneNumber()+"\",\""+contact.getContactId()+"\",\""+contact.getEmail()+"\")";
		String insertPlaceQueryString="INSERT into Address values("+contact.getAddress().getAddressID()+",\""+contact.getCity()+"\",\""+contact.getState()+"\",\""+contact.getZipCode()+"\")";
				
		Connection connection;
		try 
		{
			connection = this.getConnection();
			connection.setAutoCommit(false);
		} 
		catch (Exception e)
		{
			throw new DatabaseException(e.getMessage());
		}

		try (Statement statement = connection.createStatement())
		{
			statement.executeUpdate(insertPlaceQueryString);
		} 
		catch (Exception e) 
		{
			try 
			{
				connection.rollback();
			} 
			catch (SQLException e1) 
			{
				throw new DatabaseException(e.getMessage());
			}
			throw new DatabaseException(e.getMessage());
		}

		try (Statement statement = connection.createStatement()) 
		{
			statement.executeUpdate(insertQueryString);
			connection.commit();
			updatedContact = contact;
		} 
		catch (Exception e) 
		{
			try 
			{
				connection.rollback();
			} 
			catch (SQLException sqlException)
			{
				throw new DatabaseException(sqlException.getMessage());
			}
			throw new DatabaseException(e.getMessage());
		}

		finally
		{
			try
			{
				connection.close();
			} 
			catch (SQLException e)
			{
				throw new DatabaseException(e.getMessage());
			}
		}
		return updatedContact;
	}

	

	private void preparedStatementToReadContacts() 
	{
		try 
		{
			Connection connection = this.getConnection();
			String sql = "SELECT * from ContactDetails where AddressBookID=?";
			readContactPreparedStatement = connection.prepareStatement(sql);
		} 
		catch (Exception e) 
		{
			throw new DatabaseException(e.getMessage());
		}
	}
	private void preparedStatementToRetriveContactsInRange()
	{
        try 
        {
            Connection connection = this.getConnection();
            String query = "SELECT * from ContactDetails where DateAdded between ? and ?";
            contactAddedGivenRange = connection.prepareStatement(query);
        }
        catch (Exception e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }

	public List<Contact> readContactsAddedInRange(Date startDate, Date endDate) 
	{
        if (contactAddedGivenRange == null) 
        {
            this.preparedStatementToRetriveContactsInRange();
        }
        try 
        {
        	contactAddedGivenRange.setDate(1, startDate);
        	contactAddedGivenRange.setDate(2, endDate);
            ResultSet resultSet = contactAddedGivenRange.executeQuery();
            return this.getContactList(resultSet);
        } 
        catch (Exception e)
        {
            throw new DatabaseException(e.getMessage());
        }
    }
	
}
