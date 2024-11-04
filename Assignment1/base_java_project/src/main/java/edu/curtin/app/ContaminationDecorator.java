// Contamination Decorator
package edu.curtin.app; // dont know what this is but gradle came with it

public class ContaminationDecorator extends ZoningDecorator
{
    private Boolean contaminated;

    public ContaminationDecorator(Terrain terrain, Boolean contaminated)
    {
        super(terrain);
        this.contaminated = contaminated;
    }

    @Override
    public String getDescription()
    {
        return decoratedTarrain.getDescription() + " " + contaminated; // space is added so when the values are retrieved they are concatinated into single string and can then be split
    }
}