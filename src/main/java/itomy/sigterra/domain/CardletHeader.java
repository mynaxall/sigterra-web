package itomy.sigterra.domain;


import javax.persistence.*;

@Entity
@Table(name="cardlet_header")
public class CardletHeader extends AbstractAuditingEntity{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "cta_text")
    private String ctaText;

    @Column(name = "logo")
    private String logo;

    @Column(name = "photo")
    private String photo;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "company")
    private String company;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;


}
