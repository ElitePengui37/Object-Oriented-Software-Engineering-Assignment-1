// stratagy pattern way of placing a Central city configuration
// use math formula to place specific strucutres from the centre
// application tries to place it on every grid
// at the end show grid representation of where structures have been placed
// display total cost


// foundation type is always slab
// number of floors calculated from math forumla shown on assingment

package edu.curtin.app;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class CentralCityConfiguration implements CityBuilder // inherits CityBuilder interface
{
    private static final Logger log = Logger.getLogger(App.class.getName()); // imports logger object

    // not implemented yet just doing skeleton structure of stratagy pattern
    @Override
    public Integer placeCity()
    {
        System.out.println("\nCentral City Configuration Chosen\n\n");

        //placed structure (input chosen by distance rules)
        String foundationType = "slab";
        String constructionType;
        Integer numberOfFloors; // number of floors can be directly generated

        List<Object>[][] gStructure = App.getStructure();
        List<Object>[][] gBaseCity = App.getBaseCity();

        Integer rows = gStructure.length; // initialize size values as size of baseCity
        Integer cols = gStructure[0].length;
        Double totalCityCost = 0.0; // initial city cost which will be added to

        String loggingString; // a variable used just for logging to prevent PMD from complaining about passing variables


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


                Double heightCalc;
                Double widthCalc;
                Double distance;
                
                
                // central calculation broken down based on assingment specification
                heightCalc = (double) locationY - ((cols - 1) / 2);
                widthCalc = (double) locationX - ((rows - 1) / 2);

                heightCalc = Math.pow(heightCalc, 2);
                widthCalc = Math.pow(widthCalc, 2);

                distance = Math.sqrt(heightCalc + widthCalc);

                // number of floors based on assingment specification
                numberOfFloors = (int) Math.round(1 + (20.0 / (distance + 1))); // extra set of brackets to prevent invalid floors from being passed

                
                //calculate distribuition
                if(distance <= 2.0)
                {
                    constructionType = "concrete";
                }
                else if (distance > 2 && distance <= 4)
                {
                    constructionType = "brick";
                }
                else if(distance > 4 && distance <= 6)
                {
                    constructionType = "stone";
                }
                else
                {
                    constructionType = "wood";
                }

                
                log.info("Calculations Dump");
                loggingString = "Distance weight: " + distance + " Construction Type: " + constructionType + " Number of Floors: " + numberOfFloors;
                log.info(loggingString);
                



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

                buildable = checkValidity(foundationType, terrainType, constructionType, heritage, numberOfFloors, heightLimit, locationX, locationY);

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
    public static Boolean checkValidity(String foundationType, String terrainType, String constructionType, String heritage, Integer numberOfFloors, Integer heightLimit, Integer locationX, Integer locationY)
    {
        List<Object>[][] gStructure = App.getStructure();
        Boolean buildable = true;

        //System.out.println("debugging statement foundation type: " + foundationType + " Terrain type: " + terrainType + " construction type: " + constructionType + " heritage: " + heritage + " number of floors: " + numberOfFloors + " height limit: " + heightLimit + " location x and y: " + locationX + ", " + locationY);
        
        // checking if structure is able to be placed (Some rules have been removed as you cannot build on swamp anymore as foundation is always slab)
        if(terrainType.equals("swampy")) // terrain type is always invalid in this case because the foundation type is always a slab
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