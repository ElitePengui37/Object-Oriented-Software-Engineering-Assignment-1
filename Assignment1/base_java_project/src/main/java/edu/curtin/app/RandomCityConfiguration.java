// stratagy pattern way of placing a random city configuration
// no user input but a random number generator that randomly places stuff
// application tries to place it on every grid
// at the end show grid representation of where structures have been placed
// display total cost

package edu.curtin.app;
import java.util.Random; // import random library to allow for random structure types
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class RandomCityConfiguration implements CityBuilder // inherits CityBuilder interface
{

    private static final Logger log = Logger.getLogger(App.class.getName()); // imports logger object

    // not implemented yet just doing skeleton structure of stratagy pattern
    @Override
    public Integer placeCity()
    {
        Random rand = new Random(); // create random number generator object
        
        
        //placed structure (input chosen by random number generator)
        String foundationType;
        String constructionType;
        Integer numberOfFloors; // number of floors can be directly generated

        // values for random number generator
        Integer foundationTypeSeed;
        Integer constructionTypeSeed;

        List<Object>[][] gStructure = App.getStructure();
        List<Object>[][] gBaseCity = App.getBaseCity();

        Integer rows = gStructure.length; // initialize size values as size of baseCity
        Integer cols = gStructure[0].length;
        Double totalCityCost = 0.0; // initial city cost which will be added to

        String loggingString; // A string just for logging to avoid PMD complaining

        System.out.println("\n\nRandom city configuration chosen:\n");


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

                String baseCityString = ""; // base city string is reset every loop to prevent accidental string concatination

                
                
                
                //random number generator sets input variables
                numberOfFloors = rand.nextInt(255) + 1; // generates random Integer between 1 and 255 (+ 1 because it starts at 0)

                foundationTypeSeed = rand.nextInt(2); // generates random number between 0 and 1
                constructionTypeSeed = rand.nextInt(4); // generates random number between 0 and 3

                //set input values from random numbers generated
                if(foundationTypeSeed == 0) // setting foundation type
                {
                    foundationType = "slab";
                }
                else
                {
                    foundationType = "stilts";
                }


                if(constructionTypeSeed == 0) // setting construction type
                {
                    constructionType = "wood";
                }
                else if(constructionTypeSeed == 1)
                {
                    constructionType = "stone";
                }
                else if(constructionTypeSeed == 2)
                {
                    constructionType = "brick";
                }
                else
                {
                    constructionType = "concrete";
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

                buildable = checkValidity(gStructure, foundationType, terrainType, constructionType, heritage, numberOfFloors, heightLimit, locationX, locationY);

                if(buildable == true)
                {
                    totalCityCost = TotalCityCost.findCost(numberOfFloors, constructionType, terrainType, floodRisk, contamination, totalCityCost);
                }
            }
        }

        DisplayCityConfiguration.displayCity(gStructure); // display city configuration

        System.out.println("The total cost of the city is $ " + String.format("%.2f", totalCityCost)); // formatted display to stop scientific nottion

        loggingString = "Final city cost is $ " + totalCityCost;
        log.info(loggingString);

        return 0;
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