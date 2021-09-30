package AddressBookSystemGradle;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import AddressBookSystemGradle.AddContactDetails.IOService;

public class AddressBookDBService 
{
	AddContactDetails contactDetails=new AddContactDetails();
	private static AddressBookDBService addressBookDBService;
	private PreparedStatement readContactPreparedStatement;
	private PreparedStatement contactAddedGivenRange;
	private PreparedStatement contactsInGivenCity;
	private PreparedStatement contactsInGivenState;

	public AddressBookDBService() 
	{

	}

	public static AddressBookDBService getDBInstance() 
	{
		addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}
	
	//normal file read and write service
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

	//connecting java program to database
	public static Connection getConnection() throws SQLException, ClassNotFoundException 
    {
		
		String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookSystem?useSSL=false";
		String userName = "root";
		String password = "Padmini_1330";
		Connection connection = DriverManager.getConnection(jdbcURL, userName, password);		
		return connection;
	}

	
	//read contacts from database
	
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
				contactsList.add(new Contact(resultSet.getInt("AddressBookID"),resultSet.getString("FirstName"), resultSet.getString("LastName"), resultSet.getInt("AddressID"), resultSet.getString("PhoneNumber"),resultSet.getInt("ContactID"), resultSet.getString("Email"),resultSet.getDate("DateAdded").toLocalDate()));
			}
		} 
		catch (SQLException e) 
		{
			throw new DatabaseException(e.getMessage());
		}
		return contactsList;

	}
	
	
	//to write the contact to address book database
	public Contact writeAddressBookDB(Contact contact, int addressBookID) 
	{
		Contact updatedContact;
		
		String insertIntoContactsTable=String.format("INSERT into ContactDetails values('%s','%s','%s','%s','%s','%s','%s','%s')",contact.getAddressBookId(),
																	contact.getFirstName(),
																	contact.getLastName(),
																	contact.getAddressID(),
																	contact.getPhoneNumber(),
																	contact.getContactId(),
																	contact.getEmail(),
																	contact.getDate());
		String insertIntoAddressTable=String.format("INSERT into Address values('%s','%s','%s','%s')",contact.getAddressID(),
																	contact.getAddress().getCity(),
																	contact.getAddress().getState(),
																	contact.getAddress().getZip());	
		
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
			statement.executeUpdate(insertIntoContactsTable);
			statement.executeUpdate(insertIntoAddressTable);
			connection.commit();
			updatedContact = contact;
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
		System.out.println("transaction complete!");
		return updatedContact;
	}

	
	//prepared statement to read contacts
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
	
	//retrieve contacts in given range
	private void preparedStatementToRetrieveContactsInRange()
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
            this.preparedStatementToRetrieveContactsInRange();
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
	
	//retrieve contacts in a given city
	private void preparedStatementToRetrieveContactsInGivenCity() 
	{
		try 
		{
			Connection connection = this.getConnection();
			String query = "SELECT * from ContactDetails c ,Address a where c.AddressID = a.AddressID and City =?";
			contactsInGivenCity = connection.prepareStatement(query);
			
		} 
		catch (Exception e) 
		{
			throw new DatabaseException(e.getMessage());
		}
	}

	public List<Contact> readContactsInGivenCity(String city) 
	{
		if (contactsInGivenCity == null) 
		{
			this.preparedStatementToRetrieveContactsInGivenCity();
		}
		try 
		{
			contactsInGivenCity.setString(1, city);
			ResultSet resultSet = contactsInGivenCity.executeQuery();
					
			return this.getContactList(resultSet);
		} 
		catch (Exception e) 
		{
			throw new DatabaseException(e.getMessage());
		}
	}
	
	//retrieve contacts in a given state
	private void preparedStatementToRetrieveContactsInGivenState() 
	{
		try 
		{
			Connection connection = this.getConnection();
			String query = "SELECT * from ContactDetails c ,Address a where c.AddressID = a.AddressID and State=?";
			contactsInGivenState = connection.prepareStatement(query);
			
		} 
		catch (Exception e) 
		{
			throw new DatabaseException(e.getMessage());
		}
	}

	public List<Contact> readContactsInGivenState(String state) 
	{
		if (contactsInGivenState == null) 
		{
			this.preparedStatementToRetrieveContactsInGivenState();
		}
		try 
		{
			contactsInGivenState.setString(1, state);
			ResultSet resultSet = contactsInGivenState.executeQuery();
			return this.getContactList(resultSet);
		} 
		catch (Exception e) 
		{
			throw new DatabaseException(e.getMessage());
		}
	}
	
}
