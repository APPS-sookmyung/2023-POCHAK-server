package com.apps.pochak.login.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApplePublicKeyResponse {

    private List<Key> keys;

    @Getter
    public static class Key {
        @JsonProperty
        private String kty;
        @JsonProperty
        private String kid;
        @JsonProperty
        private String use;
        @JsonProperty
        private String alg;
        @JsonProperty
        private String n;
        @JsonProperty
        private String e;
    }

    public Optional<Key> getMatchedKeyBy(String kid, String alg) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }
}
