package bikeridenetwork;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

@Entity
public class BikeRideCircle {
	@Id private String myId;
	private String myDisplayName;
	private double latitude;
	private double longitude;
	
	private static ArrayList<Friends> myFriends;
}
