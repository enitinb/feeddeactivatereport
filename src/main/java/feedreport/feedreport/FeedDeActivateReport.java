package feedreport.feedreport;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FeedDeActivateReport {

	public static void main(String[] args) throws IOException, ParseException {


		/** Read the cruise feed and make a list of itin_cruise from S3 **/

		List<String> itinCruiseList = new ArrayList<String>();


		//JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		//This is the cruise feed from s3 archive for that day which already executed.
		FileReader reader = new FileReader("/Users/niteuseb/Documents/Business/Sapient_Project/Carnival/HAL_FEED_ISSUES/5_24_2018/hal_1527170401182_cruise-en_en_US.json");

		//Read cruise JSON file
		Object obj = jsonParser.parse(reader);
		JSONArray cruiseList = (JSONArray) obj;

		//Iterate the cruise list from json file
		Iterator<?> itr = cruiseList.iterator();

		while(itr.hasNext()) {

			JSONObject jsonObject = (JSONObject)itr.next();
			
			//Add combination of ItinID/CruiseID in the list 1
			itinCruiseList.add(jsonObject.get("itineraryId").toString()+"/"+jsonObject.get("id").toString());

		}

		System.out.println("count of itinCruiseList from feed--- " + itinCruiseList.size());
		System.out.println(itinCruiseList);
		System.out.println("*********************************");

		/** Read the query builder json results as is and make a combination of itin/cruise **/

		List<String> itinCruiseQBList = new ArrayList<String>();

		//This is the aem query builder json results as is saved as json file
		FileReader readerQB = new FileReader("/Users/niteuseb/Documents/Business/Sapient_Project/Carnival/HAL_FEED_ISSUES/5_24_2018/querybuilder.5_24.json");
		
		Object objQB = jsonParser.parse(readerQB);
		JSONObject cruiseListQBObj = (JSONObject) objQB;
		
		//The "hits" node has the object of cruises with fields which were exposed
		JSONArray cruiseListQB = (JSONArray) cruiseListQBObj.get("hits");

		//Iterate the cruise list from json file
		Iterator<?> itrQB = cruiseListQB.iterator();

		while(itrQB.hasNext()) {

			JSONObject jsonObject = (JSONObject)itrQB.next();

			String websitePath = jsonObject.get("websitePath").toString();
			
			//Prepare the itin/cruise from website path
			websitePath = websitePath.replaceAll("/content/hal/master_website/en_US/find-a-cruise/", "");
			
			//Add itin/cruise in the list
			itinCruiseQBList.add(websitePath);

		}
		
		
		/**Testing if the code is correct
		itinCruiseList.add("B9C14B/E936A");
		itinCruiseList.add("B9C14B/E942A");
		**/
		
		System.out.println("count of itinCruiseQBList from query builder--- " + itinCruiseQBList.size());
		System.out.println(itinCruiseQBList);
		System.out.println("*********************************");
		

		/** Final results **/
		
		System.out.println("removed---" + itinCruiseList.retainAll(itinCruiseQBList));
		System.out.println("activation list ---" + itinCruiseList);
		

	}

}
