package itomy.sigterra.domain;

import javax.persistence.*;

@Entity
@Table(name = "geoip_location")
public class GeoIpLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "ip_from")
    private long ipFrom;

    @Column(name = "ip_to")
    private long ipTo;

    @Column(name = "country_name")
    private String country;

    @Column(name = "city_name")
    private String city;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIpFrom() {
        return ipFrom;
    }

    public void setIpFrom(long ipFrom) {
        this.ipFrom = ipFrom;
    }

    public long getIpTo() {
        return ipTo;
    }

    public void setIpTo(long ipTo) {
        this.ipTo = ipTo;
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
    public String toString() {
        return "GeoIpLocation{" +
            "id=" + id +
            ", ipFrom=" + ipFrom +
            ", ipTo=" + ipTo +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            '}';
    }
}
