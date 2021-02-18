package dynamics.gain.service;

import dynamics.gain.model.Location;
import dynamics.gain.model.markup.Url;
import dynamics.gain.model.markup.UrlSet;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MarshallService {

    public static final String BASE = "https://www.dynamicsgain.org/z/";
    public static final String SITEMAP = "src/main/webapp/sitemaps/locations.xml";

    public boolean out(List<Location> locations) throws JAXBException {
        UrlSet urlSet = new UrlSet();
        List<Url> urls = new ArrayList<>();
        for(Location location: locations){
            String loc = BASE + "locations/" + location.getLocationUri();
            Url url = new Url();
            url.setLoc(loc);
            url.setLastmod(new Date().toString());
            url.setPriority("1.0");
            urls.add(url);
        }

        urlSet.setUrl(urls);

        JAXBContext cntx = JAXBContext.newInstance(UrlSet.class);
        Marshaller marshaller = cntx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(urlSet, new File(SITEMAP));

        return true;
    }

}
