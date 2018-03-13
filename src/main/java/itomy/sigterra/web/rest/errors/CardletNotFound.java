package itomy.sigterra.web.rest.errors;

//exception: not found cardlet
// generates return code 404 (see ExceptionTranslator.class)
public class CardletNotFound extends RuntimeException{
    private Long cardletId;

    private String message;

    public CardletNotFound(Long cardletId) {
        this.cardletId = cardletId;
        this.message = "Cardlet with id:"+cardletId+" not found";
    }

    @Override
    public String getMessage() {
        return message;
    }

}
