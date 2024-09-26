package arizona.tools.plugin.services;

import arizona.tools.plugin.dto.*;
import arizona.tools.plugin.exceptions.FormatNotValidException;
import arizona.tools.plugin.dto.Property;
import arizona.tools.plugin.exceptions.NotValidJSONResponseException;
import arizona.tools.plugin.exceptions.ServerNotRespondingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PluginService {
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, APIMapResponse> redisTemplate;

    private static final String ARIZONA_MAP_URL = "https://n-api.arizona-rp.com/api/map";
    private static final long REDIS_CACHE_SECONDS = 10 * 60;

    public PluginService(RestTemplate restTemplate1, RedisTemplate<String, APIMapResponse> redisTemplate1) {
        restTemplate = restTemplate1;
        redisTemplate = redisTemplate1;
    }


    public GetCaptchasResponse getCaptchas(GetCaptchasRequest getCaptchasRequest) {
        List<String> captchas = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < getCaptchasRequest.getCount(); i++) {
            StringBuilder captcha = new StringBuilder(Integer.toString(random.nextInt(1, 10)));

            for (int j = 0; j < 3; j++) {
                captcha.append(random.nextInt(0, 10));
            }

            if (random.nextInt(0, 100) < getCaptchasRequest.getZeroChance()) {
                captcha.append("0");
            } else {
                captcha.append(random.nextInt(0, 10));
            }

            captchas.add(captcha.toString());
        }
        return new GetCaptchasResponse(captchas);
    }

    public ConvertTimeInMoscowResponse convertTimeInMoscow(ConvertTimeInMoscowRequest convertTimeInMoscowRequest) {
        long clientTime;

        if (convertTimeInMoscowRequest.getIsNumber()) {
            clientTime = Integer.parseInt(convertTimeInMoscowRequest.getTime().toString());
        } else {
            try {
                clientTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(
                        String.format("01/01/1970 %s", convertTimeInMoscowRequest.getTime().toString())
                ).getTime() / 1000;
            } catch (ParseException e) {
                throw new FormatNotValidException();
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        String moscowTimeHours = simpleDateFormat.format(new Date((Instant.now().getEpochSecond()) * 1000));

        String moscowTimeMinutesAndSeconds = new SimpleDateFormat("mm:ss").format(new Date(clientTime * 1000));

        return new ConvertTimeInMoscowResponse(String.format("%s:%s", moscowTimeHours, moscowTimeMinutesAndSeconds));
    }

    public CalcTaxResponse calcTax(CalcTaxRequest calcTaxRequest) {
        int maxTax = (calcTaxRequest.getProperty().equals(Property.HOUSES)) ? 104000 : 250000;

        int hoursRemaining = (int) Math.ceil(((double) (maxTax - calcTaxRequest.getNalogNow()) / ((double) calcTaxRequest.getNalogInHour() * (calcTaxRequest.isInsurance() ? 1 : 2))));
        int secondsRemaining = 3600 * hoursRemaining;
        int daysRemaining = hoursRemaining / 24;
        int daysLeftHours = hoursRemaining - (daysRemaining * 24);

        int secondsWhenExpire = calcTaxRequest.getTime() + secondsRemaining + calcTaxRequest.getTimeOffset();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");
    
        if (calcTaxRequest.isCalcInMskTime()) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        }

        String dateWhenExpire = simpleDateFormat.format(new Date(secondsWhenExpire * 1000L));
        return new CalcTaxResponse(
                hoursRemaining,
                daysRemaining,
                daysLeftHours,
                dateWhenExpire
        );
    }

    public SearchPropertyResponse searchProperty(SearchPropertyRequest request) {
        APIMapResponse responseBody = fetchAPIMap(request);

        Map<String, SearchPropertyResponse.UserInfo> userInfoMap = new HashMap<>();

        processItems(responseBody.getHouses().getOnAuction(), userInfoMap, true);
        processItems(responseBody.getHouses().getHasOwner(), userInfoMap, true);
        processItems(responseBody.getBusinesses().getOnAuction(), userInfoMap, false);
        processItems(responseBody.getBusinesses().getNoAuction(), userInfoMap, false);

        return new SearchPropertyResponse(userInfoMap);
    }

    private APIMapResponse fetchAPIMap(SearchPropertyRequest request) {
        String redisKey = "apiMapResponse" + request.getServerNumber();
        APIMapResponse redisResponse = redisTemplate.opsForValue().get(redisKey);
        if (redisResponse != null) {
            return redisResponse;
        }

        ResponseEntity<APIMapResponse> response;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("accept", "application/json");
        httpHeaders.set("origin", "https://arizona-rp.com/");
        httpHeaders.set("referer", "https://arizona-rp.com/");

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        try {
            response = restTemplate.exchange(ARIZONA_MAP_URL + String.format("/%s", request.getServerNumber()), HttpMethod.GET, requestEntity, APIMapResponse.class);
        } catch (HttpClientErrorException e) {
            throw new ServerNotRespondingException("server not responding " + e.getStatusText() + " : " + e.getStatusCode());
        }

        if (!(response.getStatusCode().value() >= 200 && response.getStatusCode().value() <= 250)) {
            throw new ServerNotRespondingException("not responding backend");
        }

        APIMapResponse responseBody = response.getBody();
        redisTemplate.opsForValue().set(redisKey, responseBody, REDIS_CACHE_SECONDS, TimeUnit.SECONDS);
        return responseBody;
    }

    private void processItems(List<APIMapResponse.Item> items, Map<String, SearchPropertyResponse.UserInfo> userInfoMap, boolean isHouse) {
        for (APIMapResponse.Item item : items) {
            String owner = item.getOwner();
            if (!userInfoMap.containsKey(owner)) {
                userInfoMap.put(owner, new SearchPropertyResponse.UserInfo(0, new ArrayList<>(), 0, new ArrayList<>()));
            }
            SearchPropertyResponse.UserInfo userInfo = userInfoMap.get(owner);
            if (isHouse) {
                userInfo.setHousesCount(userInfo.getHousesCount() + 1);
                userInfo.getHousesIds().add(item.getId() - 1);
            } else {
                userInfo.setBusinessesCount(userInfo.getBusinessesCount() + 1);
                userInfo.getBusinessesIds().add(item.getId() - 1);
            }
        }
    }
}
