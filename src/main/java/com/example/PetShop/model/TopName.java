package com.example.PetShop.model;

public class TopName implements ITopName{

	private String name;
	private int counter;
	
	TopName(){}
	
	TopName(String name, int counter){
		this.name = name;
		this.counter = counter;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String toString() {
		return "TopName [name=" + name + ", counter=" + counter + "]";
	}
	
}
