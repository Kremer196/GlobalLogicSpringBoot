package com.example.globallogic;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;


@Service
public class MyService {
	
private final MyRepository repository;
	
	
	@Autowired
	public MyService(MyRepository repository) {
		super();
		this.repository = repository;
	}


	public List<GlobalLogic> getTests() {
		System.out.println(repository.findAll());
		return repository.findAll();
	}




	public List<GlobalLogic> analizeFormData(MultiValueMap<String, String> formData) {
		String pattern = formData.get("pattern").get(0);
		String sentence = formData.get("sentence").get(0);
		

		String[] wordsInSentence = sentence.split(" ");
		
		Map<Map<String, Integer>, Integer> resultMap = new HashMap<Map<String,Integer>, Integer>();
		
		int patternCharacters = countPatternCharacters(wordsInSentence, pattern);
		
		int totalCharacters =  countTotalCharacters(wordsInSentence);
		
		
		for(int i = 0; i < wordsInSentence.length; i++) {
			if(wordsInSentence[i].trim().equals("")) continue;
			updateMap(wordsInSentence[i], resultMap, pattern);
		}
			
			
		List<Map.Entry<Map<String, Integer>, Integer>> list = new ArrayList<Map.Entry<Map<String, Integer>, Integer>>(resultMap.entrySet());
		Collections.sort(list, new MyComparator<Map<String, Integer>, Integer>());
			
		String result = printResult(list, patternCharacters, totalCharacters);
		
		GlobalLogic newTest = new GlobalLogic(pattern, sentence, result);
		repository.save(newTest);

		return repository.findAll();
	}


	private String printResult(List<Entry<Map<String, Integer>, Integer>> list, int patternCharacters,int totalCharacters) {
		
		String results = "";
		for(int i = 0; i < list.size(); i++) {
			String partOfWord = "";
			int lengthOfWord = 0;
			for(Map.Entry<String, Integer> entry : list.get(i).getKey().entrySet()) {
				partOfWord = entry.getKey();
				lengthOfWord = entry.getValue(); 
			}
			results += ("{ (" + partOfWord + "), " + lengthOfWord + "} = " + (double) Math.round(list.get(i).getValue()*1.0/patternCharacters * 100) / 100 +" (" + list.get(i).getValue() + "/" + patternCharacters + ")\n");
		}
		
		results += ("TOTAL Frequency: " + (double) Math.round(patternCharacters*1.0/totalCharacters * 100) / 100 + " (" + patternCharacters + "/" + totalCharacters + ")\n");
		
		return results;
	}
		
	


	private void updateMap(String word, Map<Map<String, Integer>, Integer> resultMap, String pattern) {
		Set<String> set = new LinkedHashSet<String>();
		word = word.toLowerCase();
		int patternCount = 0;
		
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i))) && !set.contains(String.valueOf(word.charAt(i)))) {
				set.add(String.valueOf(word.charAt(i)));
				patternCount++;
			} else if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		final int patternCountFinal = patternCount;
		
		if (set.isEmpty()) {
			return;
		}
		reorderSet(set, pattern);
		
		Map<String, Integer> helpMap = new HashMap<String, Integer>();
		helpMap.put(set.toString(), countTotalCharactersInWord(word));
		
		if(!resultMap.containsKey(helpMap)) {
			resultMap.put(helpMap, patternCount);
		} else {
			resultMap.compute(helpMap, (key, val) -> val + patternCountFinal);
		}
		
		
	}


	private void reorderSet(Set<String> set, String pattern) {
		Set<String> reorderedSet = new LinkedHashSet<String>();
		
		for(int i = 0; i < pattern.length(); i++) {
			if(set.contains(String.valueOf(pattern.charAt(i)))) {
				reorderedSet.add(String.valueOf(pattern.charAt(i)));
				//System.out.println(reorderedSet);
			}
		}
		
		set.removeAll(set);
		set.addAll(reorderedSet);
		
	}


	private int countTotalCharacters(String[] wordsInSentence) {
		int totalCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			 if(wordsInSentence[i].trim().equals("")) continue;
			 totalCount += countTotalCharactersInWord(wordsInSentence[i]);
		}
		
		return totalCount;
	}


	private int countTotalCharactersInWord(String word) {
		word = word.trim();
		String invalidCharacters = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
		int count = 0;
		for(int i = 0; i < word.length(); i++) {
			if(!invalidCharacters.contains(String.valueOf(word.charAt(i)))) {
				count++;
			}
		}
		
		return count;
	}


	private int countPatternCharacters(String[] wordsInSentence, String pattern) {
		int totalPatternCount = 0;
		for(int i = 0; i < wordsInSentence.length; i++) {
			totalPatternCount += countPatternCharactersInWord(wordsInSentence[i], pattern);
		}
		return totalPatternCount;
	}


	private int countPatternCharactersInWord(String word, String pattern) {
		int patternCount = 0;
		word = word.toLowerCase();
		for(int i = 0; i < word.length(); i++) {
			if(pattern.contains(String.valueOf(word.charAt(i)))) {
				patternCount++;
			}
		}
		
		return patternCount;
	}
	
}
