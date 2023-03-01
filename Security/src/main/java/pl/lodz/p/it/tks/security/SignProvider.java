package pl.lodz.p.it.tks.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.tks.exception.security.JwsException;
import pl.lodz.p.it.tks.out.JwsCommandPort;

import java.text.ParseException;

@ApplicationScoped
public class SignProvider implements JwsCommandPort {

    @Inject
    @ConfigProperty(name = "pl.lodz.pas.security.jws.secret")
    private String secret;


    public String sign(String payload) throws JwsException {
        try{
            JWSSigner signer = new MACSigner(secret);
            JWSObject jws = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));
            jws.sign(signer);
            return jws.serialize();
        } catch(JOSEException e) {
            throw new JwsException();
        }
    }


    public boolean verify(String ifMatch, String payload) throws JwsException{
        try{
            JWSObject jws = JWSObject.parse(ifMatch);
            JWSVerifier jwsVerifier = new MACVerifier(secret);

            if (!jws.verify(jwsVerifier)) {
                return false;
            }

            String signedRequestObject = sign(payload);
            return ifMatch.equals(signedRequestObject);
        } catch(JOSEException | ParseException e) {
            throw new JwsException();
        }
    }
}
