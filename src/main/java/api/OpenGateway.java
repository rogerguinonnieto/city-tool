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
    private static final String CLIENT_ID2 = dotenv.get("OPENGATEWAY_CLIENT_ID2");
    private static final String CLIENT_SECRET2 = dotenv.get("OPENGATEWAY_CLIENT_SECRET2");
    private static final String BASE_URL = "https://sandbox.opengateway.telefonica.com/apigateway";

    public static boolean verifyAge(int phoneNumber, int threshold) throws Exception {
        try {
            System.out.println("[OG] Starting age verification for: " + phoneNumber);
            
            String authReqId = getAuthReqId(phoneNumber, true);
            System.out.println("[OG] Received authReqId: " + authReqId);

            String accessToken = getAccessToken(authReqId, CLIENT_ID, CLIENT_SECRET);
            System.out.println("[OG] Received Access Token successfully.");

            boolean result = callVerifyAgeAPI(accessToken, phoneNumber, threshold);
            System.out.println("[OG] Final verification result: " + result);
            
            return result;
        } catch (Exception e) {
            System.err.println("[OG ERROR] Verification flow crashed: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be caught by your Service class
        }
    }

    private static String getAuthReqId(int phone, Boolean isAgeAPI) throws Exception {
        Client client = ClientBuilder.newClient();
        Response response;
        if (isAgeAPI){
            Form form = new Form()
            .param("login_hint", "tel:+" + phone)
            .param("scope", "dpv:FraudPreventionAndDetection#kyc-age-verification:verify");
            response = client.target(BASE_URL + "/bc-authorize")
            .register(HttpAuthenticationFeature.basic(CLIENT_ID, CLIENT_SECRET))
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));
        } else {
            Form form = new Form()
            .param("login_hint", "tel:+" + phone)
            .param("scope", "dpv:FraudPreventionAndDetection#kyc-match:match");
            response = client.target(BASE_URL + "/bc-authorize")
            .register(HttpAuthenticationFeature.basic(CLIENT_ID2, CLIENT_SECRET2))
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));
        }
        

        String entity = response.readEntity(String.class);
        if (response.getStatus() != 200 && response.getStatus() != 201) {
            System.err.println("[OG ERROR] /bc-authorize failed. Status: " + response.getStatus());
            System.err.println("[OG ERROR] Response: " + entity);
            throw new Exception("Auth Request failed with status " + response.getStatus());
        }
        return new JSONObject(entity).getString("auth_req_id");
    }

    private static String getAccessToken(String authReqId, String clientId, String clientSecret) throws Exception {
        Client client = ClientBuilder.newClient();
        Form form = new Form()
            .param("grant_type", "urn:openid:params:grant-type:ciba")
            .param("auth_req_id", authReqId);

        Response response = client.target(BASE_URL + "/token")
            .register(HttpAuthenticationFeature.basic(clientId, clientSecret))
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.form(form));

        String entity = response.readEntity(String.class);
        if (response.getStatus() != 200) {
            System.err.println("[OG ERROR] /token failed. Status: " + response.getStatus());
            System.err.println("[OG ERROR] Response: " + entity);
            throw new Exception("Token retrieval failed");
        }
        return new JSONObject(entity).getString("access_token");
    }

    private static boolean callVerifyAgeAPI(String token, int phone, int threshold) {
        Client client = ClientBuilder.newClient();
        String verifyUrl = "https://sandbox.opengateway.telefonica.com/apigateway/kyc-age-verification/v0.1/verify";
        
        JSONObject body = new JSONObject();
        body.put("ageThreshold", threshold);

        Response response = client.target(verifyUrl)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .header("x-correlator", java.util.UUID.randomUUID().toString())
            .post(Entity.json(body.toString()));

        String entity = response.readEntity(String.class);
        if (response.getStatus() == 200) {
            JSONObject resJson = new JSONObject(entity);
            String result = resJson.getString("ageCheck");
            return "true".equals(result);
        } else {
            System.err.println("[OG ERROR] /verify API failed. Status: " + response.getStatus());
            System.err.println("[OG ERROR] Response: " + entity);
            return false;
        }
    }

    public static boolean verifyName(int phoneNumber, String name) throws Exception {
        try {
            System.out.println("[OG] Starting name verification for: " + phoneNumber);
            
            String authReqId = getAuthReqId(phoneNumber, false);
            System.out.println("[OG] Received authReqId for name verification: " + authReqId);

            String accessToken = getAccessToken(authReqId, CLIENT_ID2, CLIENT_SECRET2);
            System.out.println("[OG] Received Access Token successfully for name verification.");

            boolean result = callVerifyNameAPI(accessToken, phoneNumber, name);
            System.out.println("[OG] Final name verification result: " + result);
            
            return result;
        } catch (Exception e) {
            System.err.println("[OG ERROR] Name verification flow crashed: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be caught by your Service class
        }
    }

    private static boolean callVerifyNameAPI(String token, int phone, String name) {
        Client client = ClientBuilder.newClient();
        String verifyUrl = "https://sandbox.opengateway.telefonica.com/apigateway/kyc-match/v0.2/match";
        
        JSONObject body = new JSONObject();
        body.put("name", name);

        Response response = client.target(verifyUrl)
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .header("x-correlator", java.util.UUID.randomUUID().toString())
            .post(Entity.json(body.toString()));

        String entity = response.readEntity(String.class);
        if (response.getStatus() == 200) {
            JSONObject resJson = new JSONObject(entity);
            String result = resJson.getString("nameMatch");
            return "true".equals(result);
        } else {
            System.err.println("[OG ERROR] /verify name API failed. Status: " + response.getStatus());
            System.err.println("[OG ERROR] Response: " + entity);
            return false;
        }
    }
}