package com.learnjava.apiclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {

    protected String name;
    private List<String> topLevelDomain;
    protected String alpha2Code;
    private String alpha3Code;
    private List<String> callingCodes;
    protected String capital;
    private List<String> altSpellings;
    protected String region;
    protected String subregion;
    protected Integer population;
    private List<Double> latlng;
    private String demonym;
    private Double area;
    protected Double gini;
    private List<String> timezones;
    protected List<String> borders;
    protected String nativeName;
    private String numericCode;

}
