// Flood Risk Decorator
package edu.curtin.app; // dont know what this is but gradle came with it

public class FloodRiskDecorator extends ZoningDecorator
{
    private Double floodRisk;

    public FloodRiskDecorator(Terrain terrain, Double floodRisk)
    {
        super(terrain);
        this.floodRisk = floodRisk;
    }

    @Override
    public String getDescription()
    {
        return decoratedTarrain.getDescription() + " " + floodRisk; // space is added so when the values are retrieved they are concatinated into single string and can then be split
    }
}