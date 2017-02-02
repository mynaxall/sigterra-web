package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A InputProperties.
 */
@Entity
@Table(name = "input_properties")
public class InputProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "bold")
    private Boolean bold;

    @Column(name = "italic")
    private Boolean italic;

    @Column(name = "underline")
    private Boolean underline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public InputProperties value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean isBold() {
        return bold;
    }

    public InputProperties bold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean isItalic() {
        return italic;
    }

    public InputProperties italic(Boolean italic) {
        this.italic = italic;
        return this;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean isUnderline() {
        return underline;
    }

    public InputProperties underline(Boolean underline) {
        this.underline = underline;
        return this;
    }

    public void setUnderline(Boolean underline) {
        this.underline = underline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InputProperties inputProperties = (InputProperties) o;
        if(inputProperties.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, inputProperties.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "InputProperties{" +
            "id=" + id +
            ", value='" + value + "'" +
            ", bold='" + bold + "'" +
            ", italic='" + italic + "'" +
            ", underline='" + underline + "'" +
            '}';
    }
}
