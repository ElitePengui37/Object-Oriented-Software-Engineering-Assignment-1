// Concrete class for rocky terrain (non decorator part) (decorator patten)
package edu.curtin.app; // dont know what this is but gradle came with it

public class RockyTerrain extends Terrain // Rocky terrain inherits form Terrain abstract class
{
    public RockyTerrain()
    {
        super("rocky");    // superclass is called and "rocky" is added to type
    }

    @Override
    public String getDescription()
    {
        return type; // returns "rocky"
    }
}