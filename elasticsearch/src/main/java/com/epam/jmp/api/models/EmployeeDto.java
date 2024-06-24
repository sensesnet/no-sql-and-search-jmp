package com.epam.jmp.api.models;

import com.epam.jmp.models.Hit;
import com.epam.jmp.models.Source;

import java.util.List;

public class EmployeeDto {
    private String name;
    private String dob;
    private AddressDto address;
    private String email;
    private List<String> skills;
    private int experience;
    private double rating;
    private String description;
    private boolean verified;
    private int salary;

    private String id;

    public EmployeeDto() {
    }


    public EmployeeDto(String name,
                       String dob,
                       AddressDto address,
                       String email,
                       List<String> skills,
                       int experience,
                       double rating,
                       String description,
                       boolean verified,
                       int salary, String id) {

        this.name = name;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.skills = skills;
        this.experience = experience;
        this.rating = rating;
        this.description = description;
        this.verified = verified;
        this.salary = salary;
        this.id = id;
    }

    public static EmployeeDto createFromHit(Hit hit) {
        var source = hit.get_source();
        return new EmployeeDto(source.getName(),
                source.getDob(),
                new AddressDto(source.getAddress().getCountry(), source.getAddress().getTown()),
                source.getEmail(),
                source.getSkills(),
                source.getExperience(),
                source.getRating(),
                source.getDescription(),
                source.isVerified(),
                source.getSalary(), hit.get_id());
    }

    public static EmployeeDto createFromSource(Source source, String id) {
        return new EmployeeDto(source.getName(),
                source.getDob(),
                new AddressDto(source.getAddress().getCountry(), source.getAddress().getTown()),
                source.getEmail(),
                source.getSkills(),
                source.getExperience(),
                source.getRating(),
                source.getDescription(),
                source.isVerified(),
                source.getSalary(), id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "EmployeeDto{" +
                "name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", address=" + address +
                ", email='" + email + '\'' +
                ", skills=" + skills +
                ", experience=" + experience +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", verified=" + verified +
                ", salary=" + salary +
                ", id='" + id + '\'' +
                '}';
    }
}
