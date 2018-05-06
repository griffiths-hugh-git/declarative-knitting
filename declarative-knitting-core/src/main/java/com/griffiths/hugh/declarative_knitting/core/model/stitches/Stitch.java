package com.griffiths.hugh.declarative_knitting.core.model.stitches;

import java.util.List;

public class Stitch {
    private final List<Loop> parents;
    private final String type;
    private final List<Loop> children;
    private int colour;

    public Stitch(List<Loop> parents, String type, List<Loop> children) {
        this.parents=parents;
        this.type = type;
        this.children = children;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public String getType() {
        return type;
    }

    public List<Loop> getParents() {
        return parents;
    }

    public List<Loop> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return type;
    }
}
