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

        String consumerId = "b7423a78-a2fb-4454-9848-d2cc69dcdde5";
        String privateKeyVersion = "1";
        String key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC/yD3Ate9b2frr\n" +
                "3BuIy45ck9fFgMzzeX5R/xfSXmdXeBGCHJXuf+dOzPV7m4jSlNbCW8EGHLRQYer0\n" +
                "V0EUyXUIqDEITM0hewVjYpIBBX18odr/5P7ql5z+kQCa+yHH4j0ksjU5mZRRfU13\n" +
                "KaLV/3Svm1+aQ+h3cAoAafiqb5JlDD7NtsBpYwKlOsilz5qtFpeuJE1PkopGHK0/\n" +
                "xj5TtY/9zG+rX6HCUcYecl59gvh6DKKd3V0JUuyG1tvF21bMqtj4b1ULWgkwfP2j\n" +
                "6qlO2Ml4Yh7nX1WNUxOCAg3RDBEZrhMXvkB1JdpJ/1QUq6gJNTmYC6oHyDTHbk9C\n" +
                "AhD8hUTzAgMBAAECggEASi3knNaCR6/Pm00epVG+tdZFflIsc/xJPOQDTc5QAIgj\n" +
                "lYUDBSYZzOQAC5iEWpdRXgnpZjjSEcld/LaHKMgnaXrMxF4YddvEoMVundCG0h80\n" +
                "uwfTBeAwzFI/vrJmRY8fgUMgzS+3K7Hgtbe4ETfm5Umet+eOUixW4gQbN6lCIazm\n" +
                "1WmwpHalH6MRzZqmchU3mf+jMpN9dwUQRzuNM0yksdhyWV6TI1UxdB/AdfbFGZ+U\n" +
                "ANyiUOkLhossJYaZUW15cY0qM1p1C97tfyEyQccXAp1RzxPFld6USeXDrde+NZ9o\n" +
                "41a4FF9K5TQx70/frwwvgA1x0gT7ufN4CJaOAkFHYQKBgQD+E1/OZl9UiYjZLe1c\n" +
                "hgV2nU2QShZM6LvvuUJKqe+8tYXzxAZLyX0C5PlUqmptutxbm5LqWcxq6R/jjbJN\n" +
                "Q/aNsjMzrzB2P7WMgT2vX7MECGUavjECgixFPko4YRqnkTy6F59FCt8Qbm2JjBHi\n" +
                "9GCuMprfi+PYc6JNPLjHl0c08QKBgQDBPBYmRcvYyKYdeP11ic70F7lT+rkQ0BZZ\n" +
                "E9+D5hsZ+wlfdOXHGiT4nWPyGtjYyb7MaZuAO6feew7VjXBg1vlIH4oWTnOOK6WB\n" +
                "ahvwvowta4BE10/84eKlVqc5ojOZxbUOcSSiIPld4lMn1+kBvKMImfnqrMiKWWZf\n" +
                "HOG5sDmIIwKBgHHV1mAQh/fFY1YC+M0ykvIRCnZya0lsIQAIHgZ72xjOr8SG+Fcu\n" +
                "YF9Snc9sRyEAzsgQjSOItkjUSGBY1SE/KRFVXRF0FZ91be8U3MFdfER7SmlP+39n\n" +
                "OVSQ0wotn5aiQPYTrV1uwICJKXSuGDl6SnLh43fzIlORg49dfGCYi+PRAoGAZIpR\n" +
                "7/ny1UmY/M69+dEJKAHMAMD8V54dcp2fd25bfYobV1poT87vm5ewsAB+AVX++zab\n" +
                "nP3tGxomHsDnzssrivY2D+Mjxr447/v4q6tJHjzH0YnB6Y7SMTrD6X3WoVS4HtqJ\n" +
                "t/22Ib4njByP9MJkhY4v9YWPFkTrueM6IzZoyMcCgYBEfTRpo6xWvwCl6VV8RxRu\n" +
                "RE1EF1BlEmW72ozouJCZ8Z9cToO57DQNr0CCFmLkwpiwzcoN4nyG8JL5OtdKMGca\n" +
                "IHAJRwcoiRkw8vMpXCrpk/fTb0ekLDkiyZr+dWxZg/ZhXKAZL41XsjlgNdYX2txD\n" +
                "BPVY5HTtn88WLnM1d17hnw==";

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