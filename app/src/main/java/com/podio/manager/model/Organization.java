package com.podio.manager.model;

import java.util.Comparator;
import java.util.List;

public class Organization extends ItemListRow implements Comparator<Organization>, Comparable<Organization> {

    protected List<Workspace> workspaceList;

    public Organization(){

    }

    public Organization(int sectionFirstPosition) {
        super(sectionFirstPosition);
    }

    public List<Workspace> getWorkspaceList() {
        return workspaceList;
    }

    public void setWorkspaceList(List<Workspace> workspaceList) {
        this.workspaceList = workspaceList;
    }

    //TODO Set the other Organization fields...

    @Override
    public int compareTo(Organization another) {
        return (this.name).compareTo(another.name);
    }

    @Override
    public int compare(Organization lhs, Organization rhs) {
        return (lhs.name).compareTo(rhs.name);
    }

}
