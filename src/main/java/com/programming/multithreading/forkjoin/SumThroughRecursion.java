package com.programming.multithreading.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SumThroughRecursion {
	
	public static class RecursiveSumTask extends RecursiveTask<Integer>{
		int start, end;
		int[] array;
		public RecursiveSumTask(int[] arr, int start, int end) {
			// TODO Auto-generated constructor stub
			array = arr;
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			if(end-start == 0) {
				return array[start];
			}
			int midPoint = start + (end-start)/2;
			RecursiveSumTask leftSum = new RecursiveSumTask(array, start, midPoint);
			RecursiveSumTask rightSum = new RecursiveSumTask(array, midPoint+1, end);
			rightSum.fork();
			return leftSum.compute() + rightSum.join();
			
		}

	}

	public static void main(String[] args) {
		int[] arr = {1,2,3,4,5,6,7,8,9,10,11};
		ForkJoinPool pool = ForkJoinPool.commonPool();
		RecursiveSumTask recursiveSumTask = new RecursiveSumTask(arr,0,arr.length-1);
		System.out.println(pool.invoke(recursiveSumTask));
	}

}
