package fr.scc.saillie.icadapi;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import fr.scc.saillie.geniteur.error.GeniteurException;
import fr.scc.saillie.geniteur.model.Geniteur;
import fr.scc.saillie.geniteur.spi.IcadInventory;
import fr.scc.saillie.geniteur.utils.DateUtils;
import fr.scc.saillie.icadapi.model.IcadResponse;

@Component
public class IcadClient implements IcadInventory {

    private final RestTemplate restTemplate;

    @Value("${icad.base-uri}")
    private String icadBaseUri;

    @Value("${icad.login}")
    private String login;

    @Value("${icad.password}")
    private String password;

    public IcadClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Geniteur byIdentifiant(String tatouage, String puce) throws GeniteurException {
        Geniteur geniteur = new Geniteur(0, null);

        var icadResponse = getGeniteurFromIcad(tatouage, puce);
        geniteur.setDateDeces(DateUtils.convertIcadStringToLocalDate(icadResponse.getDateDeces()));
        return geniteur;
    }

    private IcadResponse getGeniteurFromIcad(String tatouage, String puce) {
        // http://srvprodtraefik/wslo.i-cad.fr/requete_lof.php?l=[login]&m=[password]&ci=[puce]&ct=[tatouage]
        icadBaseUri += "l="+ login;
        icadBaseUri += "&m="+ password;
        icadBaseUri += "&ci="+ (puce == null ? "" : puce);
        icadBaseUri += "&ct=" + (tatouage == null ? "" : tatouage);
        return restTemplate.getForObject(icadBaseUri, IcadResponse.class);
    }

}
