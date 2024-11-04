// base class Abstract for generic terrain type (for decorator pattern) 
package edu.curtin.app; // dont know what this is but gradle came with it

public abstract class Terrain
{
    protected String type;

    public Terrain(String type)
    {
        this.type = type;
    }

    public abstract String getDescription();
}