package model;

import java.util.ArrayList;
import java.util.List;

// TOP LAYER OF METRİCS
// represents a complete measurement scenario with its quality dimensions.
 
// example object: new Scenario("Scenario C - Team Alpha")



public class Scenario {
    private final String name;
    private final List<Dimension> dimensions;

    public Scenario(String name) {
        this.name = name;
        this.dimensions = new ArrayList<>();
    }

    public void addDimension(Dimension dimension) {
        dimensions.add(dimension);
    }

    public String getName() {
        return name;
    }

    public List<Dimension> getDimensions(){
        return dimensions;
    }
}
