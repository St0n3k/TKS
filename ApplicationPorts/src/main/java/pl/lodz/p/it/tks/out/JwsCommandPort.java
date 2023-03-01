package pl.lodz.p.it.tks.out;

import pl.lodz.p.it.tks.exception.security.JwsException;

public interface JwsCommandPort {
    String sign(String payload) throws JwsException;
    boolean verify(String ifMatch, String payload) throws JwsException;
}
