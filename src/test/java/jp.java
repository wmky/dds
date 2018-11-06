/*
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;



public class jp {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		
		HttpPost post = new HttpPost("http://survey.fang.com/web/poll_simple.php?survey_id=58503");
        DefaultHttpClient client = new DefaultHttpClient();
            HttpHost proxy = new HttpHost("60.191.157.90", 3128);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        // 设置需要提交的参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        		list.add(new BasicNameValuePair("q_175474[37290]", "37290"));
                list.add(new BasicNameValuePair("q_175476[37312]", "37312"));
                list.add(new BasicNameValuePair("q_175475[37302]", "37302"));
                list.add(new BasicNameValuePair("q_175476[42744]", "42744"));
                post.setEntity(new UrlEncodedFormEntity(list, "utf8"));
                HttpResponse re =   client.execute(post);
             
                if (re.getEntity() != null) {    
                    InputStream instreams = re.getEntity().getContent();    
                    String str = convertStreamToString(instreams);  
                    System.out.println("Do something");   
                    System.out.println(str);  
                    // Do not need the rest    
                }  
	}
	
	public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return sb.toString();      
    }  

}
*/