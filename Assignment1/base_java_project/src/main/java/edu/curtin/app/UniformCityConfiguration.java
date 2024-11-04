// stratagy pattern way of placing a uniform city configuration
// user is prompted building stuff
// application tries to place it on every grid
// at the end show grid representation of where structures have been placed
// display total cost

package edu.curtin.app;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;

public class UniformCityConfiguration implements CityBuilder // inherits CityBuilder interface
{

    private static final Logger log = Logger.getLogger(App.class.getName()); // imports logger object
    private static final Scanner SCANNER = new Scanner(System.in);

    // takes user input with validation to be used as the base to place city
    @Override
    public Integer placeCity()
    {
        //placed structure (user input)
        String foundationType = "";
        String constructionType = "";
        Integer numberOfFloors = 0;
        Boolean valid = false; // used for exception handling

        String loggingString; // A string just for logging so PMD doesnt complain


        System.out.println("\n\n\nUniform city configuration chosen:\n");

        // user input blocks with validation block

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

        parseTerrainInfo(foundationType, constructionType, numberOfFloors);

        return 0;
    }




    // parses Terrain string and calls other methods to check set and calculate cost of structure
    public static void parseTerrainInfo(String foundationType, String constructionType, Integer numberOfFloors)
    {
        List<Object>[][] gStructure = App.getStructure();
        List<Object>[][] gBaseCity = App.getBaseCity();


        Integer rows = gStructure.length; // initialize size values as size of baseCity
        Integer cols = gStructure[0].length;
        Double totalCityCost = 0.0; // initial city cost which will be added to

        String loggingString; // A string just for logging so PMD doesnt complain


        // parsing part block
        for(Integer locationX = 0; locationX < rows; locationX++)
        {
            for(Integer locationY = 0; locationY < cols; locationY++)
            {
                //city values (default) reset for every passthrough back to default
                String terrainType;
                String heritage = "";
                Double floodRisk = 0.0;
                Integer heightLimit = 255;
                Boolean contamination = false;
                Boolean buildable;

                String baseCityString = ""; // base city string is reseat every loop to prevent accidental string concatination


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

                buildable = checkValidity(gStructure, foundationType, terrainType, constructionType, heritage, numberOfFloors, heightLimit, locationX, locationY);

                if(buildable == true) // calculate cost if buildable has returned true
                {
                    totalCityCost = TotalCityCost.findCost(numberOfFloors, constructionType, terrainType, floodRisk, contamination, totalCityCost);
                }
            }
        }

        DisplayCityConfiguration.displayCity(gStructure); // display city configuration

        System.out.println("The total cost of the city is $ " + String.format("%.2f", totalCityCost)); // formatted display to stop scientific nottion

        loggingString = "Final city cost is $ " + totalCityCost;
        log.info(loggingString);
    }





    // checks if stucture is able to be placed if yes its added to gStructure and cost of structure it found (called several times and added)
    public static Boolean checkValidity(List<Object>[][] gStructure, String foundationType, String terrainType, String constructionType, String heritage, Integer numberOfFloors, Integer heightLimit, Integer locationX, Integer locationY)
    {
        Boolean buildable = true;

        //System.out.println("debugging statement foundation type: " + foundationType + " Terrain type: " + terrainType + " construction type: " + constructionType + " heritage: " + heritage + " number of floors: " + numberOfFloors + " height limit: " + heightLimit + " location x and y: " + locationX + ", " + locationY);
        
        // checking if structure is able to be placed
        if(foundationType.equals("slab") && terrainType.equals("swampy")) // invalid if terrain is swampy and foundation type is not a slab
        {
            buildable = false;
        }

        if(terrainType.equals("swampy") && constructionType.equals("wood")) // unable to build wood structures on swamp
        {
            buildable = false;
        }

        if(!heritage.equals(constructionType) && !heritage.equals("")) // disables building of different types of construction materials if there is a heritage type
        {
            buildable = false;
        }

        if(numberOfFloors > heightLimit) // check if height limit is exceeded
        {
            buildable = false;
        }

        if(buildable == true) // set values and call the setter for changes to take effect in parent container
        {
            gStructure[locationX][locationY].add(foundationType);
            gStructure[locationX][locationY].add(constructionType);
            gStructure[locationX][locationY].add(numberOfFloors);

            App.setStructure(gStructure);
        }

        return buildable;
    }
}