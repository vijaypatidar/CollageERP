package com.svceindore.uiservice.utils;

import org.springframework.stereotype.Component;

/**
 * Created by Vijay Patidar
 * Date: 01/02/21
 * Time: 11:37 AM
 **/
@Component
public class DataUtils {
    private String[] states = new String[]{
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chhattisgarh",
            "Goa",
            "Gujarat",
            "Haryana",
            "Himachal Pradesh",
            "Jammu and Kashmir",
            "Jharkhand",
            "Karnataka",
            "Kerala",
            "Madhya Pradesh",
            "Maharashtra",
            "Manipur",
            "Meghalaya",
            "Mizoram",
            "Nagaland",
            "Odisha",
            "Punjab",
            "Rajasthan",
            "Sikkim",
            "Tamil Nadu",
            "Telangana",
            "Tripura",
            "Uttarakhand",
            "Uttar Pradesh",
            "West Bengal",
            "Andaman and Nicobar Islands",
            "Chandigarh",
            "Dadra and Nagar Haveli",
            "Daman and Diu",
            "Delhi",
            "Lakshadweep",
            "Puducherry"
    };

    private String[] countries = new String[]{
            "India",
            "Nepal",
            "America",
            "other"
    };

    public String[] getCountries() {
        return countries;
    }

    public String[] getStates() {
        return states;
    }
}
