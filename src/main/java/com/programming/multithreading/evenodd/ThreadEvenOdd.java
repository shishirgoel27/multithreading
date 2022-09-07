package com.programming.multithreading.evenodd;

public class ThreadEvenOdd {

	static int count = 0;

	public static void main(String[] args) {
		Class<? extends Class> class1 = ThreadEvenOdd.class.getClass();
		Thread even = new Thread(new EvenPrint(class1), "EVEN");
		Thread odd = new Thread(new OddPrint(class1), "ODD");
		even.start();
		odd.start();
	}

	static class EvenPrint implements Runnable {

		Class<? extends Class> clazz;

		public EvenPrint(Class<? extends Class> class1) {
			this.clazz = class1;
		}

		public void run() {
			synchronized (clazz) {
				while (count <= 10) {
					if (count % 2 != 0) {
						try {
							clazz.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println(Thread.currentThread().getName() + " " + count);
						clazz.notifyAll();
						count++;
					}
				}
			}
		}
	}

	static class OddPrint implements Runnable {
		Class<? extends Class> clazz;

		public OddPrint(Class<? extends Class> class1) {
			this.clazz = class1;
		}

		public void run() {
			synchronized (clazz) {
				while (count <= 10) {
					if (count % 2 == 0) {
						try {
							clazz.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println(Thread.currentThread().getName() + " " + count);
						clazz.notifyAll();
						count++;
					}
				}
			}
		}
	}
}
