package phased.game.util;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtil
{
    /**
     * @return The entire source of a file as a single string
     */
    public static String readFromFile(String name)
    {
        StringBuilder source = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(name)));

            String line;
            while ((line = reader.readLine()) != null)
            {
                source.append(line).append("\n");
            }

            reader.close();
        }
        catch (Exception e)
        {
            System.err.println("Error loading source code: " + name);
            e.printStackTrace();
        }

        return source.toString();
    }

    /**
     * @return An array of all the lines of a file
     */
    public static String[] readAllLines(String name)
    {
        return readFromFile(name).split("\n");
    }
}