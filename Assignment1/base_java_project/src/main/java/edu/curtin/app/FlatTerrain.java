// Concrete class for flat terrain (non decorator part) (decorator patten)
package edu.curtin.app; // dont know what this is but gradle came with it

public class FlatTerrain extends Terrain // Flat terrain inherits form Terrain abstract class
{
    public FlatTerrain()
    {
        super("flat");    // superclass is called and "flat" is added to type
    }

    @Override
    public String getDescription()
    {
        return type; // returns "flat"
    }
}