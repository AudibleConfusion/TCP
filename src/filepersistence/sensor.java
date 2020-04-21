package filepersistence;
import java.io.*;
import java.util.Date;

// Should be able to write and read variable amounts of datasets with variable amounts of measurements

public class sensor
{
    public static void main(String[] args) throws PersistenceException
    {
        // three example data sets
        String sensorName = "MyGoodOldSensor"; // does not change

        long timeStamps[] = new long[3];
        timeStamps[0] = System.currentTimeMillis();
        timeStamps[1] = timeStamps[0] + 10000;
        timeStamps[2] = timeStamps[1] + 10000;

        float[][] values = new float[3][];
        // 1st measure .. just one value
        float[] valueSet = new float[1];
        values[0] = valueSet;
        valueSet[0] = (float) 1.5; // example value 1.5 degrees

        // 2nd measure .. just three values
        valueSet = new float[3];
        values[1] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;
        valueSet[2] = (float) 2.1;

        // 3rd measure .. two values
        valueSet = new float[2];
        values[2] = valueSet;
        valueSet[0] = (float) 0.7;
        valueSet[1] = (float) 1.2;

        //implementation
        ISensorDataStorage sds = new SensorDataStorage();

        sds.writeToFile(sensorName, timeStamps, values);
        readFromFile();
    }

    public static void readDataSet(DataInputStream dis) // reads and prints datasets
    {
        try
        {
            String name = dis.readUTF();
            System.out.println(name);
            long date = dis.readLong();
            System.out.println(new Date(date));
            int length = dis.readInt();
            for(int i = 0; i < length; i++)
            {
                float measurement = dis.readFloat();
                System.out.println(measurement);
            }
        }
        catch (IOException ex)
        {
            System.err.println("Couldn't read from file - fatal");
            System.exit(0);
        }
    }

    public static void readFromFile() // determines how many datasets are present in file
    {
        InputStream is;
        DataInputStream dis = null;

        try
        {
            is = new FileInputStream("testfile.txt");
            dis = new DataInputStream(is);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Couldn't open file - fatal");
            System.exit(0);
        }

        try
        {
            int length = dis.readInt(); //length determines how many datasets are to be read
            for(int i = 0; i < length; i++)
            {
                System.out.printf("Dataset #%d%n", i + 1);
                readDataSet(dis);
                System.out.println();
            }
        }
        catch (IOException ex)
        {
            System.err.println("Couldn't read from file - fatal");
            System.exit(0);
        }
    }
}