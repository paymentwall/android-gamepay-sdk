package com.paymentwall.pwunifiedsdk.mint.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.paymentwall.pwunifiedsdk.mint.message.MintRequest;
import com.paymentwall.pwunifiedsdk.util.PwUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

public class PWHttpClient {
	public static final String USER_AGENT = "PWHttpClient";
//	public static final String BASE_URL = "https://api.paymentwall.com";
	public static final String BASE_URL = "https://feature-shopify-main.wallapi.bamboo.stuffio.com";
	public static final String MINT_REDEEM_URL = BASE_URL+"/api/pure-mint/payment";
	public static final int TIMEOUT_READ = 10000;
	public static final int TIMEOUT_CONNECT = 20000;
	/*private static AsyncHttpClient client = new AsyncHttpClient();

	public static void mintRedeem(Context context, MintRequest mintRequest, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException
	{
		client.setUserAgent(USER_AGENT);
		UrlEncodedFormEntity entity = mintRequest.toUrlEncodedFormEntity();
		client.post(context, MINT_REDEEM_URL, entity, null, responseHandler);
	}*/

	public static void mintRedeem(final Context context, final MintRequest mintRequest, final MintCallback callback) {
		Looper looper;
		if(context!=null) {
			looper = context.getMainLooper();
		} else {
			looper = Looper.getMainLooper();
		}
		final Handler handler = new Handler(looper);

		Thread mintRedeemThread = new Thread(new Runnable() {
			@Override
			public void run() {
				postMintOnStart(callback, handler);
				OutputStream outputStream = null;
				//Prepare SSL cache
				String originalDNSCacheTTL = null;
				boolean allowedToSetTTL = true;
				try {
					originalDNSCacheTTL = Security
							.getProperty("networkaddress.cache.ttl");
					Security.setProperty("networkaddress.cache.ttl", "0");
				} catch (SecurityException se) {
					allowedToSetTTL = false;
				}
				try {
					URL url = new URL(MINT_REDEEM_URL);
					HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

					String query = getQuery(mintRequest.toParametersMap());
					connection.setFixedLengthStreamingMode(query.length());
					connection.setReadTimeout(TIMEOUT_READ);
					connection.setConnectTimeout(TIMEOUT_CONNECT);
					connection.setRequestMethod("POST");
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setUseCaches(false);
					connection.setRequestProperty(
							"Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					connection = PwUtils.addExtraHeaders(context, connection);
					checkSSLCert(connection);
					outputStream = connection.getOutputStream();
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
					writer.write(query);
					writer.flush();
					writer.close();
					int statusCode = connection.getResponseCode();
					if (statusCode < HttpURLConnection.HTTP_OK || statusCode >=HttpURLConnection.HTTP_MULT_CHOICE) {
						try {
							String errorResponse = getResponseBody(connection.getErrorStream());
							postMintError(statusCode, errorResponse, null, callback, handler);
						} catch (IOException e) {
							postMintError(statusCode, null, null, callback, handler);
						}
					} else {
						try {
							String successfulResponse = getResponseBody(connection.getInputStream());
							postMintSuccess(statusCode, successfulResponse, callback, handler);
						} catch (IOException e) {
							postMintSuccess(statusCode, null, callback, handler);
						}
					}

				} catch (MalformedURLException malformedURLException) {
					postMintError(0, null, malformedURLException, callback, handler);
				} catch (IOException e) {
					postMintError(0, null, e, callback, handler);
				} catch (Exception e) {
					e.printStackTrace();
					postMintError(0, null, e, callback, handler);
				} catch (Throwable throwable) {
					postMintError(0, null, throwable, callback, handler);
				} finally {
					if(outputStream!=null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							postMintError(0, null, e, callback, handler);
						}
					}

					if (allowedToSetTTL) {
						if (originalDNSCacheTTL == null) {
							Security.setProperty("networkaddress.cache.ttl", "-1");
						} else {
							Security.setProperty("networkaddress.cache.ttl",
									originalDNSCacheTTL);
						}
					}
				}
			}
		});
		mintRedeemThread.start();

	}

	private static void postMintError(final int statusCode, final String responseBody, final Throwable error, final MintCallback callback, Handler handler) {
		if(handler!=null && callback!=null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					callback.onMintStop();
					callback.onMintError(statusCode, responseBody, error);
				}
			});
		}
	}

	private static void postMintSuccess(final int statusCode, final String responseBody, final MintCallback callback, Handler handler) {
		if(handler!=null && callback!=null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					callback.onMintStop();
					callback.onMintSuccess(statusCode, responseBody);
				}
			});
		}
	}

	private static void postMintOnStart(final MintCallback callback, Handler handler) {
		if(handler!=null && callback!=null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					callback.onMintStart();
				}
			});
		}
	}

	private static void checkSSLCert(HttpURLConnection connnection) throws Throwable{
		if(!connnection.getURL().getHost().equals("api.paymentwall.com")) {
			return;
		}
		HttpsURLConnection tempConnection = (HttpsURLConnection) connnection;
		try {
			tempConnection.connect();
		} catch (IOException e) {
			throw e;
		}

		Certificate[] certificates;
		try {
			certificates = tempConnection.getServerCertificates();
		} catch (SSLPeerUnverifiedException e) {
			throw e;
		}

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] der = certificates[0].getEncoded();
			md.update(der);
			byte[] digest = md.digest();
			byte[] revokedCertDigest = {5, -64, -77, 100, 54, -108, 71, 10,
					-120, -116, 110, 127, -21, 92, -98, 36, -24, 35, -36, 83};
			if (Arrays.equals(digest, revokedCertDigest)) {
				throw new SSLPeerUnverifiedException("Revoked certificate");
			}
		} catch (NoSuchAlgorithmException e) {
			throw e;
		} catch (CertificateEncodingException e) {
			throw e;
		}
	}

	private static String getResponseBody(InputStream responseStream)
			throws IOException {
		String responseBody = getStringFromInputStream(responseStream);
		try {
			responseStream.close();
		} catch (IOException e) {
			throw e;
		}
		return responseBody;
	}

	private static String getStringFromInputStream(InputStream is)
			throws IOException {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return sb.toString();
	}

	public interface MintCallback {
		void onMintError(int statusCode, String responseBody, Throwable error);
		void onMintSuccess(int statusCode, String responseBody);
		void onMintStart();
		void onMintStop();
	}

	private static String getQuery(Map<String,String> params) throws UnsupportedEncodingException {
		StringBuilder query = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry: params.entrySet()) {
			if(first) first = false;
			else query.append('&');
			query.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
			query.append('=');
			query.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
		}
		return query.toString();
	}
}
