package com.podio.manager.model;

public class ItemListRow {

    protected int sectionFirstPosition;
    protected String id;
    protected String name;

    public ItemListRow(){
    }
    public ItemListRow(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}