package com.crawljax.core.configuration.applications;

public enum ApplicationNames {

    DIMESHIFT ("dimeshift"),
    PAGEKIT ("pagekit"),
    PHOENIX ("phoenix"),
    SPLITTYPIE ("splittypie"),
    RETROBOARD ("retroboard"),
    NONE ("none");

    private final String applicationName;

    ApplicationNames(String applicationName){
        this.applicationName = applicationName;
    }

    public String value(){
        return this.applicationName;
    }
}
