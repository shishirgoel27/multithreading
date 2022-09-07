package com.programming.multithreading.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class WordLadder {
	
	public static void main(String[] args) {
		String beginWord = "cat";
		String endWord = "mob";
		List<String> wordList = List.of("cot","mob","cop", "mop", "cut", "cub");
//		String beginWord = "a";
//		String endWord = "x";
//		List<String> wordList = List.of("b","x","c","d");
		int minWordList = findMinWordList(beginWord, endWord, wordList);
		System.out.println(minWordList);
	}

	private static int findMinWordList(String beginWord, String endWord, List<String> wordList) {
		if(!wordList.contains(endWord) || beginWord.equalsIgnoreCase(endWord)) {
			return 0;
		}
		BuildWordChain task = new BuildWordChain(beginWord, endWord, wordList, 0);
		Integer result = ForkJoinPool.commonPool().invoke(task);
		return  result == Integer.MAX_VALUE ? 0 : 1 + result;
	}
	
	private static class BuildWordChain extends RecursiveTask<Integer> {
		
		private static final char[] ALPHABETS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		private String beginWord;
		private String endWord;
		private List<String> wordList;
		private int countSoFar;

		BuildWordChain(String beginWord, String endWord, List<String> wordList, int countSoFar) {
			this.beginWord = beginWord;
			this.endWord = endWord;
			this.wordList = new ArrayList<>(wordList);
			this.countSoFar = countSoFar;
		}

		@Override
		protected Integer compute() {
			if(beginWord.equalsIgnoreCase(endWord)) {
				return countSoFar;
			}
			List<BuildWordChain> subTasks = new ArrayList<>();
			for(int i=0;i<beginWord.length();i++) {
				StringBuilder builder = new StringBuilder(beginWord);
				for(int j=0;j<ALPHABETS.length;j++) {
					if(beginWord.charAt(i) == ALPHABETS[j]) {
						continue;
					}
					builder.setCharAt(i, ALPHABETS[j]);
					if(wordList.contains(builder.toString())) {
						List<String> tempWordList = new ArrayList<>(wordList);
						tempWordList.remove(builder.toString());
						BuildWordChain modifiedWord = new BuildWordChain(builder.toString(), endWord, tempWordList, countSoFar+1);
						subTasks.add(modifiedWord);
					}
				}
			}
			if(subTasks.size() == 0) {
				return Integer.MAX_VALUE;
			}
			List<ForkJoinTask<Integer>> forkedTasks = subTasks.stream().map(BuildWordChain::fork).collect(Collectors.toList());
			return forkedTasks.stream().map(ForkJoinTask<Integer>::join).min(Integer::compare).get();
		}

		
	}
	
	

}
