package dev.keqing.urbansensor.utils;

import com.auth0.jwt.algorithms.Algorithm;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAKeys {
    public static Algorithm getAlgorithm() throws IOException {
        String publicPath = new File("public-key.pem").getAbsolutePath();
        String privatePath = new File("private-key.pem").getAbsolutePath();
        RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(publicPath, "RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(privatePath, "RSA");
        return Algorithm.RSA256(publicKey, privateKey);
    }
}
