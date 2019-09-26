package io.pivotal.demo;

import java.util.Calendar;
import java.util.List;

import io.pivotal.demo.domain.WordCount;
import io.pivotal.demo.domain.WordCountWindowed;
import io.pivotal.demo.domain.WordCountWindowedCompsiteKey;
import io.pivotal.demo.functions.WindowedWordCountFunction;
import io.pivotal.demo.repositories.WordCountRepo;
import io.pivotal.demo.repositories.WordCountWindowedRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication

public class WordcountUiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordcountUiApplication.class, args);
	}


}

@RestController
class WordsController {
	WordCountRepo wcRepo;
	WordCountWindowedRepo wcwRepo;
	WindowedWordCountFunction functionExecutor;

	public WordsController(WordCountRepo repo, WordCountWindowedRepo wcwRepo, 
			WindowedWordCountFunction functionExecutor) {
		this.wcRepo = repo;
		this.wcwRepo = wcwRepo;
		this.functionExecutor = functionExecutor;
	}

	@GetMapping("/add/{word}/{count}")
	public String addWordCount(@PathVariable String word, @PathVariable long count) {
		WordCount saved = wcRepo.save(new WordCount(word, count));
		if(saved.getWord().equals(word))
			return "Saved successfully";
		return "Save failed";
	}

	@GetMapping("/list")
	public Iterable<WordCount> list() {
		return wcRepo.findAll();
	}

	@GetMapping("/addw/{word}/{count}")
	public String addWordCountWindowed(@PathVariable String word, @PathVariable long count) {
		// gets a calendar using the default time zone and locale.
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -60);
		Long startTimeInLong = calendar.getTimeInMillis();
		WordCountWindowedCompsiteKey key = new WordCountWindowedCompsiteKey(word, calendar.getTime());
		calendar.add(Calendar.SECOND, 60);
		Long endTimeInLong = calendar.getTimeInMillis();
		WordCountWindowed saved = wcwRepo.save(new WordCountWindowed(key, count,
				calendar.getTime(), startTimeInLong, endTimeInLong));
		if(saved.getWordCountValue() == count)
			return "Saved successfully";
		return "Save failed";
	}

	@GetMapping("/listwcw")
	public Iterable<WordCountWindowed> listWCW() {
		return wcwRepo.findAll();
	}
	
	
	@GetMapping("/computewordcount/{startTime}/{endTime}")
	public List computeWordCount(@PathVariable Long startTime, 
			@PathVariable Long endTime) {
		return functionExecutor.computeWindowedWordCount(startTime, endTime);
	}
}

