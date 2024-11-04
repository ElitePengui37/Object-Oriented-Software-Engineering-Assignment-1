package edu.curtin.app; // dont know what this is but gradle came with it
import java.util.logging.Logger; // java logging library
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Entry point into the application. To change the package, and/or the name of this class, make
 * sure to update the 'mainClass = ...' line in build.gradle.
 */

// to run gradle with command line arguements use   ./gradlew run --args="arguement"
public class App
{
    private static final Logger log = Logger.getLogger(App.class.getName()); // imports logger object
    private static List<Object>[][] structure;
    private static List<Object>[][] baseCity;

    public static void main(String[] args)
    {
        Integer quit = 1;

        if (args.length == 1)
        {
            baseCity = fillMap(args); // inpu file is Read if command line arguement is found and filled
            

            if (baseCity!= null) // skips over if null and terminates program
            {   
                structure = initializeStructureContainer(baseCity); // initialize structure to be the same as baseCity
                quit = 0; // dont quit if everything is successful
            }


            log.info("use below line of code commented out to check contents on BaseCity individually");
            //System.out.println("testing generic stuff " + ((Terrain) BaseCity[0][3].get(0)).getDescription());
        }
        else    // else log error an continue to execute (exit program)
        {
            System.out.println("Invalid Command Line Arguments");
            log.info("Error: Command line arguments are invalid");
        }


        // while loop takes care of user options menu
        while(quit == 0)
        {
            quit = Menu.chooseOption();
        }
    }


    // Purpose of this method is to read user inputted file and place contents into the ArrayList
    public static List<Object>[][] fillMap(String[] args)
    {
        
        List<Object>[][] baseCity = null;
        String filename = args[0];


        // create buffered reader object in try-with-resources block
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)))
        {
            //String filename = args[0];
            log.info(() -> "filename: " + filename);

            String firstLine = reader.readLine();

            String[] size = firstLine.split(","); // remove , from first line


            int rows = Integer.parseInt(size[0].trim()); // convert string numbers to ints
            int cols = Integer.parseInt(size[1].trim());
            
            log.info(() -> "Size: " + rows + " rows, " + cols + " columns");



            // fill BaseCity with arraylists
            baseCity = new List[rows][cols];
            for (int i = 0; i < rows; i++)
            {
                for (int j = 0; j < cols; j++)
                {
                    baseCity[i][j] = new ArrayList<>();
                }
            }


            // This block adds terrain stuff to BaseCity
            String line;
            int rowIndex = 0;
            int colIndex = 0;
            while ((line = reader.readLine()) != null && rowIndex < rows)
            {
                Terrain terrain = createTerrain(line); // Decorator Concrete class is called and Mandatory Terrain is created
                baseCity[rowIndex][colIndex].add(terrain); // Terrain and all its other parts are added to arraylist held at index held in BaseCity

                colIndex++;

                if (colIndex >= cols) // Move to the next row after filling all columns
                {
                    colIndex = 0;
                    rowIndex++;
                }
            }
        }
        catch (IOException errorDetails) // Exception is called whenever there is a problem in file processing such as file not being found
        {
            System.out.println("\n\nError in file processing, terminating program: " + errorDetails.getMessage() + "\n\n");
            log.severe("An error has occurred in file processing.");
            //baseCity = null; // returns null array to terinate program   
        }
        return baseCity;
    }



    // This method initializes the structure array
    public static List<Object>[][] initializeStructureContainer(List<Object>[][] baseCity)
    {
        int rows = baseCity.length; // initialize size values as size of baseCity
        int cols = baseCity[0].length;

        log.info(() -> "checking size of structure array x = " + rows + " y = " + cols);

        List<Object>[][] structure = new List[rows][cols]; // structure is created

        // structure is filled with empty arraylists
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                structure[i][j] = new ArrayList<>();
            }
        }

        return structure;
    }



    private static Terrain createTerrain(String line)
    {
        String[] parts = line.split(",");
        Terrain terrain;
        
        // if statement checks terrain type and calls Concrete decorator class terrain to create it
        if(parts[0].equals("swampy"))
        {
            terrain = new SwampyTerrain();
        }
        else if(parts[0].equals("rocky"))
        {
            terrain = new RockyTerrain();
        }
        else if(parts[0].equals("flat"))
        {
            terrain = new FlatTerrain();
        }
        else
        {
            throw new IllegalArgumentException("Unknown terrain type file is invalid Terminating " + parts[0]); // if file contains any invalid terrain types the program throws an exception
        }
        

        // foreach loop adds optional Decorators to the zoning rules
        for (String part : parts)
        {
            if (part.startsWith("heritage="))
            {
                String heritageType = part.split("=")[1];
                terrain = new HeritageDecorator(terrain, heritageType);
            }
            else if (part.startsWith("flood-risk="))
            {
                Double floodRisk = Double.parseDouble(part.split("=")[1]);
                terrain = new FloodRiskDecorator(terrain, floodRisk);
            }
            else if (part.startsWith("height-limit="))
            {
                Integer heightLimit = Integer.parseInt(part.split("=")[1]);
                terrain = new HeightLimitDecorator(terrain, heightLimit);
            }
            else if (part.equals("contamination"))
            {
                terrain = new ContaminationDecorator(terrain, true);
            }
        }

        return terrain;
    }

    

    // both work tested
    // getter for structure array
    public static List<Object>[][] getStructure()
    {
        return structure;
    }

    // setter for structure array
    public static void setStructure(List<Object>[][] sStructure)
    {
        structure = sStructure;
    }

    // getter for base city (works)
    public static List<Object>[][] getBaseCity()
    {
        return baseCity;
    }

    
}
