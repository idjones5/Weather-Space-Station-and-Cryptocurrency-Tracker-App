package com.company.Final.Project.apiCalls;

import com.company.Final.Project.exceptions.WebExceptions;
import com.company.Final.Project.iss.SpaceResponse;
import com.company.Final.Project.weather.WeatherResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class IssOpenWeatherAPIs {

    // method to call the ISS and open weather APIs
    // also controls the response of the calls
    // returns the coordinates, the weather, country, and city if applicable

    public static void issWeatherConditions() {

        try {
            String issLat;
            String issLong;
            String mainWeather;
            String weatherDescription;
            String issCountry;
            String issCity;

            WebClient client = WebClient.create("http://api.open-notify.org/iss-now.json?callback");

            Mono<SpaceResponse> issResponse = client
                    .get()
                    .retrieve()
                    .bodyToMono(SpaceResponse.class);

            SpaceResponse iss = issResponse.share().block();

            issLat = iss.getIssPosition().getLatitude();
            issLong = iss.getIssPosition().getLongitude();

            WebClient client1 = WebClient.create("https://api.openweathermap.org/data/2.5/weather?lat=" + issLat + "&" +
                    "lon=" + issLong + "&appid=fd238fddcae5fb3172123f01221a835d&units=imperial");

            Mono<WeatherResponse> weatherResponse = client1
                    .get()
                    .retrieve()
                    .bodyToMono(WeatherResponse.class);

            WeatherResponse weatherAtLocation = weatherResponse.share().block();

            mainWeather = weatherAtLocation.getWeather().get(0).getMain();
            weatherDescription = weatherAtLocation.getWeather().get(0).getDescription();

            if (weatherAtLocation.getSys().getCountry() == null) {

                System.out.println("Below is a summary of the latitude, longitude" +
                        ", country, and weather at the current location of the" +
                        " International Space Station.");

                System.out.println("===================");
                System.out.println("Latitude: " + issLat + "°");
                System.out.println("Longitude: " + issLong + "°");
                System.out.println("Country: The Space Station is not currently in a country.");
                System.out.println("\n" + "Weather");
                System.out.println("===================");
                System.out.println("Main Weather: " + mainWeather);
                System.out.println("Detail: " + weatherDescription);
                System.out.println("Temperature: " + weatherAtLocation.getMain().getTemp() + " °F");
                System.out.println("===================");
            } else  {

                issCountry = weatherAtLocation.getSys().getCountry();
                issCity = weatherAtLocation.getName();

                System.out.println("Below is a summary of the latitude, longitude" +
                        ", country, city, and weather at the current location of the" +
                        " International Space Station.");

                System.out.println("===================");
                System.out.println("Latitude: " + issLat + "°");
                System.out.println("Longitude: " + issLong + "°");
                System.out.println("Country: " + issCountry);
                System.out.println("City: " + issCity);
                System.out.println("\n" + "Weather");
                System.out.println("===================");
                System.out.println("Main Weather: " + mainWeather);
                System.out.println("Detail: " + weatherDescription);
                System.out.println("===================");
            }

        }
        catch (WebClientResponseException we) {
            WebExceptions.catchException(we);
        }
        catch (Exception exception) {
            System.out.println("An error has occurred " + exception.getMessage());
        }
    }


}