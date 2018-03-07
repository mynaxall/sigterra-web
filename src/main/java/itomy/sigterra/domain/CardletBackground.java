package itomy.sigterra.domain;

import javax.persistence.*;

@Entity
@Table(name = "cardler_background")
public class CardletBackground extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "image")
    private String image;

    //user can choose only black or white colour for the text Under the cardlet.
    @Column(name="text_color")
    private boolean textColor;

    @Column(name = "caption_text")
    private String captionText;


}
