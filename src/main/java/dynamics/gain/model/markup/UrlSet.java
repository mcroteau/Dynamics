package dynamics.gain.model.markup;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "urlset")
public class UrlSet {

    List<Url> url;

    public List<Url> getUrl() {
        return url;
    }

    @XmlElement
    public void setUrl(List<Url> url) {
        this.url = url;
    }
}
