package bikeridenetwork;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UploadBikeCircle extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		BikeRideCircle currentCircle = OfyService.ofy().load().type(BikeRideCircle.class).id(req.getParameter("")).get();
		
		//This is the first time user has logged in, create a new circle in the datastore
		if (currentCircle == null) {
			currentCircle = new BikeRideCircle();
			OfyService.ofy().save().entity(currentCircle);
		}
		else {
			//Update the Friends arraylist in case they have added more friends
			
			//Add in current latitude/longitude
			
			//Save updated copy back into datastore
			
			//Send back user's circle in JSON form so we can process in Android app
		}
	}
}
