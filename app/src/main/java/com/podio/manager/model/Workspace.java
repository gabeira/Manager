package com.podio.manager.model;

import java.util.Comparator;

public class Workspace extends ItemListRow implements Comparator<Workspace>, Comparable<Workspace> {

    public Workspace(){
    }

    public Workspace(int sectionFirstPosition) {
        super(sectionFirstPosition);
    }

    //TODO Set the other Workspace fields...

    @Override
    public int compareTo(Workspace another) {
        return (this.name).compareTo(another.name);
    }

    @Override
    public int compare(Workspace lhs, Workspace rhs) {
        return (lhs.name).compareTo(rhs.name);
    }
}