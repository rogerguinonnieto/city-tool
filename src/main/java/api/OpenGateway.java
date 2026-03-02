package api;

import io.github.cdimascio.dotenv.Dotenv;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import org.json.JSONObject;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class OpenGateway {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String CLIENT_ID = dotenv.get("OPENGATEWAY_CLIENT_ID");
    private static final String CLIENT_SECRET = dotenv.get("OPENGATEWAY_CLIENT_SECRET");
    private static final String BASE_URL = "https://sandbox.opengateway.telefonica.com/apigateway";

    public static boolean verifyAge(int phoneNumber, int threshold) throws Exception {
        String authReqId = getAuthReqId(phoneNumber);
        String accessToken = getAccessToken(authReqId);
        return callVerifyAgeAPI(accessToken, phoneNumber, threshold);
    }

    private static String getAuthReqId(int phone) {
        Client client = ClientBuilder.newClient();
        Form form = new Form()
            .param("login_hint", "tel:+" + phone)
            .param("scope", "dpv:FraudPreventionAndDetection#verify-age");

        Response response = client.target(BASE_URL + "/bc-authorize")
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", HttpAuthenticationFeature.basic(CLIENT_ID, CLIENT_SECRET))
            .post(Entity.form(form));

        return new JSONObject(response.readEntity(String.class)).getString("auth_req_id");
    }

    private static String getAccessToken(String authReqId) {
        Client client = ClientBuilder.newClient();
        Form form = new Form()
            .param("grant_type", "urn:openid:params:grant-type:ciba")
            .param("auth_req_id", authReqId);

        Response response = client.target(BASE_URL + "/token")
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", HttpAuthenticationFeature.basic(CLIENT_ID, CLIENT_SECRET))
            .post(Entity.form(form));
        return new JSONObject(response.readEntity(String.class)).getString("access_token");
        
    }

    private static boolean callVerifyAgeAPI(String token, int phone, int threshold) {
        Client client = ClientBuilder.newClient();
        String verifyUrl = "https://sandbox.opengateway.telefonica.com/kyc-age-verification/v0.1/verify";
        
        JSONObject body = new JSONObject();
        body.put("ageThreshold", threshold);
        body.put("phoneNumber", "+" + phone);

        Response response = client.target(verifyUrl)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .header("x-correlator", java.util.UUID.randomUUID().toString())
            .post(Entity.json(body.toString()));

        if (response.getStatus() == 200) {
            JSONObject resJson = new JSONObject(response.readEntity(String.class));
            String result = resJson.getString("ageCheck");
            return "true".equals(result);
        }
        return false;
    }
}
