// Height Limit Decorator
package edu.curtin.app; // dont know what this is but gradle came with it

public class HeightLimitDecorator extends ZoningDecorator
{
    private Integer heightLimit;

    public HeightLimitDecorator(Terrain terrain, Integer heightLimit)
    {
        super(terrain);
        this.heightLimit = heightLimit;
    }

    @Override
    public String getDescription()
    {
        return decoratedTarrain.getDescription() + " " + heightLimit; // space is added so when the values are retrieved they are concatinated into single string and can then be split
    }
}