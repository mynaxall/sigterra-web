package itomy.sigterra.service.dto;

import java.io.Serializable;

/**
 * Created by alexander on 1/31/17.
 */
public class BusinessSocialLinks implements Serializable {
    private String twitter;
    private String facebook;
    private String google;
    private String linkedin;

    public BusinessSocialLinks() {
    }

    public BusinessSocialLinks(String twitter, String facebook, String google, String linkedin) {
        this.twitter = twitter;
        this.facebook = facebook;
        this.google = google;
        this.linkedin = linkedin;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    @Override
    public String toString() {
        return "BusinessSocialLinks{" +
            "twitter='" + twitter + '\'' +
            ", facebook='" + facebook + '\'' +
            ", google='" + google + '\'' +
            ", linkedin='" + linkedin + '\'' +
            '}';
    }
}
