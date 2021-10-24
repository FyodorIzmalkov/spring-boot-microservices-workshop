package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    // should actually return wrapper instead of a list
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
        List<Rating> ratings = userRating.getUserRating();

        return ratings.stream().map(rating -> {
            ResponseEntity<Movie> movieResponseEntity = restTemplate.getForEntity("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
            Movie movie = movieResponseEntity.getBody();

            return new CatalogItem(movie.getName(), "Test desc", rating.getRating());
        }).collect(Collectors.toList());
    }
}

// using webClient
//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();

// mock
//        return Collections.singletonList(
//                new CatalogItem("Transformers", "Test", 4)
//        );
