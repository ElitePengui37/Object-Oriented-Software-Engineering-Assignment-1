// decorator class for heritage type (wood, stone or brick)
package edu.curtin.app; // dont know what this is but gradle came with it

public class HeritageDecorator extends ZoningDecorator
{
    private String heritageType;

    public HeritageDecorator(Terrain terrain, String heritageType)
    {
        super(terrain);
        this.heritageType = heritageType;
    }

    @Override
    public String getDescription()
    {
        return decoratedTarrain.getDescription() + " " + heritageType; // returns either wood, stone or brick with terrain type being behind it
        // space is added so when the values are retrieved they are concatinated into single string and can then be split
    }

}