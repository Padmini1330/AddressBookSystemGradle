package AddressBookSystemGradle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import AddressBookSystemGradle.AddContactDetails.IOService;

public class FileIOService 
{
	AddContactDetails contactDetails=new AddContactDetails();
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
}
