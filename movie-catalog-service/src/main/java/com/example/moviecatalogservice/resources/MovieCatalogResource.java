package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        RestTemplate restTemplate = new RestTemplate();

        // get all rated movie IDs
        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 3)
        );

        return ratings.stream().map(rating -> {
            ResponseEntity<Movie> movieResponseEntity = restTemplate.getForEntity("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
            Movie movie = movieResponseEntity.getBody();
            return new CatalogItem(movie.getName(), "Test desc", rating.getRating());
        }).collect(Collectors.toList());

        //for each ID call movie info service and get details

        // put them all together

//        return Collections.singletonList(
//                new CatalogItem("Transformers", "Test", 4)
//        );
    }
}
