package com.epam.jmp.api.models;

public class AddressDto {
    private String country;
    private String town;

    public AddressDto() {
    }

    public AddressDto(String country, String town) {
        this.country = country;
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "AddressDto{" +
                "country='" + country + '\'' +
                ", town='" + town + '\'' +
                '}';
    }
}
