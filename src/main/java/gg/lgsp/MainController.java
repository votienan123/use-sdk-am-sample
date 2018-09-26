package gg.lgsp;

import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.Configuration;
import org.wso2.client.api.Cmon_CoQuanQuanLy.DefaultApi;

public class MainController {

	public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        
        DefaultApi apiInstance = new DefaultApi();
        String key = "u2ae8___hu6TFIWuNnmCLBTspFYa"; // (1) Consumer key
		String secret = "4tFR5fr4T7HR922mGgkRTf2ZuPYa"; // (2) Consumer secret
        String url = "https://api.lgsp.quangnam.gov.vn/token"; // (3) URL get token
        String accessToken = new AccessTokenJson(key, secret, url).generate().getToken();
        System.out.println(accessToken);
        defaultClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        defaultClient.setBasePath("https://api.lgsp.quangnam.gov.vn/coquanquanly/v1.0.0"); // (4) URL Base Path
        apiInstance.setApiClient(defaultClient);
        try {
        	Object obj = apiInstance.coquanquanlysCapCapGet("002");
        	System.out.println(obj);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
