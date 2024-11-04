// Concrete class for swampy terrain (non decorator part) (decorator patten)
package edu.curtin.app; // dont know what this is but gradle came with it

public class SwampyTerrain extends Terrain // Swampy terrain inherits form Terrain abstract class
{
    public SwampyTerrain()
    {
        super("swampy");    // superclass is called and "swampy" is added to type
    }

    @Override
    public String getDescription()
    {
        return type; // returns "swampy"
    }
}