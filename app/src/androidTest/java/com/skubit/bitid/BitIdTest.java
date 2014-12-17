package com.skubit.bitid;

import junit.framework.TestCase;

import java.net.URISyntaxException;

public class BitIdTest extends TestCase {

    public void testNullValueThrowExceptionForParse() throws URISyntaxException {
        try {
            BitID.parse(null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testValidUriParses() throws URISyntaxException {
        String uri = "bitid://www.site.com/callback?x=NONCE";
        BitID.parse(uri);
    }

    public void testNonce() throws URISyntaxException {
        String uri = "bitid://www.site.com/callback?x=NONCE123";
        BitID bitId = BitID.parse(uri);
        assertEquals("NONCE123", bitId.getNonce());
    }

    public void testIsSecuredDefault() throws URISyntaxException {
        String uri = "bitid://www.site.com/callback?x=NONCE";
        assertEquals(true, BitID.parse(uri).isSecured());
    }

    public void testIsSecuredConfigured() throws URISyntaxException {
        String uri = "bitid://www.site.com/callback?x=NONCE&u=0";
        assertEquals(true, BitID.parse(uri).isSecured());
    }

    public void testIsUnSecured() throws URISyntaxException {
        String uri = "bitid://www.site.com/callback?x=NONCE&u=1";
        assertEquals(false, BitID.parse(uri).isSecured());
    }

    public void testMissingIllegalUParamThrowsException() {
        String uri = "bitid://www.site.com/callback?x=NONCE&u=2";
        try {
            BitID.parse(uri);
            fail("Should have thrown URISyntaxException");
        } catch (URISyntaxException e) {
        }
    }

    public void testMissingXThrowsException() {
        String uri = "bitid://www.site.com/callback?a=NONCE";
        try {
            BitID.parse(uri);
            fail("Should have thrown URISyntaxException");
        } catch (URISyntaxException e) {
        }
    }

    public void testInvalidSchemeThrowsException() {
        String uri = "http://www.site.com/callback?x=NONCE";
        try {
            BitID.parse(uri);
            fail("Should have thrown URISyntaxException");
        } catch (URISyntaxException e) {
        }
    }
}
