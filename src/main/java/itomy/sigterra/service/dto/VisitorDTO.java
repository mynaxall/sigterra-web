package itomy.sigterra.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Visitor entity.
 */
public class VisitorDTO implements Serializable {

    private Long id;

    @NotNull
    private String ip;

    @NotNull
    private String userAgent;

    private String country;

    private String city;

    @NotNull
    private LocalDate createdDate;


    private Long userId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
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
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VisitorDTO visitorDTO = (VisitorDTO) o;

        if ( ! Objects.equals(id, visitorDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VisitorDTO{" +
            "id=" + id +
            ", ip='" + ip + "'" +
            ", userAgent='" + userAgent + "'" +
            ", country='" + country + "'" +
            ", city='" + city + "'" +
            ", createdDate='" + createdDate + "'" +
            '}';
    }
}
