package itomy.sigterra.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TabType.
 */
@Entity
@Table(name = "tab_type")
public class TabType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "path")
    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TabType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public TabType type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public TabType path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TabType tabType = (TabType) o;
        if(tabType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tabType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TabType{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", type='" + type + "'" +
            ", path='" + path + "'" +
            '}';
    }
}
