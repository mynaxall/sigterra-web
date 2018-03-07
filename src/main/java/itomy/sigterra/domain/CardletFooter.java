package itomy.sigterra.domain;

import itomy.sigterra.domain.enumeration.CardletFooterIndex;

import javax.persistence.*;

@Entity
@Table(name = "cardlet_footer")
public class CardletFooter extends AbstractAuditingEntity{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "index",columnDefinition = "smallint")
    @Enumerated
    private CardletFooterIndex index;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "logo")
    private String logo;


}
