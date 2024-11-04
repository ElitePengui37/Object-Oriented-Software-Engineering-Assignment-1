// this file handles Hashmap menus
package edu.curtin.app;
import java.util.logging.Logger; // java logging library
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier; // this allows methods called by a map to return values
import java.util.InputMismatchException;
import java.util.List;
import java.util.ArrayList;


public class Menu
{

    private static final Logger log = Logger.getLogger(App.class.getName()); // imports logger object
    private static final Scanner SCANNER = new Scanner(System.in);

    // default random city configuration object if not set by user
    private static CityBuilder cityBuilder = new RandomCityConfiguration(); // declares CityBuilder common interface to be used in STRATAGY PATTERN Random city configuration is set to default

    public static int chooseOption()
    {
        Map<Integer, Supplier<Integer>> menuOptions = new HashMap<>(); // hashmap contains menu options for function calls
        Map<Integer, String> menuDescriptions = new HashMap<>(); // hashmap contains string values and numbers for menu option names for display purposes
        int quit;
        int choice = -1;
        boolean valid = false;



        // initialize map values
        menuOptions.put(1, () -> placeStructure());
        menuDescriptions.put(1, "Place a Structure"); // need 1 place structure

        menuOptions.put(2, () -> cityBuilder.placeCity()); // need 2 build city (STRATAGY PATTEN)       polymorphic method call for stratagy pattern
        menuDescriptions.put(2, "Build City");

        menuOptions.put(3, () -> chooseCityConfiguration()); // need 3 choose city configuration (STRATAGY PATTEN)
        menuDescriptions.put(3, "Choose city configuration");
        
        menuOptions.put(4, () -> quitApplication());
        menuDescriptions.put(4, "Quit");


        System.out.println("\n\nMenu:"); // show menu Items from menuDescriptions map map
        for (Map.Entry<Integer, String> entry : menuDescriptions.entrySet())
        {
            System.out.println(entry.getKey() + ". " + entry.getValue().toString());
        }
        
        // checking if user input is correct with a combination of guards and exception handling
        while(valid == false)
        {
            try
            {
                System.out.println("\nEnter Choice:\n");
                choice = SCANNER.nextInt();
                SCANNER.nextLine(); // consume the newline

                if(menuOptions.containsKey(choice)) // check if key in menuOptions is present
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid option chosen\n");
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("The value entered is illegal please try again");
                log.info("User has inputted invalid value into Menu");
                SCANNER.nextLine();
            }
        }

        quit = menuOptions.get(choice).get();

        return quit;
    }


    public static int quitApplication()
    {
        System.out.println("Goodbye!");
        return 1;
    }

    
    public static int placeStructure()
    {
        List<Object>[][] gStructure = App.getStructure();
        List<Object>[][] gBaseCity = App.getBaseCity();

        // below 2 lines are for debugging
        //App.displayCity(gBaseCity);
        //System.out.println("testing base city getter " + gBaseCity[0][0].get(0));

        Integer rows = gStructure.length; // initialize size values as size of baseCity
        Integer cols = gStructure[0].length;
        Integer locationX = 0; // location of placed structure
        Integer locationY = 0;
        boolean valid = false; // used for excpetion handling
        String baseCityString = "";

        //city values (default)
        String terrainType;
        String heritage = "";
        Double floodRisk = 0.0;
        Integer heightLimit = 255;
        boolean contamination = false;

        String loggingString; // A string just for logging so PMD doesnt complain


        // while loop is used for user input on coordinate location (with exception handling)
        while(valid == false)
        {
            try
            {
                System.out.println("Choose coordinates to place structure row (0-" + (rows -1) + ") cols (0-" + (cols - 1) + ")");


                locationX = SCANNER.nextInt();
                SCANNER.nextLine(); // consume the newline

                locationY = SCANNER.nextInt();
                SCANNER.nextLine(); // consume the newline

                if(locationX >= 0 && locationX < rows && locationY >= 0 && locationY < cols) // check if key in menuOptions is present
                {
                    valid = true;
                }
                else
                {
                    System.out.println("Invalid grid size\n");
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println("The value entered is illegal please try again");
                log.info("User has inputted invalid value into place structure");
                SCANNER.nextLine();
            }
        }


        // below block is used to retrive values from gBaseCity to later be parsed
        for (Object item : gBaseCity[locationX][locationY])
        {
            if (item instanceof Terrain)
            {
                baseCityString += ((Terrain) item).getDescription();
            }
            else
            {
                baseCityString += item.toString();
            }
        }

        // split parts by spaces
        String[] sections = baseCityString.split(" ");

        loggingString = "Checking retrieved string from baseCityString:" + baseCityString;
        log.info(loggingString); // final concatinated string

            
        // set terrain type from parsed value
        terrainType = sections[0]; // first part will always have terrain type

        // foreach loop takes care of optional decorator parsing into usable variables
        for(String part : sections)
        {
            if(part.equalsIgnoreCase("wood") || part.equalsIgnoreCase("stone") || part.equalsIgnoreCase("brick")) // checks heritage
            {
                heritage = part;
            }
            else if (part.matches("\\d+")) // look for non decimal int height limit
            {
                heightLimit = Integer.parseInt(part);
            }
            else if (part.matches("\\d+\\.\\d+")) // look for decimal for floodrisk
            {
                floodRisk = Double.parseDouble(part);
            }
            else if(part.equalsIgnoreCase("false") || part.equalsIgnoreCase("true")) // checks for contamination
            {
                contamination = Boolean.parseBoolean(part);
            }
        }


        setMaterials(locationX, locationY, gStructure, terrainType, heritage, floodRisk, heightLimit, contamination);

        return 0;
    }


    public static void setMaterials(Integer locationX, Integer locationY, List<Object>[][] gStructure, String terrainType, String heritage, Double floodRisk, Integer heightLimit, Boolean contamination)
    {
        //placed structure (user input)
        String foundationType = "";
        String constructionType = "";
        Integer numberOfFloors = 0;
        Boolean valid = false; // used for exception handling
        Boolean buildable = true; // buildable variable is used to check if structure can be built on a plot of land based on predifined rules

        String loggingString; // A string just for logging so PMD doesnt complain


        // user input blocks with validation

        while(!foundationType.equalsIgnoreCase("slab") && !foundationType.equalsIgnoreCase("stilts")) // checking user input for unknown values
        {
            System.out.println("\nEnter Structure Foundation type: (slab) or (stilts)\n");
            foundationType = SCANNER.nextLine();
        }

        while(!constructionType.equalsIgnoreCase("wood") && !constructionType.equalsIgnoreCase("stone") && !constructionType.equalsIgnoreCase("brick") && !constructionType.equalsIgnoreCase("concrete"))
        {
            System.out.println("Enter Construction Material: (wood), (stone), (brick) or (concrete)");
            constructionType = SCANNER.nextLine();
        }

        while(valid == false)
        {
            try
            {

                System.out.println("Enter number of floors");
                numberOfFloors = SCANNER.nextInt();
                SCANNER.nextLine(); // consume the newline

                if(numberOfFloors < 1)
                {
                    System.out.println("Cannot have less then 1 floor");
                }
                else
                {
                    valid = true; // if no exceptions happen the values is valid
                }

            }
            catch(InputMismatchException e) // catching non string input for Integer field
            {
                System.out.println("The value entered is illegal please try again");
                log.info("User has inputted invalid value into place structure");
                SCANNER.nextLine();
            }
        }
        

        loggingString = "checking values enterd by user foundationType = " + foundationType + ", constructionType = " + constructionType + ", numberOfFloors = " + numberOfFloors;
        log.info(loggingString);
       
        System.out.println("\n\n"); // add some new line breaks for readability
        
        // checking if structure is able to be placed
        if(foundationType.equals("slab") && terrainType.equals("swampy")) // invalid if terrain is swampy and foundation type is not a slab
        {
            buildable = false;
            System.out.println("Failed to build structure\nUnable to build slab foundation structure on swampy Terrain");
        }

        if(terrainType.equals("swampy") && constructionType.equals("wood")) // unable to build wood structures on swamp
        {
            buildable = false;
            System.out.println("Failed to build structure\nUnable to build wooden structures in swampy terrain");
        }

        if(!heritage.equals(constructionType) && !heritage.equals("")) // disables building of different types of construction materials if there is a heritage type
        {
            buildable = false;
            System.out.println("Failed to build structure\nHeritage type and construction type does not match");
        }

        if(numberOfFloors > heightLimit) // check if height limit is exceeded
        {
            buildable = false;
            System.out.println("Failed to build structure\nHeight limit exceeded");
        }

        if(buildable == true) // set values and call the setter for changes to take effect in parent container
        {
            gStructure[locationX][locationY].add(foundationType);
            gStructure[locationX][locationY].add(constructionType);
            gStructure[locationX][locationY].add(numberOfFloors);

            App.setStructure(gStructure);

            System.out.println("\n\nStructure Successfully Placed location on map");

            DisplayCityConfiguration.displayCity(gStructure);

            findCost(numberOfFloors, constructionType, terrainType, floodRisk, contamination); // method finds the cost of construction
        }
    }


    public static void findCost(Integer numberOfFloors, String constructionType, String terrainType, Double floodRisk, Boolean contamination)
    {
        Double cost = 0.0;
        Boolean floodRiskApplies = false;

        String loggingString; // A string just for logging so PMD doesnt complain

        if(numberOfFloors > 2) // check if flood risk should be applied
        {
            floodRiskApplies = true;
        }

        if(terrainType.equals("rocky")) // adds 50k flat rate to rocky terrain no matter number of floors
        {
            cost += 50000.0;
        }

        
        if(constructionType.equals("wood")) // find cost of floors based on construction material
        {
            cost = (numberOfFloors * 10000.0) + cost;
        }
        else if(constructionType.equals("concrete"))
        {
            cost = (numberOfFloors * 20000.0) + cost;
        }
        else if(constructionType.equals("brick"))
        {
            cost = (numberOfFloors * 30000.0) + cost;
        }
        else // stone
        {
            cost = (numberOfFloors * 50000.0) + cost;
        }

        
        if(terrainType.equals("swampy")) // add 20k cost per floor if terrain is swampy
        {
            cost = (numberOfFloors * 20000.0) + cost;
        }

        
        if(floodRiskApplies == true) // calculate prive if flood risk is relevent (if flood risk is 0 and flood risk still applies value will be unchanged)
        {
            cost = cost * (1 + (floodRisk / 50));
        }

        if(contamination == true) // apply contamination cost rule if it exists
        {
            cost = cost * 1.5;
        }


        System.out.println("The final price of the structure is $" + cost);


        loggingString = "Structure placed final cost of building is $" + cost;
        log.info(loggingString);


    }


    // this method creates an object depending on configuration of user input (STRATAGY PATTERN)
    //PMD warning supression for chooseCityConfiguration method
    // warning is supressed because the citybuilder is overridden and PMD thinks the initial assingment of random city configuration is unnessesary
    // initial configuration is necessary if this method is never called and user tries to build city straight away which has a safe default configuration
    // and stops the applicatino from crashing
    @SuppressWarnings("PMD.UnusedAssignment")
    public static int chooseCityConfiguration()
    {
        Boolean valid = false;
        Integer configurationChoice; 

        while(valid == false)
        {
            try
            {
                System.out.println("Choose city building option:\n1. Uniform\n2. Random\n3. Central");

                configurationChoice = SCANNER.nextInt();
                SCANNER.nextLine(); // consume \n

                if(configurationChoice == 1)  // block creates instances ob objects to be later called to be solved
                {
                    cityBuilder = new UniformCityConfiguration();
                    System.out.println("\nUniform city choice picked\n");
                }
                else if(configurationChoice == 3)
                {
                    cityBuilder = new CentralCityConfiguration();
                    System.out.println("\nCentral city configuration picked\n");
                }
                else // default value of choise 2
                {
                    cityBuilder = new RandomCityConfiguration();
                    System.out.println("\nRandom city choise picked\n");
                }

                valid = true;
            }
            catch(InputMismatchException e)
            {
                log.info("User enterd illegal input in Choose City Configuration");
                System.out.println("Illegal input please try again:\n");
                SCANNER.nextLine();
            }
        }

        return 0;
    }
}
