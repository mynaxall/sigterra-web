package itomy.sigterra.service.dto;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class InputModel implements Serializable {
    private String value;
    private Boolean bold;
    private Boolean italic;
    private Boolean underline;

    public InputModel() {
    }

    public InputModel(String value, Boolean bold, Boolean italic, Boolean underline) {
        this.value = value;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public Boolean getItalic() {
        return italic;
    }

    public void setItalic(Boolean italic) {
        this.italic = italic;
    }

    public Boolean getUnderline() {
        return underline;
    }

    public void setUnderline(Boolean underline) {
        this.underline = underline;
    }

    @Override
    public String toString() {
        return "InputModel{" +
            "value='" + value + '\'' +
            ", bold=" + bold +
            ", italic=" + italic +
            ", underline=" + underline +
            '}';
    }
}
