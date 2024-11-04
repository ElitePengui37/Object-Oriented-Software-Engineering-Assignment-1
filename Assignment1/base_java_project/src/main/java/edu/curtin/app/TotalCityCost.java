// this file contains a common method used to find the total cost of the city

// this method in this class is a slightly modified version of the findCost method found in Menu.java


package edu.curtin.app; 

public class TotalCityCost
{

    // modified version of findCost in Menu class that allows this method to have persistant cost between strucutres
    public static Double findCost(Integer numberOfFloors, String constructionType, String terrainType, Double floodRisk, Boolean contamination, Double totalCityCost)
    {
        Boolean floodRiskApplies = false;
        Double cost = 0.0;
        Double addedCityCost = totalCityCost; // update current cost


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

        cost = cost + addedCityCost; // add cost to previous costs

        return cost;
    }
}