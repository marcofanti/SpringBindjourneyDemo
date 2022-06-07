package org.itnaf.demo;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@CrossOrigin //(origins = "http://localhost:8000", maxAge = 3600)
@Controller
public class BindJourneyController {

    private static String REGEX_AMPERSAND = "&";
    private static String REGEX_EQUAL = "=";
    private static final String template = "Hello, %s!";

    Pattern patternAmpersand = Pattern.compile(REGEX_AMPERSAND);
    Pattern patternEqual = Pattern.compile(REGEX_EQUAL);

    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/")
    public String index() {
        return "login";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/BehavioSenseDemo/FinalizeSession")
    public String postFinalizeSession(@RequestBody String input) {
        String[] result = patternAmpersand.split(input);
        return "{\"status\": \"ok\"}";
    }

    @PostMapping(value = "/BehavioSenseAPI/GetAjaxAsync")
    public String postAjaxSync(@RequestBody String input) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String[] result = patternAmpersand.split(input);

        String endPoint = "http://localhost:18080";
        String path = "/BehavioSenseAPI/GetReport";
        String journeyid = null;
        List<NameValuePair> request = new ArrayList<NameValuePair>();
        String uri = endPoint + path;
        try {
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-type", "text/plain");
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            for (String data : result) {
                String[] namevalue = patternEqual.split(data);
                if (namevalue[0].equalsIgnoreCase("journeyid")) {
                    journeyid = namevalue[1];
                    continue;
                }
                String decodedString = "";
				if (namevalue[0].equalsIgnoreCase("data")) {
					try {
                        decodedString = java.net.URLDecoder.decode(namevalue[1], java.nio.charset.StandardCharsets.UTF_8.toString());
					} catch (Exception e) {
						decodedString = namevalue[1]; //"[[\"w\",[{\"text#username\":8},{\"password#password\":8}],\"/BehavioSenseDemo/Login/\"],[\"f\",\"text#username\",[[0,84,1690],[1,84,1754],[0,69,1882],[1,69,1962],[0,83,2042],[1,83,2122],[0,84,2154],[1,84,2217],[0,85,2635],[1,85,2698],[0,83,2795],[1,83,2858],[0,69,2969],[1,69,3049],[0,82,3130],[1,82,3194]]],[\"fa\",\"password#password\",[[0,0,4427],[1,0,4474],[0,1,4522],[1,1,4587],[0,2,4714],[1,2,4778],[0,3,4874],[1,3,5003],[0,4,5034],[1,4,5113],[0,5,5243],[1,5,5306],[0,6,5387],[1,6,5450],[0,7,5546],[1,7,5627]]]]";
					}
					nvps.add(new BasicNameValuePair("timing", decodedString));
				} else {
                    nvps.add(new BasicNameValuePair(namevalue[0], namevalue[1]));
				}
            }
            nvps.add(new BasicNameValuePair("operatorFlags", "1954"));
            nvps.add(new BasicNameValuePair("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36"));
            nvps.add(new BasicNameValuePair("ip", "127.0.0.1"));
            System.out.println(nvps);
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));
            postRequest.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.88 Safari/537.36");
            HttpResponse response = httpClient.execute(postRequest);
            //response.
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //String sessionid = UUID.randomUUID().toString();

        //String bindjourneypath = "/BehavioSenseAPI/BindJourney";
        //uri = endPoint + bindjourneypath;
        //bindJourney(journeyid, sessionid, "mfanti", uri);
        return "ok";
    }
/*
    private void bindJourney(String journeyid, String sessionid, String userid, String uri) {
        try {
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-type", "text/plain");
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            nvps.add(new BasicNameValuePair("userid", userid));
            nvps.add(new BasicNameValuePair("journeyid", journeyid));
            nvps.add(new BasicNameValuePair("sessionid", sessionid));
            nvps.add(new BasicNameValuePair("report", "true"));
            nvps.add(new BasicNameValuePair("reportflags", "746877092"));
            System.out.println(nvps);
            postRequest.setEntity(new UrlEncodedFormEntity(nvps));
            postRequest.setHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36");
//			HttpResponse response = httpClient.execute(postRequest);
//			System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    } */
}

//timing=[["m","n",{"vendorSub":"","productSub":"20030107","vendor":"Google Inc.","maxTouchPoints":0,"userActivation":{},"brave":{},"globalPrivacyControl":true,"doNotTrack":null,"geolocation":{},"pdfViewerEnabled":true,"webkitTemporaryStorage":{},"webkitPersistentStorage":{},"hardwareConcurrency":8,"cookieEnabled":true,"appCodeName":"Mozilla","appName":"Netscape","appVersion":"5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36","platform":"MacIntel","product":"Gecko","userAgent":"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36","language":"en-US","languages":["en-US","en"],"onLine":true,"webdriver":false,"scheduling":{},"bluetooth":{},"clipboard":{},"credentials":{},"keyboard":null,"managed":{},"mediaDevices":{},"storage":{},"serviceWorker":{},"wakeLock":{},"deviceMemory":8,"ink":{},"hid":{},"locks":{},"mediaCapabilities":{},"mediaSession":{},"permissions":{},"presentation":{},"virtualKeyboard":{},"usb":{},"xr":{},"userAgentData":{"brands":[{"brand":" Not A;Brand","version":"99"},{"brand":"Chromium","version":"101"}],"mobile":false}}],["m","s",{"availWidth":5120,"availHeight":1355,"width":5120,"height":1440,"colorDepth":24,"pixelDepth":24,"availLeft":0,"availTop":25,"orientation":{},"onchange":null,"isExtended":true}],["m","v",250],["f","text#username",[[0,77,5339],[1,77,5402],[0,70,5652],[1,70,5707],[0,65,5719],[1,65,5785],[0,78,5966],[1,78,6032],[0,84,6213],[0,73,6259],[1,84,6263],[1,73,6347],[0,16,6643],[0,50,6776],[1,16,6865],[1,50,6910],[0,66,7093],[1,66,7146],[0,69,7226],[1,69,7305],[0,72,7499],[1,72,7551],[0,65,7563],[1,65,7677],[0,86,7881],[1,86,7967],[0,73,7990],[0,79,8060],[1,73,8115],[1,79,8169],[0,83,8271],[1,83,8349],[0,69,8453],[1,69,8552],[0,67,8733],[1,67,8802],[0,190,8916],[1,190,9016],[0,67,9185],[1,67,9250],[0,79,9295],[0,77,9377],[1,79,9430],[1,77,9510],[0,9,9681]]],["fa","password#password",[[0,0,11187],[1,0,11251],[0,1,11298],[1,1,11365],[0,2,11501],[1,2,11578],[0,3,11786],[0,4,11871],[1,3,11881],[1,4,11939],[0,5,12132],[0,5,12243],[1,5,12323],[1,5,12333],[0,6,12583],[1,6,12625],[0,7,12863],[1,7,12942],[0,8,12951],[1,8,13030],[0,9,13234],[1,9,13300]]],["w",[{"text#username":21},{"password#password":10}],"/login"]]
