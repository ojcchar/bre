package edu.utdallas.seers.bre.javabre.entity.words;

import java.util.ArrayList;
import java.util.List;

public class WordData {

	private String lemma;
	private int freq;
	private List<WordCodeData> codeData;

	public WordData(String lemma, int freq, ArrayList<WordCodeData> codeData) {
		this.lemma = lemma;
		this.freq = freq;
		this.codeData = codeData;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public List<WordCodeData> getCodeData() {
		return codeData;
	}

	public void setCodeData(List<WordCodeData> codeData) {
		this.codeData = codeData;
	}

	public void addFreq() {
		++this.freq;
	}

}
