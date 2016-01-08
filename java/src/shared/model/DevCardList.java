package shared.model;

import shared.definitions.DevCardType;

public class DevCardList{
	private int monopoly;
	private int monument;
	private int roadBuilding;
	private int soldier;
	private int yearOfPlenty;

	public DevCardList(int monopoly, int monument, int roadBuilding, int soldier, int yearOfPlenty) {
		this.monopoly = monopoly;
		this.monument = monument;
		this.roadBuilding = roadBuilding;
		this.soldier = soldier;
		this.yearOfPlenty = yearOfPlenty;
	}

	public DevCardList(){

	}

	/**
	 * Increments a dev card.
	 * @param type
	 */
	public void incDevCard(DevCardType type){
		switch(type){
			case MONOPOLY:
				monopoly++;
				break;
			case MONUMENT:
				monument++;
				break;
			case ROAD_BUILD:
				roadBuilding++;
				break;
			case SOLDIER:
				soldier++;
				break;
			case YEAR_OF_PLENTY:
				yearOfPlenty++;
				break;
		}
	}

	/**
	 * Decrements a dev card.
	 * @param type
	 */
	public void decDevCard(DevCardType type){
		switch(type){
			case MONOPOLY:
				monopoly--;
				break;
			case MONUMENT:
				monument--;
				break;
			case ROAD_BUILD:
				roadBuilding--;
				break;
			case SOLDIER:
				soldier--;
				break;
			case YEAR_OF_PLENTY:
				yearOfPlenty--;
				break;
		}
	}

	public int getMonopoly() {
		return monopoly;
	}

	public void setMonopoly(int monopoly) {
		this.monopoly = monopoly;
	}

	public int getMonument() {
		return monument;
	}

	public void setMonument(int monument) {
		this.monument = monument;
	}

	public int getRoadBuilding() {
		return roadBuilding;
	}

	public void setRoadBuilding(int roadBuilding) {
		this.roadBuilding = roadBuilding;
	}

	/**
	 * @pre none
	 * @post returns a number of soldier cards >= 0
	 * @return int
	 */
	public int getSoldier() {
		return soldier;
	}

	public void setSoldier(int soldier) {
		this.soldier = soldier;
	}

	public int getYearOfPlenty() {
		return yearOfPlenty;
	}

	public void setYearOfPlenty(int yearOfPlenty) {
		this.yearOfPlenty = yearOfPlenty;
	}

	/**
	 * Determines if the DevCardList is empty.
	 * @pre none
	 * @post returns true if all dev cards (monopoly, monument, roadbuilding, soldier, and year of plenty) are set to 0,
	 *	   false otherwise.
	 * @return boolean
	 */
	public boolean isEmpty(){
		return (getMonopoly() <= 0 &&
				getMonument() <= 0 &&
				getRoadBuilding() <= 0 &&
				getSoldier() <= 0 &&
				getYearOfPlenty() <= 0);
	}
}