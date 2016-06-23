package com.android.volley.toolbox;

import android.support.v4.app.DialogFragment;
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.CursorAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpClientStack.HttpPatch;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

public class HurlStack implements HttpStack {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private final SSLSocketFactory mSslSocketFactory;
    private final UrlRewriter mUrlRewriter;

    public interface UrlRewriter {
        String rewriteUrl(String str);
    }

    public HurlStack() {
        this(null);
    }

    public HurlStack(UrlRewriter urlRewriter) {
        this(urlRewriter, null);
    }

    public HurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap<String, String> map = new HashMap();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (this.mUrlRewriter != null) {
            String rewritten = this.mUrlRewriter.rewriteUrl(url);
            if (rewritten == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }
            url = rewritten;
        }
        HttpURLConnection connection = openConnection(new URL(url), request);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, (String) map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
        if (connection.getResponseCode() == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }
        BasicHttpResponse response = new BasicHttpResponse(new BasicStatusLine(protocolVersion, connection.getResponseCode(), connection.getResponseMessage()));
        response.setEntity(entityFromConnection(connection));
        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                response.addHeader(new BasicHeader((String) header.getKey(), (String) ((List) header.getValue()).get(0)));
            }
        }
        return response;
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        InputStream inputStream;
        BasicHttpEntity entity = new BasicHttpEntity();
        try {
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength((long) connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    protected HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.mSslSocketFactory);
        }
        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Method.DEPRECATED_GET_OR_POST /*-1*/:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
            case DialogFragment.STYLE_NORMAL /*0*/:
                connection.setRequestMethod("GET");
            case DefaultRetryPolicy.DEFAULT_MAX_RETRIES /*1*/:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
            case CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER /*2*/:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
            case FragmentManagerImpl.ANIM_STYLE_CLOSE_ENTER /*3*/:
                connection.setRequestMethod("DELETE");
            case TransportMediator.FLAG_KEY_MEDIA_PLAY /*4*/:
                connection.setRequestMethod("HEAD");
            case FragmentManagerImpl.ANIM_STYLE_FADE_ENTER /*5*/:
                connection.setRequestMethod("OPTIONS");
            case FragmentManagerImpl.ANIM_STYLE_FADE_EXIT /*6*/:
                connection.setRequestMethod("TRACE");
            case Method.PATCH /*7*/:
                addBodyIfExists(connection, request);
                connection.setRequestMethod(HttpPatch.METHOD_NAME);
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HEADER_CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }
}
