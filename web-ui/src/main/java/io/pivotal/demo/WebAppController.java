package io.pivotal.demo;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.pivotal.demo.domain.WordCount;

@RestController
@RequestMapping ("/")
public class WebAppController {
	
	@Autowired
	private RestTemplate restTemplate ;
	
	@GetMapping("/words/60S" )
	public Collection<WordCount> getWords() {
		  
		  String url = "http://localhost:8091/windowedWordCount/60000/";
		  ResponseEntity<Collection<WordCount>> response
				  = restTemplate.exchange(
			               url, HttpMethod.GET, null,
			               new ParameterizedTypeReference<Collection<WordCount>>() {
			                });
		
		 
		  return response.getBody();
		  
	  }

	@GetMapping("/words/2-5M" )
	public Collection<WordCount> getWordsForTwoMinutes() {
		  
		  String url = "http://localhost:8091/windowedWordCount/120000/";
		  ResponseEntity<Collection<WordCount>> response
				  = restTemplate.exchange(
			               url, HttpMethod.GET, null,
			               new ParameterizedTypeReference<Collection<WordCount>>() {
			                });
		
		 
		  return response.getBody();
		  
	  }


}
