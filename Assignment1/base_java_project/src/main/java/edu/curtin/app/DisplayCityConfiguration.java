// this takes care of the same way all cities display the contents of gStructure

package edu.curtin.app;
import java.util.List;
import java.util.ArrayList;

public class DisplayCityConfiguration
{

    // this method takes care of displaying successfully placed structures on gStructure
    public static void displayCity(List<Object>[][] gStructure)
    {
        Integer totalStructuresBuilt = 0;
        System.out.println("\n\nFinal city Configuration\n|\n|\nV");

        // loop over each section and place an X if the list contained in gStructure is not empty
        for (List<Object>[] i : gStructure)
        {
            for (List<Object> j : i)
            {
                if(j.isEmpty() == false)
                {
                    System.out.print("X ");
                    totalStructuresBuilt++;
                }
                else
                {
                    System.out.print(". ");
                }
            }
            System.out.println(); // go to the next line
        }

        System.out.println("Total Structures Built: " + totalStructuresBuilt);
    }
}