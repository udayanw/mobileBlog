package mBlogPack;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;


public class RMSHandler
{
	/** Name of RecordStore */
    private static final String APP_SETTINGS = "settings"; 
    private static RMSHandler instance = new RMSHandler();
    private RecordStore rsSettings;
    private int recordId = -1;

    private RMSHandler()
    {
	try
	{
	    openRecordStore();
	    RecordEnumeration enumeration = rsSettings
		    .enumerateRecords(null, null, false);
	    while (enumeration.hasNextElement())
	    {
		recordId = enumeration.nextRecordId();
	    }
	    closeRecordStore();

	}
	catch (RecordStoreFullException e)
	{
	    e.printStackTrace();
	}
	catch (RecordStoreNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (RecordStoreException e)
	{
	    e.printStackTrace();
	}
    }

    public static RMSHandler getInstance()
    {
	return instance;
    }

    private void openRecordStore() throws RecordStoreFullException,
	    RecordStoreNotFoundException, RecordStoreException
    {
	rsSettings = RecordStore.openRecordStore(APP_SETTINGS, true);
    }

    private void closeRecordStore() throws RecordStoreNotOpenException,
	    RecordStoreException
    {
	rsSettings.closeRecordStore();
    }

    public Settings getSettings() throws RecordStoreNotOpenException,
	    InvalidRecordIDException, RecordStoreException
    {
	Settings appSettings = null;
	if (recordId != -1)
	{
	    openRecordStore();
	    byte[] recordData = rsSettings.getRecord(recordId);
	    if (recordData != null)
	    {
		String strRecord = new String(recordData);
		String[] parsedParams = parseSettings(strRecord);
		appSettings = new Settings();
		appSettings.setUserName(parsedParams[0]);
		appSettings.setPassword(parsedParams[1]);
		appSettings.setServerAddress(parsedParams[2]);
		appSettings.setCameraLocator(Integer.parseInt(parsedParams[3]));
	    }
	}
	return appSettings;
    }

    public void setSettings(String userName, String password, String serverAddress,
	    int snapperOption) throws RecordStoreFullException,
	    RecordStoreNotFoundException, RecordStoreException
    {
	StringBuffer record = new StringBuffer(userName);
	record.append(';').append(password);
	record.append(';').append(serverAddress);
	record.append(';').append(snapperOption);
	openRecordStore();
	byte[] recordData = record.toString().getBytes();
	if (recordId != -1)
	{
	    rsSettings.setRecord(recordId, recordData, 0, recordData.length);
	}
	else
	{
	    rsSettings.addRecord(recordData, 0, recordData.length);
	}
    }

    private String[] parseSettings(String recordData)
    {
	String username = recordData.substring(0, recordData.indexOf(';'));
	recordData = recordData.substring(recordData.indexOf(';') + 1);
	String password = recordData.substring(0, recordData.indexOf(';'));
	recordData = recordData.substring(recordData.indexOf(';') + 1);
	String serverURL = recordData.substring(0, recordData.indexOf(';'));
	recordData = recordData.substring(recordData.indexOf(';') + 1);
	String snapper = recordData;
	String[] sendStringArray =
	    { username, password, serverURL, snapper };
	return sendStringArray;

    }
}
