package itomy.sigterra.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CardletTestimonialWidget.
 */
@Entity
@Table(name = "cardlet_testimonial_widget")
public class CardletTestimonialWidget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "co_name")
    private String coName;

    @Column(name = "designation")
    private String designation;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnore
    private Cardlet cardlet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoName() {
        return coName;
    }

    public void setCoName(String coName) {
        this.coName = coName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cardlet getCardlet() {
        return cardlet;
    }

    public void setCardlet(Cardlet cardlet) {
        this.cardlet = cardlet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CardletTestimonialWidget cardletTestimonialWidget = (CardletTestimonialWidget) o;
        if (cardletTestimonialWidget.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cardletTestimonialWidget.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CardletTestimonialWidget{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", coName='" + coName + '\'' +
            ", designation='" + designation + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
