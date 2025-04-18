package com.jostinhv.jakarta.infrastructure.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class JwtUtil {

    @Inject
    @ConfigProperty(name = "jwt.private-key.path")
    private String privateKeyPath;

    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 días

    private Key privateKey;

    @PostConstruct
    public void init() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(privateKeyPath)) {
            if (is == null) {
                throw new IllegalStateException("No se encontró el archivo en classpath: " + privateKeyPath);
            }

            String key = new String(is.readAllBytes());
            // Eliminar las líneas de encabezado y pie
            key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] decoded = Base64.getDecoder().decode(key);
            // Comprueba que el decoded contenga datos
            if (decoded == null || decoded.length == 0) {
                throw new IllegalStateException("La clave decodificada está vacía");
            }
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.privateKey = kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar la clave privada desde " + privateKeyPath, e);
        }
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", "Test Key")
                .subject(username)
                .claim("upn", username)  // Agregar el claim "upn"
                .claim("aud", "jwt-audience")  // O el valor que estés utilizando en la configuración
                .claim("groups", roles)
                .issuer("http://localhost:8080")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public String generateRefreshToken(String username, List<String> roles) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", "Test Key")
                .subject(username)
                .claim("upn", username)  // Agregar el claim "upn"
                .claim("aud", "jwt-audience")  // O el valor que estés utilizando en la configuración
                .claim("groups", roles)
                .issuer("http://localhost:8080")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
