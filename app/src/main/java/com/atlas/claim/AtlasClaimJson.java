package com.atlas.claim;

public class AtlasClaimJson {

    private final String ATLAS_CLAIM_SHORT_CODE_JSON_KEY = "short_code";
    private final String ATLAS_CLAIM_SECRET_KEY_JSON_KEY = "secret_key";
    private final String ATLAS_CLAIM_OWNER_IDENTITY_JSON_KEY = "owner_identity";

    private String json;

    public AtlasClaimJson(String shortCode, String secretKey, String ownerIdentity) {
        json = "{"
                + "\"" + ATLAS_CLAIM_SHORT_CODE_JSON_KEY + "\":\"" + shortCode + "\","
                + "\"" + ATLAS_CLAIM_SECRET_KEY_JSON_KEY + "\":\"" + secretKey + "\","
                + "\"" + ATLAS_CLAIM_OWNER_IDENTITY_JSON_KEY + "\":\"" + ownerIdentity + "\""
                + "}";

    }

    public String buildJson(String shortCode, String secretKey, String ownerIdentity) {
        json = "{"
                + "\"" + ATLAS_CLAIM_SHORT_CODE_JSON_KEY + "\":\"" + shortCode + "\","
                + "\"" + ATLAS_CLAIM_SECRET_KEY_JSON_KEY + "\":\"" + secretKey + "\","
                + "\"" + ATLAS_CLAIM_OWNER_IDENTITY_JSON_KEY + "\":\"" + ownerIdentity + "\""
                + "}";

        return json;
    }

    public String getJson() {
        return json;
    }


}
