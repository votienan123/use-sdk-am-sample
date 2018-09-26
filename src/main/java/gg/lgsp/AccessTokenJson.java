package gg.lgsp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AccessTokenJson {
	private String consumerKey;
	private String secretKey;
	private String tokenUrl;
	private JsonObject response;

	public AccessTokenJson() {
	}

	public AccessTokenJson(String consumerKey, String scretKey, String url) {
		this.consumerKey = consumerKey;
		this.secretKey = scretKey;
		this.tokenUrl = url;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getTokenUrl() {
		return tokenUrl;
	}

	public void setTokenUrl(String url) {
		this.tokenUrl = url;
	}

	public String toString() {
		return response != null ? response.toString() : "";
	}

	
	public String gen() {
		try {
		URL url = new URL("https://192.168.1.166:8244/token");
		HttpsURLConnection.setDefaultHostnameVerifier(
				new javax.net.ssl.HostnameVerifier(){

				    public boolean verify(String hostname,
				            javax.net.ssl.SSLSession sslSession) {
				        return hostname.equals("192.168.1.166");
				    }
				});
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Basic amVYTEh5OWVJSjlzZDdnMjFwS1dEaDFzM0M0YTppT3NiMWhodkUzXzU4ZFhnZmNIT0VvcnBkVG9h");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("grant_type", "client_credentials");
		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		                new OutputStreamWriter(os, "UTF-8"));
		writer.write("grant_type=client_credentials");
		writer.flush();
		writer.close();
		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		String output;
		System.out.println("gen gen Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }
		return "";
	}
	
	public AccessToken generate() {
		AccessToken accessToken = null;
		if (getConsumerKey() != null && getSecretKey() != null) {
			if (getTokenUrl() != null && !getTokenUrl().isEmpty()) {
				String grantType = "client_credentials";
				String keyBase64Encrypted = Base64.getEncoder()
						.encodeToString((getConsumerKey() + ":" + getSecretKey()).getBytes());
				//System.out.println("keyBase64Encrypted: "+keyBase64Encrypted);
				String[] command = { "curl", "-k", "-d", "grant_type=" + grantType, "-H",
						"Authorization: Basic " + keyBase64Encrypted, getTokenUrl() };
				ProcessBuilder process = new ProcessBuilder(command);
				
				Process p;
				try {
					p = process.start();
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					StringBuilder builder = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						builder.append(line);
						builder.append(System.getProperty("line.separator"));
					}
					String result = builder.toString();
					response = new JsonParser().parse(result).getAsJsonObject();
					accessToken = new AccessToken(response);
					p.destroy();
				} catch (IOException e) {
					System.out.print("error");
					e.printStackTrace();
				}
			} else {
				System.out.println("Token url must not be null");
			}
		} else {
			System.out.println("Consumer key and scret key must not be null");
		}
		return accessToken;
	}
	
}

class AccessToken {
	private String token;
	private long expire;
	private JsonObject json;
	private String scope;
	private String tokenType;
	
	public AccessToken() {}
	
	public AccessToken(JsonObject json) {
		this.json = json;
		if(this.json != null) {
			if(this.json.get("access_token") != null) {
				this.token = this.json.get("access_token").getAsString();
			}
			if(this.json.get("scope") != null) {
				this.scope = this.json.get("scope").getAsString();
			}
			if(this.json.get("expires_in") != null) {
				this.expire = this.json.get("expires_in").getAsLong();
			}
			if(this.json.get("token_type") != null) {
				this.tokenType = this.json.get("token_type").getAsString();
			}
		}
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpire() {
		return expire;
	}
	public void setExpire(long expire) {
		this.expire = expire;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
