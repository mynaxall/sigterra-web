package itomy.sigterra.domain;

import com.google.common.base.Objects;

public class Location {

    private String country;
    private String city;

    public Location(String country, String city) {
        this.country = country;
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location that = (Location) o;
        return Objects.equal(country, that.country) &&
            Objects.equal(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(country, city);
    }

    @Override
    public String toString() {
        return "Location{" +
            "country='" + country + '\'' +
            ", city='" + city + '\'' +
            '}';
    }
}
