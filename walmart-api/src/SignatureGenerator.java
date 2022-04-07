//package com.walmart.platform.common;

import java.io.ObjectStreamException;
import java.security.KeyRep;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.codec.binary.Base64;

public class SignatureGenerator {

    public static void main(String[] args) {
        SignatureGenerator generator = new SignatureGenerator();

        String consumerId = "870dc509-9b4c-451c-bfc6-b3c11622d618";
        String privateKeyVersion = "1";
        String key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDUYNSPPb7odfl/\n" +
                "MO8r0Vof4RxIf5SYy4D091wdXa7G7yyqIhmfA19CLA0pTcvWME2EKQjx3aIB8UZ8\n" +
                "XX17iBO/fhRkZ6ZxBXLUnC/aUM6Ba7ENWB6zumjqX3+ExXJruWERBAyxh2RjFGH3\n" +
                "NZAzQ+acqGQRN/QSNqEbVGPx1pQ4snMeS5PPI6HgF+ELpGZodv0uLZsjb0cKYIqw\n" +
                "Mv3eHzA4QPJSaKee641NMi13fqhCgrIkrYv28m7mE7x3AiNrTfqt+Xl5BZkduDlL\n" +
                "0ClsKifejhILhB96CCf++cFPfCsIkY2aAbsAbgjcqfO+msVJsN6MvglN0wzD+EwT\n" +
                "GCmCSgRTAgMBAAECggEAQod6zsx263dZiyCXK9DPPMFWDNf6gsrtQM/kSUa5o5P1\n" +
                "F+7Ku+dghegqz0+RlS1YexgntyzIvXcJr5fR4UrFdO1YEgPuPeWJDVVIIEnEBwLn\n" +
                "WdjK9V5l01XLKpp3t4tA/wWXvF0/c1JFRh2/aG/S5CxT/JjcmoAGg/sxu+aOVBvw\n" +
                "d9G0xaBUScxAGwTyAd4qj//Mn4owXUiuc/jHLIr7L+bF6Od5E7aa/FqfJIxLbIln\n" +
                "HNhd68Oi25yjqjkrzQqEPGgG70lsK5FtpFW/hUITOhUKi14RN/hDlrtb65gw8tBr\n" +
                "oY3wC08DgtGWia9zwdq0WPw/sx1o+bRzNuLczzH0gQKBgQD0cmjCHz74AXII2gvY\n" +
                "uywF6r8ljvLS+HObQVQ4kbobxZ0KEKUevjlqNmxHiJnxqw1aylr/34hvl/8eyVnv\n" +
                "ppyGAx8phsWF2Xb0eg3WWUEZ03v998v7GccXV1UChol00p1XG1P1aaJs0kKt9nfj\n" +
                "uTumvGbHKm1ZZVn3c1UdEYwTIQKBgQDeamstxUb5f6xHrkDhMBioh0+tOjiriosA\n" +
                "rsx53baPL+dYT/Y8cxFFZKmsYlGSpN2EKlI2pf8ZuZMKk6KXMqu0LQG5bB+PubLA\n" +
                "a6p8CUn2Uv3es8VaPnfWPCtInI4ZwVJT2TaRYD2FbEaV3s5YLvCwZffVL+2EmEjt\n" +
                "dZBgSPRc8wKBgQDsWWqctyVlGdCualUl+uN33R9wKyTxgzQUGzhZcd1mWM2cG7fr\n" +
                "X7WX0oMjLxrZpa/F5v3RhMogS/BqTfjdVFTm5kHNHvgAkOEJRLH4XFgrpmWui1b4\n" +
                "tFXhZZlWVcq136YtwMiO8tIPMcUy+qm0zzaz4lnlnEoaM3qnXSYgcwTiwQKBgEOL\n" +
                "sBcLZX2aD3kJa3y3/ZUY2tKx7snx3jsL2t9bvPiJRMnmJtK+40zewESw6zMMzJU5\n" +
                "wRQFn45xdD1vDp66bbjfPjnQo1kjvSyAxhq7O3IKTD0Y/WNlu0EdrstzeBhMRLNg\n" +
                "MRk/UA+4DUuwiZvwTXpUzUo6LvjowqUS4hDLtyKPAoGAL07KaAuREjgaTxWpwhvj\n" +
                "PugQ7vZcZN5aaZr120WqnxWPi+6WUi99TkXcUrWZ4zlGeNKsPyhzCtBDZQKeZhGe\n" +
                "riRNju+wTClkSP72jpMv6rhe+PD63cr8ewqxzazF7h66y+173Hl1uFTAxtmbMZCE\n" +
                "K8qtHxA/CeawaqstP4vqHi4=";

        long intimestamp = System.currentTimeMillis();

        System.out.println("consumerId: " + consumerId);
        System.out.println("intimestamp: " + intimestamp);

        Map<String, String> map = new HashMap<>();
        map.put("WM_CONSUMER.ID", consumerId);
        map.put("WM_CONSUMER.INTIMESTAMP", Long.toString(intimestamp));
        map.put("WM_SEC.KEY_VERSION", privateKeyVersion);

        String[] array = canonicalize(map);

        String data = null;

        try {
            data = generator.generateSignature(key, array[1]);
        } catch(Exception e) { }
        System.out.println("Signature: " + data);
    }
    public String generateSignature(String key, String stringToSign) throws Exception {
        Signature signatureInstance = Signature.getInstance("SHA256WithRSA");

        ServiceKeyRep keyRep = new ServiceKeyRep(KeyRep.Type.PRIVATE, "RSA", "PKCS#8", Base64.decodeBase64(key));

        PrivateKey resolvedPrivateKey = (PrivateKey) keyRep.readResolve();

        signatureInstance.initSign(resolvedPrivateKey);

        byte[] bytesToSign = stringToSign.getBytes("UTF-8");
        signatureInstance.update(bytesToSign);
        byte[] signatureBytes = signatureInstance.sign();

        String signatureString = Base64.encodeBase64String(signatureBytes);

        return signatureString;
    }
    protected static String[] canonicalize(Map<String, String> headersToSign) {
        StringBuffer canonicalizedStrBuffer=new StringBuffer();
        StringBuffer parameterNamesBuffer=new StringBuffer();
        Set<String> keySet=headersToSign.keySet();

        // Create sorted key set to enforce order on the key names
        SortedSet<String> sortedKeySet=new TreeSet<String>(keySet);
        for (String key :sortedKeySet) {
            Object val=headersToSign.get(key);
            parameterNamesBuffer.append(key.trim()).append(";");
            canonicalizedStrBuffer.append(val.toString().trim()).append("\n");
        }
        return new String[] {parameterNamesBuffer.toString(), canonicalizedStrBuffer.toString()};
    }

    class ServiceKeyRep extends KeyRep  {
        private static final long serialVersionUID = -7213340660431987616L;
        public ServiceKeyRep(Type type, String algorithm, String format, byte[] encoded) {
            super(type, algorithm, format, encoded);
        }
        protected Object readResolve() throws ObjectStreamException {
            return super.readResolve();
        }
    }
}