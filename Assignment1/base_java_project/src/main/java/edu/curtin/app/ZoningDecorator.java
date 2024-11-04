// Base Decorator Abstract class for the "Optional" zoning rules such as (heritage, height limit, flood risk and/or contamination)

package edu.curtin.app; // dont know what this is but gradle came with it

public abstract class ZoningDecorator extends Terrain
{
    protected Terrain decoratedTarrain; // this variable holds the instance of the terrain being deorated

    public ZoningDecorator(Terrain terrain)
    {
        super(terrain.type);
        this.decoratedTarrain = terrain;
    }

    @Override
    public abstract String getDescription();

}